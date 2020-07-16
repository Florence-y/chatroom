package messageHandlerOfServer;

import dataPackageClass.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static dataPackageClass.MessageTypeInterface.*;

/**
 * @author Florence
 * 发送文件给客户端的方法
 * 思路：首先客户端先发送一个Message包告诉服务器我要发文件了，
 * 文件名是xxx，然后服务器接受到这个信息，启动转发文件线程（也就是这个线程），在这个线程里面服务器先接受文件
 * 存在本地，然后向转发目标发送消息包说明我要发送文件给你啦，然后客户端收到信息打开Socket接收文件
 */
public class ServerSendFileThread implements Runnable {
    private Socket s;
    private ServerSocket ss;
    private ObjectInputStream ois;
    private ObjectOutputStream os;
    private Message message;
    private BufferedInputStream bis;
    private BufferedOutputStream bos;
    public ServerSendFileThread (Message mess,String type){
        message = mess;
        try {
            //先打开一个socket接受文件
            ss = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            s = ss.accept();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            //设置文件路径
            File file = new File("Server/src/recivedFiles/"+message.getContent());
            if(!file.exists()){
                try {
                    //获取文件流
                    bis = new BufferedInputStream((s.getInputStream()));
                    bos = new BufferedOutputStream(new FileOutputStream("Server/src/recivedFiles/"+message.getContent()));
                } catch (IOException e){
                    e.printStackTrace();
                }
                byte[] bys = new byte[1024];
                int len;
                //先接受从客户端来的文件
                while ((len = bis.read(bys)) != -1) {
                    bos.write(bys, 0, len);
                    bos.flush();
                }
            }
            //根据信息的类型选择发送群文件还是个人文件（用已经接受的文件发送给客户端）
            if(message.getMessageType().equals(Send_FileToGroup)){
                //发送给所有人
                SendFileToGroup("Server/src/recivedFiles/"+message.getContent());
            }else if(message.getMessageType().equals(Send_FileToPerson)){
                //发送给个人
                SendFileToPerson("Server/src/recivedFiles/"+message.getContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(bis!=null){
                    bis.close();
                }
                if(ss!=null){
                    ss.close();
                }
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 发送给个人
     */
    public void SendFileToPerson(String FileName){
        Socket s1 = null;
        try {
            //根据获得者取得服务器端与客户端通信的线程
            ContactWithClientThread thread = ServerConnectCollection.getServerContinueConnectClient(message.getGetter());
            bis = new BufferedInputStream(new FileInputStream(FileName));
            os = new ObjectOutputStream(thread.contactSocket.getOutputStream());
            os.writeObject(message);
            //获取IP地址
            InetAddress ip = thread.contactSocket.getInetAddress();
            //连接客户端
            s1 = new Socket(ip, 7777);
            bos = new BufferedOutputStream(s1.getOutputStream());
            byte[] bys = new byte[1024];
            int len;
            //发送文件
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(s1!=null){
                    s1.shutdownOutput();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if(s1!=null){
                    s1.close();
                }
                if(bis!=null){
                    bis.close();
                }
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * 发送文件给所有人
     */
    public void SendFileToGroup(String FileName){
        //获得好友名字
        String string = message.getGetter();
        String[] strings = string.split(" ");
        String Name;
        //for循环分别取出要发送的好友名
        for (String value : strings) {
            Name = value;
            if (!message.getSender().equals(Name)) {
                //设置接收用户
                Socket s1 = null;
                message.setGetter(Name);
                try {
                    bis = new BufferedInputStream(new FileInputStream(FileName));
                    //获得其他服务器端与客户端通信的线程
                    ContactWithClientThread thread = ServerConnectCollection.getServerContinueConnectClient(Name);
                    //如果为空直接返回
                    if(thread==null){
                        continue;
                    }
                    //先发送消息包然后发送文件
                    os = new ObjectOutputStream(thread.contactSocket.getOutputStream());
                    os.writeObject(message);
                    //获取IP地址
                    InetAddress ip = thread.contactSocket.getInetAddress();
                    //连接客户端
                    s1 = new Socket(ip, 7777);
                    bos = new BufferedOutputStream(s1.getOutputStream());
                    byte[] bys = new byte[1024];
                    int len;
                    //发送文件
                    while ((len = bis.read(bys)) != -1) {
                        bos.write(bys, 0, len);
                        bos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //发送完关闭接口
                } finally {
                    try {
                        if(s1!=null) {
                            s1.shutdownOutput();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        if (s1 != null) {
                            s1.close();
                        }
                        if (bis != null) {
                            bis.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //休眠避免同步执行造成异常
                    try {
                        Thread.sleep(500);
                        Thread.yield();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
