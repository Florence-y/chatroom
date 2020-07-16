package toolOfClient;

import messageHandlerOfClient.ClientControlCollection;
import viewOfClient.GroupChatFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 群聊面板
 * @author Florence
 */
public class GroupChatLable implements MouseListener {
    private static final int CLICK_DOUBLE=2;
    private String friendNames;
    private String groupChatFrameName;
    private JLabel headPicture;
    public Box friendCard = Box.createHorizontalBox();
    public GroupChatLable(String name,String frameName){
        friendNames=name;
        groupChatFrameName=frameName;
        headPicture=new JLabel(groupChatFrameName);
        headPicture.setIcon(new ImageIcon(this.getClass().getResource("/images/群聊入口.png")));
        friendCard.setPreferredSize(new Dimension(300,50));
        friendCard.add(headPicture);
        Border border=BorderFactory.createEtchedBorder();
        friendCard.setBorder(border);
        friendCard.addMouseListener(this);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //双击事件打开聊天窗口
        if(e.getClickCount()==CLICK_DOUBLE&&!ClientControlCollection.windowOfGroupChat.containsKey(groupChatFrameName)){
            ClientControlCollection.windowOfGroupChat.put(groupChatFrameName,new GroupChatFrame(ClientControlCollection.myFrame.myName,friendNames,groupChatFrameName));
        }
    }

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
}
