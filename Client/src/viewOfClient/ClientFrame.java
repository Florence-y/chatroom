package viewOfClient;
import controlerOfClient.ClientListener;
import java.util.List;
import dataPackageClass.Message;
import messageHandlerOfClient.ClientControlCollection;
import messageHandlerOfClient.ClientManage;
import toolOfClient.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Florence
 * 用户好友群聊界面
 */
public class ClientFrame extends JFrame implements WindowListener, ActionListener {
    public JButton addFriends;
    public JButton removeFriends;
    public JButton addGroupChat;
    public JPanel singleChat;
    public JPanel groupChat;
    public JLabel myCard;
    public String myName;
    ClientListener listener;
    FileHandler fileHandler;
    JTabbedPane singleChatOrGroupChat;
    Box container;
    Box firstLine,secondLine;
    public ClientFrame(String userName){
        super(userName);
        myName=userName;
        listener=new ClientListener(this);
        fileHandler=new FileHandler();
        /**
         *一些控件图片的添加以及配置的设置
         */
        myCard= new JLabel();
        myCard.setOpaque(true);
        myCard.setIcon(new ImageIcon(ClientFrame.class.getResource("/images/用户.png")));
        myCard.setText(userName);
        myCard.setPreferredSize(new Dimension(50,50));
        addFriends=new JButton();
        removeFriends=new JButton();
        addGroupChat=new JButton();
        addFriends.setIcon(new ImageIcon(ClientFrame.class.getResource("/images/添加用户.png")));
        removeFriends.setIcon(new ImageIcon(ClientFrame.class.getResource("/images/删除用户.png")));
        addGroupChat.setIcon(new ImageIcon(ClientFrame.class.getResource("/images/多人在线聊.png")));
        /**
         *添加的具体的容器里并为容器设置
         */
        firstLine= Box.createHorizontalBox();
        firstLine.add(myCard);
        firstLine.setBorder(BorderFactory.createEmptyBorder(12, 12, 12,12));
        secondLine=Box.createHorizontalBox();
        secondLine.add(Box.createHorizontalStrut(130));
        secondLine.add(addFriends);
        secondLine.add(Box.createHorizontalStrut(10));
        secondLine.add(removeFriends);
        secondLine.add(Box.createHorizontalStrut(10));
        secondLine.add(addGroupChat);
        secondLine.setBorder(BorderFactory.createEmptyBorder(12, 12, 12,12));
        container = Box.createVerticalBox();
        container.add(firstLine);
        container.add(Box.createVerticalStrut(10));
        container.add(secondLine);
        /**
         * 可切换的单人聊天用户与多人聊天群
          */
        singleChat=new JPanel();
        singleChat.setPreferredSize(new Dimension(350,1000));
        JScrollPane scrollPaneOfSingle=new JScrollPane(singleChat);
        singleChat.setLayout(new FlowLayout(FlowLayout.LEFT));
        groupChat=new JPanel();
        groupChat.setPreferredSize(new Dimension(350,1000));
        JScrollPane scrollPaneOfGroup =new JScrollPane(groupChat);
        groupChat.setLayout(new FlowLayout(FlowLayout.LEFT));
        singleChatOrGroupChat= new JTabbedPane();
        singleChatOrGroupChat.addTab("我的好友",new ImageIcon(ClientFrame.class.getResource("/images/单聊.png")),scrollPaneOfSingle);
        singleChatOrGroupChat.addTab("我的群聊",new ImageIcon(ClientFrame.class.getResource("/images/群聊.png")),scrollPaneOfGroup);
        singleChatOrGroupChat.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        singleChatOrGroupChat.setTabPlacement(JTabbedPane.TOP);
        /**
         * 全局设置
         * 头两个方法是初始化好友列表和设置监听器
         */
        initializeAlreadyAddFriendsCard();
        setListener();
        addGroupChat.addActionListener(this);
        this.setPreferredSize(new Dimension(400,700));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Images/聊天.png")));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.add(container,BorderLayout.NORTH);
        this.add(singleChatOrGroupChat);
        this.setVisible(true);
        this.setResizable(false);
        this.addWindowListener(this);
    }

    public void setListener() {
        addFriends.addActionListener(listener);
        removeFriends.addActionListener(listener);
    }

    /**
     * 初始化好友列表方法
     */
    public void initializeAlreadyAddFriendsCard(){
        if(fileHandler!=null){
            //设置好友文件路径
            fileHandler.setPath("ClientOfFriends/"+myName+"OfFriends.txt");
            //获取好友列表
            ClientManage manage=ClientControlCollection.myManage;
            Message friends= manage.reciveMessage();
            String[] friendsCard=fileHandler.readInFriends();
            if(friendsCard!=null) {
                //遍历好友列表，并添加进面板，将好友的名片放进容器中
                for (String friend : friendsCard) {
                    //防止重名
                    if(ClientControlCollection.getFriendCard(friend)!=null){
                        continue;
                    }
                    FriendLable label = new FriendLable(friend, false);
                    ClientControlCollection.addFriendCard(friend, label);
                    singleChat.add(label.friendCard);
                }
            }
            /**
             * 设置好友的在线情况
             */
            if(friends!=null){
                String[]  onlineFriends = friends.getContent().split(" ");
                for (String friend : onlineFriends) {
                    FriendLable card=ClientControlCollection.getFriendCard(friend);
                    if(card!=null){
                        card.changeStatus(true);
                    }
                }
            }
        }
        /**
         * 读取群聊面板
         */
        if(fileHandler!=null){
            List<String> myGroupChat= fileHandler.readInGroupChatName(myName);
            for (int i=0;i<myGroupChat.size();i+=2){
                GroupChatLable lable= new GroupChatLable(myGroupChat.get(i+1),myGroupChat.get(i));
                ClientControlCollection.cardOfGroupChat.put(myGroupChat.get(i),lable);
                groupChat.add(lable.friendCard);
            }
        }
        updatePanel();
    }
    /*------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        BeautyTool.makeBeauty();
        ClientFrame text=new ClientFrame("bbbb");
    }
    /*------------------------------------------------------------------------------------------------------*/



    public  void updatePanel(){
        singleChat.updateUI();
        groupChat.updateUI();
    }
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        int status= JOptionPane.showConfirmDialog(null,"确认退出客户端吗","确认",JOptionPane.YES_NO_OPTION);
        if(status==JOptionPane.YES_OPTION){
            //向服务器发送下线消息
            ClientControlCollection.myManage.sentOffLine(myName);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        new addGroupChatFrame(this);
    }
}
