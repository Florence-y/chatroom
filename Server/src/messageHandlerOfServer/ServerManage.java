package messageHandlerOfServer;
import dataPackageClass.Message;
import dataPackageClass.User;
import toolOfServer.LogFunction;
import toolOfServer.ServerConnectDatabase;
import toolOfServer.userLable;
import viewOfServer.ServerFrame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


import static dataPackageClass.MessageTypeInterface.*;
/**
 * @author Florence
 * 此类为处理登录与注册的监听类
 */
public class ServerManage implements Runnable {
    private ServerSocket ss;
    private ObjectInputStream ois;
    private ObjectOutputStream os;
    private ServerConnectDatabase server;
    private ServerFrame myFrame;
    public ServerManage(ServerFrame frame) {
        myFrame=frame;
        try {
            ss = new ServerSocket(9999);
            System.out.println("正在服务器监听");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {//下面的if分支用continue让结构更加清晰
        try {
            while (true) {
                try {
                    //接受客户端发送过来的信息
                    Socket s;
                    try {
                        s = ss.accept();
                        System.out.println("收到连接");
                    } catch (SocketException e) {
                        break;
                    }
                    ois = new ObjectInputStream(s.getInputStream());
                    User user = (User) ois.readObject();
                    //一：如果读取到的是用户注册的信息
                    if (user.getType().equals(UserRegister)) {
                        server = new ServerConnectDatabase();
                        Message mess = new Message();
                        //先确认账号是否存在
                        if(server.CheckIsExist(user)){
                            mess.setMessageType(Register_Fail_alreadyExist);
                            os=new ObjectOutputStream(s.getOutputStream());
                            os.writeObject(mess);
                            continue;
                        }
                        //如果账号不存在，在进行注册
                        if (server.CheckRegister(user)) {
                            mess.setMessageType(Register_Success);
                            //日志记录
                            LogFunction.outLog("用户： "+user.getName()+"   消息： 注册成功");
                        }
                        //未知错误服务器端
                        else {
                            mess.setMessageType(Register_Fail);
                            //日志记录
                            LogFunction.outLog("用户： "+user.getName()+"   消息： 注册失败");
                        }
                        os = new ObjectOutputStream(s.getOutputStream());
                        os.writeObject(mess);
                        continue;
                    }
                    //二：如果读取到的是用户登陆的信息
                    if(user.getType().equals(UserLogin)){
                        server = new ServerConnectDatabase();
                        Message mess = new Message();
                        //用户名不存在
                        if(!(server.CheckIsExist(user))){
                            mess.setMessageType(Login_Fail_NoUser);
                            mess.setUser(user);
                            os = new ObjectOutputStream(s.getOutputStream());
                            os.writeObject(mess);
                            continue;
                        }
                        //用户已经登录
                        if((server.Check_IsLogin(user))){
                            mess.setMessageType(Login);
                            mess.setUser(user);
                            os = new ObjectOutputStream(s.getOutputStream());
                            os.writeObject(mess);
                            continue;
                        }
                        //用户名存在，且未登录，这才需要我们验证密码
                        String statu=server.CheckLogin(user);
                        //成功登录
                        if(statu.equals(Login_Success)){
                            //先发送登录成功信息
                            mess.setMessageType(Login_Success);
                            os = new ObjectOutputStream(s.getOutputStream());
                            os.writeObject(mess);
                            ContactWithClientThread userThread=new ContactWithClientThread(s,user.getName(),myFrame);
                            //将线程放进集合
                            ServerConnectCollection.addServerConnectClientThreadCollection(user.getName(),userThread);
                            //更新登录情况（登录情况记录在数据库里）
                            server.Update_IsLogin(user,1);
                            //将面板添加进容器，添加在线列表成员并且刷新（不刷新不显示）
                            userLable card = new userLable(user.getName());
                            ServerConnectCollection.addUserCard(user.getName(),card);
                            //添加面板，并且更新面板
                            myFrame.mainPanel.add(card);
                            myFrame.updateFrame();
                            System.out.println("登录成功");
                            LogFunction.outLog("用户： "+user.getName()+"   消息： 登录");
                            //控制台输出一下当前在线名单
                            System.out.println(ServerConnectCollection.GetOnline());
                            userThread.updateOnline();
                            userThread.sendOnlineALL();
                            //线程池执行线程
                            ServerConnectCollection.threadPool.execute(userThread);
                        }
                        //密码错误
                        else if(statu.equals(Login_Fail_PasswordWrong)){
                            mess.setMessageType(Login_Fail_PasswordWrong);
                            os = new ObjectOutputStream(s.getOutputStream());
                            os.writeObject(mess);
                        }
                        //未知错误
                        else {
                            mess.setMessageType(Login_Fail);
                            os = new ObjectOutputStream(s.getOutputStream());
                            os.writeObject(mess);
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    System.out.println("客户端登录界面断开");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("客户端登录界面断开");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void CloseServer(){
        try {
            //获得在线用户的名字
            String string = ServerConnectCollection.GetOnline();
            String[] strings = string.split(" ");
            for (String s : strings) {
                //获得其他服务器端与客户端通信的线程
                ContactWithClientThread subClientThread = ServerConnectCollection.getServerContinueConnectClient(s);
                ServerConnectCollection.RemoveServerContinueConnetClient(s);
                //如果不为空就关闭
                if (subClientThread != null) {
                    subClientThread.CloseThread();
                }
            }
            //关闭客户端socket
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //---------------------------------------------------------“分割线”---------------------------------------------------------------------------
    /*public static void main(String[] args) {
        Thread thetestThread = new Thread(new ServerManage(new ServerFrame()));
        thetestThread.start();
    }*/

    /*
     * 发送有新用户登录的消息给在线用户
     * @param justLoginUser 刚刚登录的用户
    public void updataOnline(String justLoginUser){
        //获得在线用户
        String string = ServerConnectCollection.GetOnline();
        //创建一个消息包，包含内容为刚登录的成员
        Message mess = new Message();
        mess.setSender(justLoginUser);
        String[] strings = string.split(" ");
        for (String Getter : strings) {
            //发送在线用户的名单
            mess.setGetter(Getter);
            mess.setMessageType(Just_login);
            try {
                //取出每个服务器端与客户端通信的线程
                if(Getter.equals(justLoginUser)) {
                    continue;
                }
                ContactWithClientThread thread = ServerConnectCollection.getServerContinueConnectClient(Getter);
                if (thread != null) {
                    os = new ObjectOutputStream(thread.contactSocket.getOutputStream());
                    os.writeObject(mess);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
    //-------------------------------------------------------------------“结束”----------------------------------------------------------------------------
}

