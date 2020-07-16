package toolOfClient;
import messageHandlerOfClient.ClientControlCollection;
import messageHandlerOfClient.ClientManage;
import viewOfClient.LoginFrame;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author DELL
 * 删除好友类
 */
public class deleteFriendFrame extends JFrame implements ActionListener {
    public static String wantToDeleteUserName;
    public JTextField input = new JTextField(14);
    JLabel reminder = new JLabel("请输入要删除的好友名字：");
    JButton sure = new JButton("确认");
    public deleteFriendFrame(){
        //下面为全局设置，可以忽略
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sure){
            sure.setEnabled(false);
            wantToDeleteUserName=input.getText();
            int status= JOptionPane.showConfirmDialog(null,"确定删除"+wantToDeleteUserName+"?","确认",JOptionPane.YES_NO_OPTION);
            //不是好友
            if(ClientControlCollection.getFriendCard(wantToDeleteUserName)==null){
                JOptionPane.showMessageDialog(null,wantToDeleteUserName+"不是你的好友");
                sure.setEnabled(true);
                return;
            }
            if(status==JOptionPane.YES_OPTION){
                //文件处理器
                FileHandler handler=ClientControlCollection.fileHandler;
                handler.setPath("ClientOfFriends/" +ClientControlCollection.myFrame.myName + "ofFriends.txt");
                //删除客户端面板
                ClientControlCollection.myFrame.singleChat.remove(ClientControlCollection.getFriendCard(wantToDeleteUserName).friendCard);
                //删除容器按钮
                ClientControlCollection.removeFriendCard(wantToDeleteUserName);
                //删除文件中的名字
                handler.deleteFriends(wantToDeleteUserName);
                //更新面板
                ClientControlCollection.myFrame.updatePanel();
                this.dispose();
            }
            sure.setEnabled(true);
        }
    }
}
