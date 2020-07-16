package controlerOfClient;
import dataPackageClass.User;
import messageHandlerOfClient.ClientManage;
import viewOfClient.RegisterFrame;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static dataPackageClass.MessageTypeInterface.*;

/**
 * 注册界面控制器
 * @author Florence
 */
public class RegisterListener implements ActionListener, KeyListener {
    private RegisterFrame theFrame;
    String account;
    String passWord;
    String aPassWord;
    ClientManage handler;
    public RegisterListener(RegisterFrame myFrame){
        theFrame=myFrame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //登录按钮
        if(e.getSource()==theFrame.register){
            account=theFrame.name.getText();
            passWord= String.valueOf(theFrame.firstPassword.getPassword());
            aPassWord=String.valueOf(theFrame.secondPassword.getPassword());
            if(passWord.equals(aPassWord)&&!"".equals(passWord)&&!"".equals(account)){
                try {
                    handler=new ClientManage();
                    //创建自定义后台处理对象，首先先判断是否与服务器连接，在服务器连接的情况下才进行信息传输
                    if(!(handler.IsConnect())){
                        JOptionPane.showMessageDialog(null, "连接服务器失败");
                        return;
                    }
                    String status;
                    User myUser = new User(account,passWord);
                    myUser.setType(UserRegister);
                    status=handler.Register(myUser);
                    //向服务器发送信息监测账号是否存在
                    if(Register_Fail_alreadyExist.equals(status)){
                        JOptionPane.showMessageDialog(null, "用户名已存在，您可以改一个吗~");
                        return;
                    }
                    //成功注册
                    if(Register_Success.equals(status)){
                        JOptionPane.showMessageDialog(null, "恭喜宁，注册成功！");
                        theFrame.dispose();//关闭窗口
                        return ;
                    }
                    //未知错误
                    if(status==null){
                        JOptionPane.showMessageDialog(null, "服务器端出现未知错误，请联系管理员Debug");
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{
                //注册本地逻辑判断，没问题才连接服务器
                if("".equals(account)){
                    JOptionPane.showMessageDialog(null,"账号不能为空");
                    return;
                }
                else if("".equals(passWord)){
                    JOptionPane.showMessageDialog(null,"密码不能为空");
                    return;
                }
                else{
                    JOptionPane.showMessageDialog(null, "两次密码输入不一致，请重新输入！");
                    theFrame.secondPassword.setText("");
                }
            }
        }
        // 清空按钮
        if(e.getSource()==theFrame.clear){
            theFrame.name.setText("");
            theFrame.firstPassword.setText("");
            theFrame.secondPassword.setText("");
        }
        if(e.getSource()==theFrame.exit){
            {
                //关键步骤退出，防止误触，反馈提示

                int i = JOptionPane.showConfirmDialog(null,"你确定退出注册吗？", "确认", JOptionPane.YES_NO_OPTION);
                if(i == JOptionPane.YES_OPTION) {
                    theFrame.dispose();
                }
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //重写回车按钮事件
        int value = e.getKeyCode();
        if(value==KeyEvent.VK_ENTER){
            account=theFrame.name.getText();
            passWord= String.valueOf(theFrame.firstPassword.getPassword());
            aPassWord=String.valueOf(theFrame.secondPassword.getPassword());
            handler=new ClientManage();
            if(passWord.equals(aPassWord)&&!"".equals(passWord)&&!"".equals(account)){
                try {
                    // 创建自定义后台处理对象，首先先判断是否与服务器连接，在服务器连接的情况下才进行信息传输
                    if(!(handler.IsConnect())){
                        JOptionPane.showMessageDialog(null, "连接服务器失败");
                        return;
                    }
                    String status;
                    User myUser = new User(account,passWord);
                    myUser.setType(UserRegister);
                    status=handler.Register(myUser);
                    //向服务器发送信息监测账号是否存在
                    if(Register_Fail_alreadyExist.equals(status)){
                        JOptionPane.showMessageDialog(null, "用户名已存在，您可以改一个吗~");
                        return;
                    }
                    //成功注册
                    if(Register_Success.equals(status)){
                        JOptionPane.showMessageDialog(null, "恭喜宁，注册成功！");
                        theFrame.dispose();//关闭窗口
                        return ;
                    }
                    //未知错误
                    if(status==null){
                        JOptionPane.showMessageDialog(null, "服务器端出现未知错误，请联系管理员Debug");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else{
                // 注册本地逻辑判断，没问题才连接服务器
                if("".equals(account)){
                    JOptionPane.showMessageDialog(null,"账号不能为空");
                }
                else if("".equals(passWord)){
                    JOptionPane.showMessageDialog(null,"密码不能为空");
                }
                else{
                    JOptionPane.showMessageDialog(null, "两次密码输入不一致，请重新输入！");
                    theFrame.secondPassword.setText("");
                }
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
