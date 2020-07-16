package threadOfClient;
import dataPackageClass.Message;
import messageHandlerOfClient.ClientControlCollection;
import messageHandlerOfClient.ClientManage;
import messageHandlerOfClient.MessageHandler;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Florence
 * 用来登录后接受服务器的线程，只用来接受信息，信息的处理交给MessageHandler类处理
 */
public class contactWithServerThread implements Runnable {
    private Socket contactWithSocket;
    private ClientManage myMange;
    private Message mess;
    private ObjectOutputStream os;
    private ObjectInputStream ois;
    String myName;
    public contactWithServerThread(Socket s, ClientManage manage,String name){
        contactWithSocket=s;
        myMange=manage;
        myName=name;
    }
    @Override
    public void run() {
        while (true){
            Message mess = myMange.reciveMessage();
            if(myMange.IsConnect()){
                if(mess!=null) {
                    //将收到的信息交给信息处理器！！！！！！！！！！！！
                    MessageHandler.messageUse(mess);
                }
            }
            else {
                //说明被强制下线了
                ClientControlCollection.myManage.CloseResource();
                ClientControlCollection.myManage.setIsConnect(false);
                JOptionPane.showMessageDialog(null,"@服务器信息：下线成功，期待下一次见面哦（你可能出现连接异常咯！）");
                //确定后两秒退出
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }
    }
}
