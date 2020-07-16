package viewOfServer;
import messageHandlerOfServer.ServerConnectCollection;
import messageHandlerOfServer.ServerManage;
import controlerOfServer.ServerListener;
import toolOfServer.BeautyTool;
import toolOfServer.LogFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Florence
 *服务器界面
 */
public class ServerFrame extends JFrame implements WindowListener {
    public JButton startButton= new JButton("启动服务器");
    public JButton stopButton = new JButton("关闭服务器");
    public JButton allUser = new JButton("所有用户");
    public JPanel mainPanel = new JPanel();
    JLabel onlinePeople = new JLabel("在线成员");
    Box firstLine;
    Box onlineBox;
    /**
     * 后台线程类
     */
    ServerManage userHandler;
    /**
     * 服务器按钮监听器
     */
    ServerListener listener=new ServerListener(this, userHandler);
    public ServerFrame(){
        /**
         * 常规面板设置，可以忽略
         */
        super.setTitle("服务器");
        ServerConnectCollection.serverFrame=this;
        mainPanel.setLayout(new FlowLayout(1,50,10));
        firstLine=Box.createHorizontalBox();
        firstLine.add(Box.createHorizontalStrut(20));
        firstLine.add(startButton);
        firstLine.add(Box.createHorizontalStrut(20));
        firstLine.add(stopButton);
        firstLine.add(Box.createHorizontalStrut(20));
        firstLine.add(allUser);
        firstLine.add(Box.createHorizontalStrut(20));
        firstLine.setBorder(BorderFactory.createEmptyBorder(12, 12, 12,12));
        onlineBox=Box.createHorizontalBox();
        onlineBox.add(Box.createHorizontalStrut(100));
        onlineBox.add(onlinePeople);
        onlineBox.add(Box.createHorizontalStrut(100));
        mainPanel.add(onlineBox);
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        /**
         * 添加监听器和全局设置并且开启线程
         */
        LogFunction.outLog("消息： 服务器软件启动");
        setListener();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.add(firstLine,BorderLayout.NORTH);
        this.add(mainPanel);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(ServerFrame.class.getResource("/images/服务器监控.png")));
        this.setSize(400,600);
        this.setLocationRelativeTo(null);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    /*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        try {
            BeautyTool.makeBeauty();
        }catch(Exception e) {
            e.printStackTrace();
        }
        ServerFrame b=new ServerFrame();

    }

     --------------------------------------------------------------------------------*/

    /**
     * 设定监听器
     */
    private void setListener(){
        stopButton.addActionListener(listener);
        startButton.addActionListener(listener);
        allUser.addActionListener(listener);
        this.addWindowListener(this);
    }

    /**
     * 用来更新界面的方法
     */
    public void updateFrame(){
        mainPanel.updateUI();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * 重现关闭按钮事件
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int status= JOptionPane.showConfirmDialog(null,"确认关闭服务器吗？（在点击关闭服务器按钮后才能关闭）","确认",JOptionPane.YES_NO_OPTION);
        if(status==JOptionPane.YES_OPTION){
            if(ServerConnectCollection.myManage!=null){
                JOptionPane.showMessageDialog(null,"服务器正在运行，请关闭服务器后再进行操作");
                return;
            }
            LogFunction.outLog("消息： 服务器软件退出");
            System.out.println("关闭成功");
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
