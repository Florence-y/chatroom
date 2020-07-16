package toolOfClient;
import dataPackageClass.Message;
import dataPackageClass.User;
import messageHandlerOfClient.ClientControlCollection;
import messageHandlerOfClient.ClientManage;
import viewOfClient.ClientFrame;
import viewOfClient.LoginFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static dataPackageClass.MessageTypeInterface.*;

/**
 * @author Florence
 * 添加好友的弹窗
 */
public class addFriendFrame  extends JFrame implements ActionListener {
    public static String wantToAddUserName;
    public JTextField input = new JTextField(14);
    JLabel reminder = new JLabel("请输入要添加好友名字：");
    JButton sure = new JButton("确认");
    ClientManage handler= ClientControlCollection.myManage;
    /**
     * 用户总界面
     */
    ClientFrame myFrame;
    public addFriendFrame(ClientFrame frame){
        myFrame=frame;
        reminder.setIcon(new ImageIcon(this.getClass().getResource("/images/人.png")));
        input.setPreferredSize(new Dimension(200,35));
        reminder.setPreferredSize(new Dimension(200,70));
        //设置边框
        Border border = BorderFactory.createDashedBorder(Color.CYAN, 15f, 10);
        Border myBorder = BorderFactory.createTitledBorder(border);
        input.setBorder(myBorder);
        add(reminder);
        add(input);
        add(sure);
        sure.addActionListener(this);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(sure);
        this.setPreferredSize(new Dimension(250,200));
        this.pack();
        this.setResizable(false);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Images/聊天.png")));
    }
/*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
            new addFriendFrame(new ClientFrame("aaa"));
    }
/*------------------------------------------------------------------------------*/

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sure){
            //获取名字并验证是否存在该用户，如果存在那又是否在线？
            sure.setEnabled(false);
            wantToAddUserName=input.getText();
            //添加名字为空
            if("".equals(wantToAddUserName)){
                JOptionPane.showMessageDialog(null,"请输入好友名字");
                sure.setEnabled(true);

                return;
            }
            //防止添加自己
            if(wantToAddUserName.equals(myFrame.myName)){
                JOptionPane.showMessageDialog(null,"请别添加你自己！");
                sure.setEnabled(true);
                return;
            }
            //已经是好友
            if(ClientControlCollection.getFriendCard(wantToAddUserName)!=null){
                JOptionPane.showMessageDialog(null,wantToAddUserName+"已经是你的好友");
                sure.setEnabled(true);
                return;
            }
            //是否连接服务器
            if(!ClientControlCollection.myManage.IsConnect()){
                JOptionPane.showMessageDialog(null,"未连接服务器");
                sure.setEnabled(true);
                return;
            }
            Message mess=new Message();
            mess.setMessageType(Make_SURE_USER_ISEXIST);
            User user = new User();
            user.setName(wantToAddUserName);
            //为消息包添加用户对象
            mess.setUser(user);
            //调用方法判断是否登录！！！！！！！！！！！！！！！！
            boolean isExistUser = handler.isUserExist(mess);
            sure.setEnabled(true);










            //-------------------------------------------------------“修改分割线，后续可能要用”-------------------------------------------------------------------------------------------------
            /*if(isExistUser==true){
                JOptionPane.showMessageDialog(null, "添加好友成功");
                //获取好友的在线情况
                mess.setMessageType(Make_SURE_USER_ISONLINE);
                boolean isOnline=handler.isOnline(mess);
                //好友列表设置格式为用户名+ofFriends
                ass.setPath(myFrame.myName+"ofFriends.txt");
                //将好友存起来
                ass.outputFriends(wantToAddUserName);
                FriendLable lable=new FriendLable(wantToAddUserName, isOnline);
                //加入控件集合
                ClientControlCollection.addFriendCard(wantToAddUserName,lable);
                //加入面板
                myFrame.singleChat.add(lable.friendCard);
                lable.changeStatus(isOnline);
                this.dispose();
            }*/
            /*else{
                JOptionPane.showMessageDialog(null, "该用户不存在");
            }*/
            //-----------------------------------------------------“结束代码”----------------------------------------------------------------------
        }
    }
}
