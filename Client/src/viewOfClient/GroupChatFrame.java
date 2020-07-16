package viewOfClient;

import controlerOfClient.ChatListener;
import controlerOfClient.GroupChatListener;
import messageHandlerOfClient.ClientControlCollection;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Florence
 * 群聊界面
 */
public class GroupChatFrame extends JFrame implements WindowListener {
    /**
     * 控件的定义
     */
    public JButton sent = new JButton();
    public JButton file = new JButton();
    public JButton emoji = new JButton();
    public JButton showHistory = new JButton();
    public JTextPane chat = new JTextPane();
    public JTextField inputWindows = new JTextField(20);
    public JScrollPane chatPane = new JScrollPane(chat);
    public StyledDocument doc;
    public String myFriendNames;
    public String myName;
    public String chatFrameName;
    String[] nameFont = { "宋体", "黑体", "Dialog", "微软雅黑"};
    String[] sizeFont = { "12", "16", "20", "24", "28", "32","66" };
    public JComboBox fontName = new JComboBox(nameFont);
    public JComboBox fontSize = new JComboBox(sizeFont);
    Box container= Box.createVerticalBox();
    Icon mark1 = new ImageIcon(ChatFrame.class.getResource("/images/表情.png"));
    Icon mark2 = new ImageIcon(ChatFrame.class.getResource("/images/文件.png"));
    Icon mark3 = new ImageIcon(ChatFrame.class.getResource("/images/聊天记录.png"));
    GroupChatListener  listener = new GroupChatListener(this) ;
    public GroupChatFrame(String name,String friendNames,String frameName){
        myName=name;
        myFriendNames=friendNames;
        chatFrameName=frameName;
        super.setTitle(frameName+"  (我: "+name+")");
        /**
         * 按钮设置
         */
        sent.setPreferredSize(null);
        sent.setText("发送");
        file.setPreferredSize(new Dimension(48,48));
        emoji.setPreferredSize(new Dimension(48,48));
        showHistory.setPreferredSize(new Dimension(48,48));
        fontName.setPreferredSize(new Dimension(48,48));
        emoji.setIcon(mark1);
        file.setIcon(mark2);
        showHistory.setIcon(mark3);
        fontSize.setSelectedIndex(2);
        fontName.setSelectedIndex(3);
        /**
         * 面板设置
         */
        chat.setEditable(false);
        chat.setFocusable(false);

        inputWindows.requestFocus();
        inputWindows.setFont(new Font("微软雅黑",Font.PLAIN,15));
        inputWindows.setPreferredSize(new Dimension(50,50));
        doc = chat.getStyledDocument();
        chatPane.setPreferredSize(new Dimension(500,600));
        /**
         * 控件添加
         */
        Box firstLine = Box.createHorizontalBox();
        Box secondLine = Box.createHorizontalBox();
        inputWindows.requestFocus(true);
        container.add(firstLine);
        container.add(Box.createVerticalStrut(8));
        container.add(secondLine);
        container.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        firstLine.add(emoji);
        firstLine.add(Box.createHorizontalStrut(5));
        firstLine.add(file);
        firstLine.add(Box.createHorizontalStrut(5));
        firstLine.add(fontName);
        firstLine.add(Box.createHorizontalStrut(5));
        firstLine.add(fontSize);
        firstLine.add(Box.createHorizontalStrut(5));
        firstLine.add(showHistory);
        firstLine.add(Box.createHorizontalStrut(1000));
        secondLine.add(inputWindows);
        secondLine.add(Box.createHorizontalStrut(30));
        secondLine.add(Box.createVerticalStrut(30));
        secondLine.add(sent);
        /**
         * 全局设置
         */
        setListener();
        this.addWindowListener(this);
        this.getContentPane().add(chatPane);
        this.setSize(710,700);
        this.getRootPane().setDefaultButton(sent);
        this.getContentPane().add(container, BorderLayout.SOUTH);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(JFrame.class.getResource("/images/人.PNG")));
        this.setLocation(500,150);
        this.setVisible(true);
        inputWindows.requestFocus();
    }

    private void setListener() {
        sent.addActionListener(listener);
        file.addActionListener(listener);
        emoji.addActionListener(listener);
        showHistory.addActionListener(listener);
    }
/*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        new GroupChatFrame("aaa","bbbb","ccc");
    }
/*------------------------------------------------------------------------------*/

    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * 关闭时移除聊天窗口，防止再次打开出现异常
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        ClientControlCollection.windowOfGroupChat.remove(chatFrameName);
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
