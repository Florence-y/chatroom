package viewOfClient;

import controlerOfClient.LoginListener;
import toolOfClient.BeautyTool;
import toolOfClient.MyPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Florence
 */
public class LoginFrame extends JFrame {
    public JButton login = new JButton("登录");
    public JButton register = new JButton("注册");
    public JTextField userName = new JTextField(20);
    public JPasswordField userPassword = new JPasswordField(20);
    JLabel  name = new JLabel();
    JLabel password = new JLabel();
    MyPanel mainPanel = new MyPanel();
    LoginListener Listener= new LoginListener(this);
    public LoginFrame(){
        /**
         * 设置相关按钮和标签的大小
         */
        login.setPreferredSize(new Dimension(100,50));
        register.setPreferredSize(new Dimension(100,50));
        name.setPreferredSize(new Dimension(48,48));
        password.setPreferredSize(new Dimension(48,80));
        userName.setPreferredSize(new Dimension(48,40));
        userPassword.setPreferredSize(new Dimension(48,40));
        name.setIcon(new ImageIcon(LoginFrame.class.getResource("/images/账号.png")));
        password.setIcon(new ImageIcon(LoginFrame.class.getResource("/images/密码锁.png")));
        /**
         * 设置布局和添加控件
         */
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        mainPanel.add(name);
        mainPanel.add(userName);
        mainPanel.add(password);
        mainPanel.add(userPassword);
        mainPanel.add(login);
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(register);
        /**
         * 一些布局的设置
         */
        name.setHorizontalAlignment(SwingConstants.LEFT);
        password.setHorizontalAlignment(SwingConstants.LEFT);
        /**
         * 整个界面的细节处理,以及常规设置，以及最后的添加监听器（自定义方法添加）
         */
        this.getRootPane().setDefaultButton(login);
        this.add(mainPanel,BorderLayout.CENTER);
        this.setTitle("聊天室登录界面");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Images/聊天.png")));
        this.setLocation(700,300);
        this.setPreferredSize(new Dimension(360,280));
        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setListener();
    }
/*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        BeautyTool.makeBeauty();
        new LoginFrame();
    }
    ------------------------------------------------------------------------------*/



    private void setListener(){
        login.addActionListener(Listener);
        register.addActionListener(Listener);
    }
}
