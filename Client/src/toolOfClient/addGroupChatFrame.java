package toolOfClient;

import Run.Client;
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


/**
 * @author Florence
 * 添加群聊界面
 */
public class addGroupChatFrame extends JFrame implements ActionListener {
    public static String[] wantToAddUserNames;
    public JTextField input = new JTextField(14);
    public JTextField inputOfFriends = new JTextField(20);
    JLabel reminder = new JLabel("请输入群聊名字：");
    JLabel reminderOfAdd = new JLabel("请输入要群聊的好友名字(空格隔开！！！)：");
    JButton sure = new JButton("确认");
    ClientManage handler = ClientControlCollection.myManage;
    ClientFrame myFrame;

    public addGroupChatFrame(ClientFrame frame) {
        //下面为普通全局设置，可以忽略
        myFrame = frame;
        reminder.setIcon(new ImageIcon(this.getClass().getResource("/images/群聊名字.png")));
        reminderOfAdd.setIcon(new ImageIcon(this.getClass().getResource("/images/人.png")));
        input.setPreferredSize(new Dimension(200, 35));
        inputOfFriends.setPreferredSize(new Dimension(250,35));
        reminder.setPreferredSize(new Dimension(200, 70));
        //设置边框
        Border border = BorderFactory.createDashedBorder(Color.CYAN, 15f, 10);
        Border myBorder = BorderFactory.createTitledBorder(border);
        input.setBorder(myBorder);
        inputOfFriends.setBorder(myBorder);
        add(reminder);
        add(input);
        add(reminderOfAdd);
        add(inputOfFriends);
        add(sure);
        sure.addActionListener(this);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(sure);
        this.setPreferredSize(new Dimension(320, 300));
        this.pack();
        this.setResizable(false);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Images/聊天.png")));
    }
/*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        new addGroupChatFrame(null);
    }
    /*------------------------------------------------------------------------------*/

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sure) {
            sure.setEnabled(false);
            String groupChatName=input.getText();
            StringBuilder friends= new StringBuilder();
            //运用正则表达式去除以多个间隔为分隔符的情况
            String[] tempFriendsName=inputOfFriends.getText().split("\\s+");
            for (String name:tempFriendsName) {
                friends.append(name).append(" ");
            }
            //获取想要群聊的名字，并验证是否存在
            wantToAddUserNames = friends.toString().trim().split(" ");
            if("".equals(groupChatName)){
                JOptionPane.showMessageDialog(null,"请输入群聊名字！");
                sure.setEnabled(true);

                return;
            }
            //不能为空
            if("".equals(friends.toString())){
                JOptionPane.showMessageDialog(null,"请输入要聊天的好友名！");
                sure.setEnabled(true);

                return;
            }
            //已经存在
            if(ClientControlCollection.cardOfGroupChat.containsKey(groupChatName)){
                JOptionPane.showMessageDialog(null,"该群聊名字已经存在，请重新输入！");
                sure.setEnabled(true);
                return;
            }
            //获取是否好友都存在
            for (String friendName:wantToAddUserNames){
                if(ClientControlCollection.getFriendCard(friendName)==null){
                    JOptionPane.showMessageDialog(null,"输入的名字中有的不是您的好友！");
                    sure.setEnabled(true);
                    return;
                }
            }
            //添加到客户端显示页面
            GroupChatLable lable=new GroupChatLable(friends+" "+myFrame.myName,groupChatName);
            ClientControlCollection.cardOfGroupChat.put(groupChatName,lable);
            //输出群聊信息（再次登录有这个群聊）
            ClientControlCollection.fileHandler.outPutGroupChatName(myFrame.myName,groupChatName, friends.toString().trim()+" "+myFrame.myName);
            //添加面板
            myFrame.groupChat.add(lable.friendCard);
            //更新面板
            myFrame.updatePanel();
            JOptionPane.showMessageDialog(null,"添加群聊"+input.getText()+"成功！");
            inputOfFriends.setText("");
            input.setText("");
            sure.setEnabled(true);
            dispose();
        }
    }
}
