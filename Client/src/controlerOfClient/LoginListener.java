package controlerOfClient;
import dataPackageClass.User;
import messageHandlerOfClient.ClientControlCollection;
import messageHandlerOfClient.ClientManage;
import viewOfClient.LoginFrame;
import viewOfClient.RegisterFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static dataPackageClass.MessageTypeInterface.*;

/**
 * @author Florence
 * 登录监听器
 */
public class LoginListener implements ActionListener {
    LoginFrame theFrame;
    String account;
    String passWord;
    ClientManage handler;
    User myUser;
    /**
     * 构造函数传入登录窗口句柄
     * @param frame 登录窗口
     */
    public LoginListener(LoginFrame frame){
        theFrame=frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==theFrame.login){
            account=theFrame.userName.getText();
            passWord= String.valueOf(theFrame.userPassword.getPassword());
             //先检查是否账号密码为空，如果不为空再进行服务器端判断，防止多余判断
            if("".equals(account)){
                JOptionPane.showMessageDialog(null, "账号不能为空！");
                return;
            }
            else if("".equals(passWord)){
                JOptionPane.showMessageDialog(null, "密码不能为空！");
                return;
            }
            handler=new ClientManage();
            if(!(handler.IsConnect())){
                JOptionPane.showMessageDialog(null, "连接服务器失败");
                return;
            }
            //服务器已经连接，进行消息发送
            myUser=new User(account,passWord);
            //发送消息，用statu来进行状态判断
            String statu = handler.Login(myUser,true);
            switch (statu) {
                case Login_Success:
                    System.out.println("登录成功");
                    theFrame.dispose();
                    break;
                case Login:
                    System.out.println("用户已登录");
                    JOptionPane.showMessageDialog(null, "用户正在登录，请待会再试以下");
                    break;
                case Login_Fail_PasswordWrong:
                    System.out.println("账号存在但密码错误");
                    JOptionPane.showMessageDialog(null, "密码错误请重新输入");
                    theFrame.userPassword.setText("");
                    break;
                case Login_Fail_NoUser:
                    System.out.println("用户名不存在");
                    JOptionPane.showMessageDialog(null, "用户名不存在");
                    theFrame.userPassword.setText("");
                    break;
                default:
                    System.out.println("未知错误，请检查服务器端");
                    JOptionPane.showMessageDialog(null, "服务器端出现未知错误，请联系管理员Debug");
                    break;
            }
        }
        //注册
        if(e.getSource()==theFrame.register){
            new RegisterFrame();
        }
    }
}
