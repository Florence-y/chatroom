package toolOfClient;
import viewOfClient.emojiFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Florence
 * 跟文件相关操作的类
 */
public class FileHandler {
    private static final String soucrcedir = "Client/src/TempFile/";
    private static File file;
    boolean hasFile;
    BufferedWriter out;
    BufferedReader in;
    String path;
    String deliverString;
    JTextPane historyShowPane;
    StyledDocument doc;
    SimpleAttributeSet fontProperty = new SimpleAttributeSet();
    /**
     * 设置路径
     *
     * @param fileName 文件名
     */
    public void setPath(String fileName) {
        path = soucrcedir + fileName;
        file = new File(path);
    }

    /**
     * 输出好友的名字
     * @param friendName 好友名字
     */
    public void outputFriends(String friendName) {
        try {
            //如果文件不存在
            if (!file.exists()) {
                hasFile = file.createNewFile();
                System.out.println("新创建文件");
            }
            FileWriter outFile = new FileWriter(file, true);
            out = new BufferedWriter(outFile);
            out.write(friendName + " ");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 读入好友类
     *
     * @return 返回好友列表
     */
    public String[] readInFriends() {
        try {
            if (!file.exists()) {
                hasFile = file.createNewFile();
                System.out.println("新创建文件");
            }
            FileReader inFile = new FileReader(file);
            in = new BufferedReader(inFile);
            deliverString = in.readLine();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
        //如果没有数据
        if (null == deliverString) {
            return null;
        }
        return deliverString.split(" ");
    }

    /**
     * 删除一个好友
     *
     * @param friendName 好友名字
     */
    public void deleteFriends(String friendName) {
        String[] myFriends;
        if (!file.exists()) {
            return;
        }
        //先读取文件内容，再删除文件
        myFriends = readInFriends();
        file.delete();
        for (String friend : myFriends) {
            if (!friend.equals(friendName)) {
                outputFriends(friend);
            }
        }
    }

    /**
     * 输出群聊信息
     * @param myName 我的名字
     * @param frameName 群聊名字
     * @param friendsName 好友们的名字
     */
    public void outPutGroupChatName(String myName,String frameName,String friendsName){
        path=soucrcedir+"ClientOfGroupChat/"+myName+".txt";
        file=new File(path);
        try {
            if (!file.exists()) {
                hasFile = file.createNewFile();
                System.out.println("新创建文件");
            }
            FileWriter outFile = new FileWriter(file, true);
            out = new BufferedWriter(outFile);
            out.write(frameName+"\n");
            out.write(friendsName+"\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取群聊信息
     * @param myName 我的名字
     * @return 群聊信息
     */
    public List<String> readInGroupChatName(String myName){
        List<String> groupChatName=new ArrayList<>();
        path=soucrcedir+"ClientOfGroupChat/"+myName+".txt";
        file=new File(path);
        try {
            if (!file.exists()) {
                hasFile = file.createNewFile();
                System.out.println("新创建文件");
                }
            FileReader inFile = new FileReader(file);
            in = new BufferedReader(inFile);
            while((deliverString=in.readLine())!=null){
                groupChatName.add(deliverString);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return groupChatName;
    }

    /**
     *
     * @param history 输入的信息
     * @param mark  @TEXT*:文本  @PICTURE*: 图片  @FILE*:文件 @EMOTION*:表情
     * @param friendNameOfGroupChatName 聊天记录文件名
     */
    public void outPutHistory(String clientName,String friendNameOfGroupChatName,String history, String mark) {
        //看看用户目录是否存在（每一个用户有一个跟自己好友的聊天记录）
        File tempFile = new File(soucrcedir+"ClientOfHistory/"+clientName);
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }
        path = soucrcedir + "ClientOfHistory/"+clientName+"/"+friendNameOfGroupChatName+".txt";
        file = new File(path);
        try {
            if (!file.exists()) {
                hasFile = file.createNewFile();
                System.out.println("新创建文件");
                if (hasFile) {
                    System.out.println("文件不存在，创建一个(输出流)");
                }
                FileWriter outFile = new FileWriter(file, true);
                out = new BufferedWriter(outFile);
            } else {
                FileWriter outFile = new FileWriter(file, true);
                out = new BufferedWriter(outFile);
            }
            //写入标记，切记换行
            out.write("@"+mark+"*" + "\r\n");
            //写入具体内容
            out.write(history);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *用来展示聊天记录
     * @param historyFileName 聊天记录存储文件
     * @param showPane 插入聊天记录的面板
     */
    public void inPutHistory(String clientName,String historyFileName, JTextPane showPane) {
        //看看用户目录是否存在（每一个用户有一个跟自己好友的聊天记录）
        File tempFile = new File(soucrcedir+"ClientOfHistory/"+clientName);
        if(!tempFile.exists()){
            tempFile.mkdirs();
            return;
        }
        historyShowPane = showPane;
        path = soucrcedir + "ClientOfHistory/"+clientName+"/"+historyFileName+".txt";
        file = new File(path);
        //文件未存在说明没有记录，直接返回即可
        if (!file.exists()) {
            try {
                hasFile = file.createNewFile();
                System.out.println("新创建文件");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileReader inFile = new FileReader(file);
            in = new BufferedReader(inFile);
            doc = showPane.getStyledDocument();
            while((deliverString=in.readLine())!=null){
                //文字
                if("@TEXT*".equals(deliverString)){
                    //时间
                    insert(in.readLine()+"\n");
                    //名字与消息
                    insert(in.readLine());
                }
                //表情
                else if("@EMOTION*".equals(deliverString)){
                    //时间
                    insert(in.readLine()+"\n");
                    //名字
                    insert(in.readLine());
                    //表情
                    showPane.setCaretPosition(doc.getLength());
                    showPane.insertIcon(new ImageIcon(emojiFrame.class.getResource(in.readLine())));
                }//图片
                else if("@PICTURE*".equals(deliverString)){
                    //时间
                    insert(in.readLine()+"\n");
                    //名字与消息
                    insert(in.readLine()+"\n");
                    //图片
                    showPane.setCaretPosition(doc.getLength());
                    showPane.insertIcon(resizePicture(in.readLine()));
                    //文件名字
                    insert("\n"+in.readLine());
                }
                //文件
                else if("@FILE*".equals(deliverString)){
                    //时间
                    insert(in.readLine()+"\n");
                    //名字与消息
                    insert(in.readLine()+"\n");
                    //图片
                    showPane.setCaretPosition(doc.getLength());
                    showPane.insertIcon(new ImageIcon("Client/src/images/文件图片.png"));
                    //文件名字
                    insert("\n"+in.readLine());
                }
                //每次输入一次空一行
                insert("\n\n");
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 插入文字的方法
     * @param string  要插入的文字
     */
    public void insert(String string){
        try {
            doc.insertString(doc.getLength(),string,fontProperty);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新改变图片大小
     * @param filePath 图片路径
     * @return 图片
     */
    private ImageIcon resizePicture(String filePath){
        Image image = null;
        try {
            //判断是否为图片
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.out.println("不是图片");
        }
        return new ImageIcon(getScaledImage(image, 250, 180));
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
        /*public static void main(String[] args) {
        FileHandler a =new FileHandler();
        a.setPath("aa.txt");
        a.outputFriends("aaaa");
        a.outputFriends("bbbb");
        a.deleteFriends("aaaa");
    }*/
}

