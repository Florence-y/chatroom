package messageHandlerOfServer;
import dataPackageClass.Message;
import sun.rmi.runtime.Log;
import toolOfServer.LogFunction;
import toolOfServer.ServerConnectDatabase;
import toolOfServer.userLable;
import viewOfServer.ServerFrame;
import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import static dataPackageClass.MessageTypeInterface.*;

/**
 * @author Florence
 * 登录后服务器持续与客户端通信的类
 */
public class ContactWithClientThread implements Runnable {
    public Socket contactSocket;
    private String user;
    private ServerFrame myFrame;
    private ObjectInputStream ois;
    private ObjectOutputStream os;
    private boolean isConnect = true;
    private ServerConnectDatabase handler;
    public ContactWithClientThread(Socket s, String userName, ServerFrame frame){
        contactSocket=s;
        user=userName;
        myFrame=frame;
        handler=new ServerConnectDatabase();
    }
    @Override
    public void run() {
        System.out.println("线程启动");
        try {
            //首先判读是否连接
            while (isConnect) {
                //如果连接那么接口Socket是否关闭，如果关闭说明登陆后的用户已经断开连接（原因未知，总之先把他移除了再说）
                if(contactSocket.isClosed()){
                    System.out.println("1");
                    ServerConnectCollection.RemoveServerContinueConnetClient(user);
                    serverUpdateOnline();
                    sendALLJustOffLine();
                    break;
                }
                //debug用于判断是哪个用户除了错
                System.out.println(user);
                try {
                    ois=new ObjectInputStream(contactSocket.getInputStream());
                }catch (SocketException e){
                    ServerConnectCollection.RemoveServerContinueConnetClient(user);
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+" 消息： 下线");
                    //更新服务器的在线用户
                    serverUpdateOnline();
                    //通知其他人更新在线用户
                    sendALLJustOffLine();
                    //关闭Socket
                    CloseThread();
                    System.out.println("Socket出问题");
                    e.printStackTrace();
                    break;
                }catch (EOFException e){
                    System.out.println("异常关闭服务器");
                    //输出日志消息
                    LogFunction.outLog("消息： 服务器异常关闭");
                    e.printStackTrace();
                }
                System.out.println("初始化成功");
                Message comeFromClientMessage = (Message)ois.readObject();
                //debug用
                System.out.println(user+"收到"+comeFromClientMessage.getMessageType());
                System.out.println("收到信息");
                String messType=comeFromClientMessage.getMessageType();
                boolean status;
                //登陆后的用户请求寻找用户是否存在（添加好友时需要）
                if(Make_SURE_USER_ISEXIST.equals(messType)){
                    status=handler.CheckIsExist(comeFromClientMessage.getUser());
                    Message mess=new Message();
                    mess.setMessageType(ADD_Friend);
                    mess.setUser(comeFromClientMessage.getUser());
                    //好友存在
                    if(status){
                        mess.setContent(Register_Fail_alreadyExist);
                    }
                    //不存在
                    else {
                        mess.setContent(NO_EXIST);
                    }
                    os = new ObjectOutputStream(contactSocket.getOutputStream());
                    os.writeObject(mess);
                }
                //确认添加的用户是否在线
                else if(Make_SURE_USER_ISONLINE.equals(messType)){
                    status=handler.Check_IsLogin(comeFromClientMessage.getUser());
                    Message mess=new Message();
                    mess.setMessageType(ADD_Friend);
                    //在线
                    if(status){
                        mess.setContent(Login);
                        mess.setGetter(comeFromClientMessage.getUser().getName());
                    }
                    //不在线
                    else{
                        mess.setContent(NoLogin);
                    }
                    os = new ObjectOutputStream(contactSocket.getOutputStream());
                    os.writeObject(mess);
                    System.out.println("发送成功登录");
                }
                //普通消息
                else if(Common_Message_ToPerson.equals(messType)){
                    sentPersonMessage(comeFromClientMessage);
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+"   消息： 给"+comeFromClientMessage.getGetter()+"发文字信息");
                }
                //群消息
                else if(Common_Message_ToGroup.equals(messType)){
                    sentGroupMessage(comeFromClientMessage);
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+"   群聊名字： "+comeFromClientMessage.getMessageTypeDetail()+
                            "   消息： 给"+comeFromClientMessage.getGetter()+"发文字信息");
                }
                //文件消息
                else if(Send_FileToPerson.equals(messType)){
                    //执行发送文件线程
                    ServerConnectCollection.threadPool.execute(new ServerSendFileThread(comeFromClientMessage,comeFromClientMessage.getContent()));
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+"   消息： 给"+comeFromClientMessage.getGetter()+"发文件，文件名为"+
                            comeFromClientMessage.getContent());
                }
                //群文件消息
                else if(Send_FileToGroup.equals(messType)){
                    //执行发送文件线程
                    ServerConnectCollection.threadPool.execute(new ServerSendFileThread(comeFromClientMessage,comeFromClientMessage.getContent()));
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+"   群聊名字： "+comeFromClientMessage.getMessageTypeDetail()
                            +"   消息： 给"+comeFromClientMessage.getGetter().trim()+"发文件，文件名为"+ comeFromClientMessage.getContent());
                }
                //表情信息
                else if(SEND_EMOTION.equals(messType)){
                    sentPersonMessage(comeFromClientMessage);
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+"   消息： 给"+comeFromClientMessage.getGetter()+"发表情");
                }
                else if(SEND_EMOTIONToAll.equals(messType)){
                    sentGroupMessage(comeFromClientMessage);
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+"   群聊名字： "+comeFromClientMessage.getMessageTypeDetail()
                            +"   消息： 给"+comeFromClientMessage.getGetter().trim()+"发表情信息");
                }
                //下线信息
                else if(Just_OFF_Line.equals(messType)){
                    //先将某个用户下线的信息发送给他的好友
                    comeFromClientMessage.setMessageType(Just_OFF_Line);
                    comeFromClientMessage.setGetter(ServerConnectCollection.GetOnline());
                    sentGroupMessage(comeFromClientMessage);
                    //新建一个信息包 ，告诉发送下线消息的那个用户可以下线了（也就是反馈给那个要下线的用户的信息）
                    Message mess2 = new Message();
                    mess2.setMessageType(YOU_CAN_OFF_LINE);
                    os=new ObjectOutputStream(contactSocket.getOutputStream());
                    os.writeObject(mess2);
                    //关闭资源与线程
                    ServerConnectCollection.RemoveServerContinueConnetClient(comeFromClientMessage.getSender());
                    //更新服务器在线人员
                    myFrame.mainPanel.removeAll();
                    serverUpdateOnline();
                    //输出日志消息
                    LogFunction.outLog("用户： "+user+"   消息： 下线");
                    break;
                }
            }
        sendALLJustOffLine();
        } catch (SocketException e){
            e.printStackTrace();
            sendALLJustOffLine();
        } catch (EOFException e){
            e.printStackTrace();
            sendALLJustOffLine();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            sendALLJustOffLine();
        }
    }

    /**
     * 发送群体消息
     * @param mess
     */
    private void sentGroupMessage(Message mess){
        //获取接受的用户（也就是要发送的用户）
        String[] sendUsers = mess.getGetter().split(" ");
        for (String getter : sendUsers) {
            if(getter.equals(mess.getSender())){
                continue;
            }
            try {
                //取出每个服务器端与客户端通信的线程
                ContactWithClientThread thread = ServerConnectCollection.getServerContinueConnectClient(getter);
                if (thread != null) {
                    os = new ObjectOutputStream(thread.contactSocket.getOutputStream());
                    os.writeObject(mess);
                }
            } catch (SocketException e){
               ServerConnectCollection.RemoveServerContinueConnetClient(getter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单发消息类
     * @param mess 消息包
     */
    private void sentPersonMessage(Message mess){
        //获取接受的用户（也就是要发送的用户）
        String sentUser=mess.getGetter();
        ContactWithClientThread thread=ServerConnectCollection.getServerContinueConnectClient(sentUser);
        try {
            if(thread!=null){
                os=new ObjectOutputStream(thread.contactSocket.getOutputStream());
                os.writeObject(mess);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    /**
     * 服务器面板更新用户（用户上下线与强制下线要用到）
     */
    private void serverUpdateOnline() {
        //获取在线成员名称
        String string =ServerConnectCollection.GetOnline();
        if("".equals(string)){
            return ;
        }
        //先移除全部
        myFrame.mainPanel.removeAll();
        //分隔开
        String[] strings = string.split(" ");
        //先加入一个开头标签
        myFrame.mainPanel.add(new JLabel("在线成员"));
        //循环加入
        for (String s : strings) {
            myFrame.mainPanel.add(new userLable(s));
        }
        myFrame.updateFrame();
    }
    /**
     * 关闭线程序的方法
     */
    public void CloseThread(){
        isConnect = false;
        try {
            contactSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向其他用户发送他下线的消息（一个用户下线要用到）
     */
    public void sendALLJustOffLine(){
        //向用户群发消息他下线了
        Message mess =  new Message();
        mess.setSender(user);
        mess.setMessageType(Just_OFF_Line);
        mess.setGetter(ServerConnectCollection.GetOnline());
        sentGroupMessage(mess);
    }
    /**
     * 向用户发送当前在线的所有成员（一个用户上线会用到）
     */
    public void sendOnlineALL(){
        //获得在线用户,并且转入包中发送
        String string = ServerConnectCollection.GetOnline();
        Message mess=new Message();
        mess.setContent(string);
        mess.setGetter(user);
        mess.setMessageType(Send_Online);
        try {
            os=new ObjectOutputStream(contactSocket.getOutputStream());
            os.writeObject(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送有新用户登录的消息给在线用户
     */
    public void updateOnline(){
        //获得在线用户
        String string = ServerConnectCollection.GetOnline();
        //创建一个消息包，包含内容为刚登录的成员
        Message mess = new Message();
        mess.setSender(user);
        String[] strings = string.split(" ");
        for (String Getter : strings) {
            //发送在线用户的名单
            mess.setGetter(Getter);
            mess.setMessageType(Just_login);
            try {
                //别发给自己
                if(Getter.equals(user)) {
                    continue;
                }
                //取出每个服务器端与客户端通信的线程
                ContactWithClientThread thread = ServerConnectCollection.getServerContinueConnectClient(Getter);
                //发送信息包
                if (thread != null) {
                    try {
                        os = new ObjectOutputStream(thread.contactSocket.getOutputStream());
                        os.writeObject(mess);
                    }catch (SocketException e){
                        ServerConnectCollection.RemoveServerContinueConnetClient(Getter);
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
