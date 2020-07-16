package toolOfClient;
import messageHandlerOfClient.ClientControlCollection;
import viewOfClient.ChatFrame;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * @author Florence
 * 好友列表类
 */
public class FriendLable implements MouseListener {
    private static final int CLICK_DOUBLE=2;
    private String friendName;
    private JLabel headPicture;
    private JLabel onlineStatusShow;
    private boolean onlineStatus;
    public Box friendCard = Box.createHorizontalBox();
    public FriendLable(String name, Boolean status){
        //一下为常规初始化界面，可以忽略，另外因为监听器内容较少，就以自身实现接口作为监听器
        friendName=name;
        onlineStatus =status;
        headPicture=new JLabel();
        headPicture.setText(friendName);
        headPicture.setIcon(new ImageIcon(this.getClass().getResource("/images/人.png")));
        onlineStatusShow = new JLabel();
        onlineStatusShow.setIcon(new ImageIcon(this.getClass().getResource("/images/在线.png")));
        friendCard.setPreferredSize(new Dimension(300,50));
        friendCard.add(headPicture);
        friendCard.add(Box.createHorizontalStrut(180));
        Border border=BorderFactory.createEtchedBorder();
        friendCard.setBorder(border);
        friendCard.add(onlineStatusShow);
        friendCard.addMouseListener(this);
        //根据是否在线设置在线标志
        if(onlineStatus) {
            onlineStatusShow.setVisible(true);
        }
        else{
            onlineStatusShow.setVisible(false);
        }
    }
    public void changeStatus(Boolean status){
        //状态已经相同不用更改
        if(status==onlineStatus){
            return;
        }
        if(status){
            onlineStatusShow.setVisible(true);
        }
        else{
            onlineStatusShow.setVisible(false);
        }
        onlineStatus=status;
        friendCard.updateUI();
    }
    /**
     * 实现双击的方法
     * @param e 事件
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        //双击发送信息并且与服务器相连
        if(e.getClickCount()==CLICK_DOUBLE&& ClientControlCollection.myManage.IsConnect()) {
            if(!ClientControlCollection.windowOfFriends.containsKey(friendName)) {
                ClientControlCollection .addChatFrame(friendName,new ChatFrame(friendName,ClientControlCollection.myFrame.myName));
            }
            if(!onlineStatus){
                JOptionPane.showMessageDialog(null,"用户不在线，快去call他上来撩骚！");
            }
        }
        if(!ClientControlCollection.myManage.IsConnect()){
            JOptionPane.showMessageDialog(null,"未连接服务器");
        }
    }

    /**
     * 不用实现的方法
     * @param e 事件
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //测试用例
    /*------------------------------------------------------------------------------------------------/
    public static void main(String[] args) {
        //test
        JFrame text = new JFrame();
        text.setLayout(new FlowLayout(FlowLayout.CENTER));
        FriendLable c=new FriendLable("李四",true);
        text.add(c.friendCard);
        text.remove(c.friendCard);
        text.add(new FriendLable("sss",false).friendCard);






        text.pack();
        text.setVisible(true);
        text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }/------------------------------------------------------------------------*/
}
