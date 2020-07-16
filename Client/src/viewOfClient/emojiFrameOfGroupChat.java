package viewOfClient;

import dataPackageClass.Message;
import messageHandlerOfClient.ClientControlCollection;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import static dataPackageClass.MessageTypeInterface.*;

/**
 * @author Florence
 */
public class emojiFrameOfGroupChat extends JFrame implements ActionListener {
    JPanel pane = new JPanel();
    /**
     * 表情包按钮
     */
    GroupChatFrame myFrame;
    JButton[] memePackage = new JButton[26];
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    public emojiFrameOfGroupChat(GroupChatFrame frame){
        myFrame=frame;
        pane.setPreferredSize(new Dimension(500, 500));
        pane.setLayout(new GridLayout(5, 5));
        //添加表情
        for (int i = 1; i <= 25; i++) {
            memePackage[i] = new JButton(new ImageIcon(this.getClass().getResource("/images/"+i+".png")));
            memePackage[i].setSize(32, 32);
        }
        //设置监听器
        for (int i = 1; i <= 25; i++) {
            memePackage[i].addActionListener(this);
        }
        //将25个表情加入面板
        for (int i = 1; i <= 25; i++) {
            pane.add(memePackage[i]);
        }
        setPreferredSize(new Dimension(500, 500));
        add(pane);
        setVisible(true);
        //先自动调整大小
        pack();
        //然后再进行锁定窗口大小，防止用户误操作
        setResizable(false);
        setLocation(20,300);
        this.add(pane);
    }
    /**
     * 可设置对齐的类型
     * @return 类型集
     */
    public SimpleAttributeSet getAttributeSet(int align){
        //获取文字类型
        String fontFamily=(String)myFrame.fontName.getSelectedItem();
        //获取文字大小
        int size = Integer.parseInt((String) Objects.requireNonNull(myFrame.fontSize.getSelectedItem()));
        SimpleAttributeSet fontProperty = new SimpleAttributeSet();
        if(fontFamily!=null){
            StyleConstants.setFontFamily(fontProperty,fontFamily);
        }
        else{
            StyleConstants.setFontFamily(fontProperty,"微软雅黑");
        }
        StyleConstants.setAlignment(fontProperty,align);
        StyleConstants.setFontSize(fontProperty,size);
        return fontProperty;
    }

    /**
     * 不可对齐的类型
     * @return 类型集
     */
    public SimpleAttributeSet getAttributeSet(){
        //获取文字类型
        String fontFamily=(String)myFrame.fontName.getSelectedItem();
        //获取文字大小
        int size = Integer.parseInt((String) Objects.requireNonNull(myFrame.fontSize.getSelectedItem()));
        SimpleAttributeSet fontProperty = new SimpleAttributeSet();
        if(fontFamily!=null){
            StyleConstants.setFontFamily(fontProperty,fontFamily);
        }
        else{
            StyleConstants.setFontFamily(fontProperty,"微软雅黑");
        }
        StyleConstants.setFontSize(fontProperty,size);
        return fontProperty;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String emojiName=null;
        for (int i = 1; i <= 25; i++) {
            try {
                if (e.getSource() == memePackage[i]) {
                    System.out.println("是表情");
                    //这段代码的作用是显示日期
                    myFrame.doc.insertString(myFrame.doc.getLength(), "时间：" + getTime()+"\n", getAttributeSet(1));
                    myFrame.doc.insertString(myFrame.doc.getLength(),myFrame.myName+"：",getAttributeSet());
                    //获取目前面板内容的长度，以用于插入图片
                    myFrame.chat.setCaretPosition(myFrame.doc.getLength());
                    //插入图片
                    myFrame.chat.insertIcon(new ImageIcon(emojiFrame.class.getResource("/Images/" + i + ".png")));
                    //从第6个开始截取，因为URL文件有FILE：这几个额外的符号
                    myFrame.doc.insertString(myFrame.doc.getLength(),"\n\n",getAttributeSet());
                    emojiName="/Images/" + i + ".png";
                    //输出聊天记录
                    ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.chatFrameName,getTime()+"\n"+myFrame.myName+": \n"
                            +emojiName+"\n","EMOTION");
                    break;
                }
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
        //如果名字不为空，向服务器发送信息
        if(emojiName!=null) {
            String finalEmojiName = emojiName;
            //在另外的线程执行，防止EDT线程堵塞
            ClientControlCollection.threadPool.execute(() -> {
                Message message = new Message();
                message.setMessageType(SEND_EMOTIONToAll);
                message.setSender(myFrame.myName);
                message.setGetter(myFrame.myFriendNames);
                message.setContent(finalEmojiName);
                message.setFriendsName(myFrame.myFriendNames);
                message.setMessageTypeDetail(myFrame.chatFrameName);
                message.setTime(getTime());
                ClientControlCollection.myManage.sendMessage(message);
            });

        }
    }
    /**
     * 获取时间
     * @return 时间
     */
    public String getTime(){
        return formatter.format(Calendar.getInstance().getTime());
    }
}
