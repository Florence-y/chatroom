package viewOfClient;
import controlerOfClient.ChatListener;
import messageHandlerOfClient.ClientControlCollection;
import toolOfClient.BeautyTool;
import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Florence
 */
public class ChatFrame extends JFrame implements WindowListener {
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
    public String myFriendName;
    public String myName;
    String[] nameFont = { "宋体", "黑体", "Dialog", "微软雅黑"};
    String[] sizeFont = { "12", "16", "20", "24", "28", "32","66" };
    public JComboBox fontName = new JComboBox(nameFont);
    public JComboBox fontSize = new JComboBox(sizeFont);
    Box container= Box.createVerticalBox();
    Icon mark1 = new ImageIcon(ChatFrame.class.getResource("/images/表情.png"));
    Icon mark2 = new ImageIcon(ChatFrame.class.getResource("/images/文件.png"));
    Icon mark3 = new ImageIcon(ChatFrame.class.getResource("/images/聊天记录.png"));
    ChatListener listener = new ChatListener(this);
    public ChatFrame(String friendName,String name){
        super.setTitle("我："+"("+name+")"+"发送给："+friendName);
        myFriendName=friendName;
        myName=name;
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
        //添加监听器
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
    public void setListener(){
        sent.addActionListener(listener);
        file.addActionListener(listener);
        emoji.addActionListener(listener);
        showHistory.addActionListener(listener);
    }
    /*------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        //test
        BeautyTool.makeBeauty();
        new ChatFrame("aaa"," 是是是");
        System.out.println("我是谁");
    }
    /*------------------------------------------------------------------------------------------------------------*/

    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * 关闭时退出移除窗口（防止再次打开出现错误）
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        ClientControlCollection.windowOfFriends.remove(myFriendName);
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
