package controlerOfClient;
import Run.Client;
import dataPackageClass.Message;
import messageHandlerOfClient.ClientControlCollection;
import messageHandlerOfClient.ClientManage;
import viewOfClient.ChatFrame;
import viewOfClient.ShowChatHistory;
import viewOfClient.emojiFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


import static dataPackageClass.MessageTypeInterface.*;

/**
 * @author Florence
 * 单聊监听器
 */
public class ChatListener implements ActionListener {
    private ChatFrame myFrame;
    private ClientManage handler;
    /**
     * 文件选择器
     */
    private JFileChooser fileChooser;
    /**
     * 时间类型
     */
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    public ChatListener(ChatFrame frame) {
        myFrame=frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //发送
        if(e.getSource()==myFrame.sent){
            //当文本框没有内容的时候，拒绝输入
            if(myFrame.inputWindows.getText().length()==0) {
                return;
            }
            //插入消息（自定义方法）
            insertStringInToShow();
            //发送消息(因为actionListener的方法是在EDT线程里处理的，而下面这段代码比较耗时（其实文字信息不会~），会造成堵塞，所以我们应该放在另外的线程里处理)
            ClientControlCollection.threadPool.execute(() -> {
                Message message = new Message();
                message.setSender(myFrame.myName);
                message.setTime(getTime());
                message.setContent(myFrame.inputWindows.getText());
                message.setGetter(myFrame.myFriendName);
                message.setMessageType(Common_Message_ToPerson);
                message.setMessageTypeDetail("TEXT");
                message.setAttributeSet(getAttributeSet());
                myFrame.inputWindows.setText("");
                ClientControlCollection.myManage.sendMessage(message);
            });
        }
        //发送文件
        if(e.getSource()==myFrame.file){
            fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(myFrame);
            //获取文件对象
            File selectedFile= fileChooser.getSelectedFile();
            //选中文件才执行方法
            if(selectedFile==null){
                return;
            }
            String type=insertFileToShow(selectedFile);
            /*发送消息(因为actionListener的方法是在EDT线程里处理的，
            而下面这段代码比较耗时（其实文字信息不会~，但图片会！！！！，被这个BUG卡了很久），
            会造成堵塞，导致画面假死（点什么按钮都没用，甚至强制关闭也没用）
            所以我们应该放在另外的线程里处理
             */
            ClientControlCollection.threadPool.execute(() -> {
                Message message = new Message();
                message.setSender(myFrame.myName);
                message.setTime(getTime());
                message.setGetter(myFrame.myFriendName);
                message.setMessageType(Send_FileToPerson);
                message.setMessageTypeDetail(type);
                message.setAttributeSet(getAttributeSet());
                message.setContent(selectedFile.getName());
                ClientControlCollection.myManage.sendMessage(message);
                ClientControlCollection.myManage.sendFile(selectedFile.getPath());
            });
        }
        //发送表情
        if(e.getSource()==myFrame.emoji){
            new emojiFrame(myFrame);
        }
        //历史记录
        if(e.getSource()==myFrame.showHistory){
            new ShowChatHistory(myFrame.showHistory,myFrame.myName,myFrame.myFriendName);
        }
    }

   private String insertFileToShow(File selectedFile) {
        //获取路径
        String filePath = selectedFile.getPath();
       Image image = null;
       try {
           //判断是否为图片
           image = ImageIO.read(selectedFile);
       } catch (IOException e) {
           System.out.println("不是图片");
       }
        //插入时间
       insert(getTime()+"\n",1);
       //为图片
       if(null != image){
            insert(myFrame.myName+"：图片\n");
            myFrame.chat.setCaretPosition(myFrame.doc.getLength());
            myFrame.chat.insertIcon(new ImageIcon(getScaledImage(image,myFrame.getWidth() /3,myFrame.getHeight()/3)));
            insert("\n");
           //插入文件名字
           insert(selectedFile.getName());
            //留空行
            insert("\n\n");
            myFrame.chat.select(myFrame.doc.getLength(), myFrame.doc.getLength());
            //输出聊天记录
            ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.myFriendName,getTime()+"\n"
                    +myFrame.myName+"：图片\n"+filePath+"\n"+selectedFile.getName()+"\n","PICTURE");
            return "PICTURE";
        }
        //为文件
        else{
            insert(myFrame.myName+"：文件\n");
            myFrame.chat.insertIcon(new ImageIcon("Client/src/images/文件图片.png"));
            insert("\n");
           //插入文件名字
           insert(selectedFile.getName());
            //留空行
            insert("\n\n");
            myFrame.chat.select(myFrame.doc.getLength(), myFrame.doc.getLength());
            //输出聊天记录
            ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.myFriendName,getTime()+"\n"+myFrame.myName
                    +": 文件\n"+selectedFile.getName()+"\n","FILE");
            return "FILE";
        }
    }

    /**
     * 获取时间与名字
     * @return 时间与名字
     */
    public String getTimeAndName(){
        return "时间："+formatter.format(Calendar.getInstance().getTime())+ "\n"+myFrame.myName+":";
    }

    /**
     * 获取时间
     * @return 时间
     */
    public String getTime(){
        return "时间："+formatter.format(Calendar.getInstance().getTime());
    }

    /**
     * 插入文字
     * @param string 要插入的文字
     */
     public void insert(String string){
        try {
            myFrame.doc.insertString(myFrame.doc.getLength(),string,getAttributeSet());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可调节对齐版本
     * @param string 要插入的文字
     */
    public void insert(String string,int align){
        try {
            myFrame.doc.insertString(myFrame.doc.getLength(),string,getAttributeSet(align));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param srcImg 插入的图片
     * @param w 宽
     * @param h 高
     * @return 修改完的图片
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    public SimpleAttributeSet getAttributeSet(){
        //获取文字类型
        String fontFamily=(String)myFrame.fontName.getSelectedItem();
        //获取文字大小
        int size = Integer.parseInt((String) Objects.requireNonNull(myFrame.fontSize.getSelectedItem()));
        SimpleAttributeSet fontProperty = new SimpleAttributeSet();
        //设置默认为微软雅黑
        if(fontFamily!=null){
            StyleConstants.setFontFamily(fontProperty,fontFamily);
        }
        else{
            StyleConstants.setFontFamily(fontProperty,"微软雅黑");
        }
        StyleConstants.setFontSize(fontProperty,size);
        return fontProperty;
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
     * 插入文字信息
     */
    public void insertStringInToShow(){
        insert(getTime()+"\n",1);
        insert(myFrame.myName+"： "+myFrame.inputWindows.getText()+"\n\n");
        //输出聊天记录
        ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.myFriendName,getTime()+"\n"
                +myFrame.myName+"： "+myFrame.inputWindows.getText()+"\n","TEXT");
    }
}
