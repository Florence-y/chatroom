package controlerOfServer;

import messageHandlerOfServer.ServerConnectCollection;
import messageHandlerOfServer.ServerManage;
import toolOfServer.LogFunction;
import viewOfServer.ServerFrame;
import viewOfServer.showAllUsersFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florence
 * 服务器界面监听器
 */
public class ServerListener  implements ActionListener {
    private ServerFrame myFrame;
    private ServerManage myManage;
    private Thread myThread;
    public ServerListener(ServerFrame frame, ServerManage manage){
        myFrame=frame;
        myManage=manage;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //启动服务器
        if(e.getSource()==myFrame.startButton){
            myManage=new ServerManage(myFrame);
            ServerConnectCollection.myManage=myManage;
            //启动服务器管理线程
            ServerConnectCollection.threadPool.execute(myManage);
            myFrame.startButton.setEnabled(false);
            myFrame.stopButton.setEnabled(true);
            //输出日志
            LogFunction.outLog("消息： 服务器启动");
            System.out.println("启动成功");
        }
        //关闭服务器
        if(e.getSource()==myFrame.stopButton){
            //关闭服务器线程与删除客户端线程
            myManage.CloseServer();
            ServerConnectCollection.myManage=null;
            myFrame.startButton.setEnabled(true);
            myFrame.stopButton.setEnabled(false);
            //清空全部控件
            myFrame.mainPanel.removeAll();
            //添加一个最上面的在线成员标签
            myFrame.mainPanel.add(new JLabel("在线成员"));
            //刷新面板
            myFrame.updateFrame();
            //输出日志
            LogFunction.outLog("消息： 服务器关闭");
            System.out.println("关闭成功");
        }
        if(e.getSource()==myFrame.allUser){
            new showAllUsersFrame();
        }
    }
}
