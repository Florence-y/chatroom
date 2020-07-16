package messageHandlerOfClient;
import Run.Client;
import dataPackageClass.Message;
import toolOfClient.FileHandler;
import toolOfClient.FriendLable;
import toolOfClient.GroupChatLable;
import toolOfClient.addFriendFrame;
import viewOfClient.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import static dataPackageClass.MessageTypeInterface.*;
/**
 * @author Florence
 * 消息处理类，所有从服务器接受到的消息都在这里处理！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
 */
public class MessageHandler {
    private static addFriendFrame addFrame;
    /**
     * 处理器接口方法：根据传入对象不同的消息类型，用不同的方法处理（策略模式）不过只是运用了思想，不是用多态实现的~~~~~
     * @param message 消息包
     */
    public static void messageUse(Message message){
            String messageType = message.getMessageType();
            //获取在线信息
            if(messageType.equals(Send_Online)) {
                initializeClientFrame(message.getContent());
            }
            //添加好友相关信息
            else if(messageType.equals(ADD_Friend)) {
                showAddResult(message);
            }
            //用户是否登录存在信息
            else if(messageType.equals(Just_login)){
                updateMyFriendCard(message);
            }
            //用户下线信息
            else if(messageType.equals(Just_OFF_Line)){
                updateMyFriendCard(message);
            }
            //普通单人文字信息
            else if(messageType.equals(Common_Message_ToPerson)){
                showMessageFromMyFriend(message);
            }
            //群聊文字信息
            else if(messageType.equals(Common_Message_ToGroup)){
                showMessageFromGroup(message);
            }
            //文件信息
            else if(messageType.equals(Send_FileToPerson)){
                File file =  ClientControlCollection.myManage.reciveFile(message);
                showFileFromFriend(message,file);
            }
            //群文件
            else if(messageType.equals(Send_FileToGroup)){
                File file =ClientControlCollection.myManage.reciveFile(message);
                showGroupFileFromFriend(message,file);
            }
            //表情信息
            else if(messageType.equals(SEND_EMOTION)){
                showEmotionFromFriends(message);
            }
            //群聊表情
            else if(messageType.equals(SEND_EMOTIONToAll)){
                showGroupEmotionFromFriend(message);
            }
            //下线反馈
            else if(messageType.equals(YOU_CAN_OFF_LINE)){
                try {
                    ClientControlCollection.myManage.CloseResource();
                    ClientControlCollection.myManage.setIsConnect(false);
                    JOptionPane.showMessageDialog(null,"下线成功，期待下一次见面哦~");
                    //确认信息后两秒退出
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
            else{//
            }
    }


    /**
     * 接受文字信息
     * @param message 信息
     */
    private static void showMessageFromMyFriend(Message message) {
        //如果面板不存在，则新建
        if(!ClientControlCollection.windowOfFriends.containsKey(message.getSender())){
            ClientControlCollection.addChatFrame(message.getSender(),new ChatFrame(message.getSender(),message.getGetter()));
        }
        ChatFrame myFrame = ClientControlCollection.getChatFrame(message.getSender());
        //写入面板
        insert(message.getTime()+"\n",message.getSender(),1,myFrame);
        insert(message.getSender()+": ",message.getSender(),myFrame);
        //插入文字内容
        try {
            myFrame.doc.insertString(myFrame.doc.getLength(),message.getContent()+"\n\n",message.getAttributeSet());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        //输出聊天记录
        ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.myFriendName,message.getTime()+"\n"+message.getSender()+": "
                +message.getContent()+"\n","TEXT");
    }

    /**
     * 文字消息（群聊版）
     * @param message 消息包
     */
    private static void showMessageFromGroup(Message message){
        //没有群聊自动添加（新建相关控件并添加到容器里面）
        if(!ClientControlCollection.cardOfGroupChat.containsKey(message.getMessageTypeDetail())){
            GroupChatLable lable=new GroupChatLable(message.getGetter(),message.getMessageTypeDetail());
            ClientControlCollection.cardOfGroupChat.put(message.getMessageTypeDetail(),lable);
            //输出群聊信息到本地，使再次登录能有这个群聊
            ClientControlCollection.fileHandler.outPutGroupChatName(ClientControlCollection.myFrame.myName,message.getMessageTypeDetail(),message.getGetter());
            //添加面板
            ClientControlCollection.myFrame.groupChat.add(lable.friendCard);
            //更新界面
            ClientControlCollection.myFrame.updatePanel();
        }
        //是否已经打开窗口，没有，添加窗口
        if(!ClientControlCollection.windowOfGroupChat.containsKey(message.getMessageTypeDetail())){
            ClientControlCollection.windowOfGroupChat.put(message.getMessageTypeDetail(),new GroupChatFrame(ClientControlCollection.myFrame.myName,message.getGetter(),message.getMessageTypeDetail()));
        }
        GroupChatFrame myFrame = ClientControlCollection.windowOfGroupChat.get(message.getMessageTypeDetail());
        //写入面板
        insert(message.getTime()+"\n",message.getSender(),1,myFrame);
        insert(message.getSender()+": ",message.getSender(),myFrame);
        //插入文字内容
        try {
            myFrame.doc.insertString(myFrame.doc.getLength(),message.getContent()+"\n\n",message.getAttributeSet());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        //输出聊天记录
        ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.chatFrameName,message.getTime()+"\n"+message.getSender()+": "
                +message.getContent()+"\n","TEXT");
    }

    /**
     * 表情
     * @param message 消息包
     */
    private static void showEmotionFromFriends(Message message){
        System.out.println("是表情");
        //是否已经打开窗口，没有的话新建一个（弹出效果）
        if(!ClientControlCollection.windowOfFriends.containsKey(message.getSender())){
            ClientControlCollection.addChatFrame(message.getSender(),new ChatFrame(message.getSender(),message.getGetter()));
        }
        try {
            ChatFrame myFrame = ClientControlCollection.getChatFrame(message.getSender());
            //这段代码的作用是显示日期
            insert("时间："+message.getTime()+"\n",message.getSender(),1,myFrame);
            insert(message.getSender()+":",message.getSender(),myFrame);
            //获取目前面板内容的长度，以用于插入图片
            myFrame.chat.setCaretPosition(myFrame.doc.getLength());
            //插入表情
            myFrame.chat.insertIcon(new ImageIcon(MessageHandler.class.getResource(message.getContent())));
            //暂停一会并重新分配线程，防止刷新不及时
            try {
                Thread.sleep(100);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //中间空一行
            myFrame.doc.insertString(myFrame.doc.getLength(), "\n\n", getAttributeSet(myFrame));
            //输出聊天记录
            ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.myFriendName,message.getTime()+"\n"+message.getSender()+": \n"
                    +message.getContent()+"\n","EMOTION");
        }catch (BadLocationException e){
            e.printStackTrace();
        }
    }

    /**
     * 表情（群聊版）
     * @param message 消息包
     */
    private static void showGroupEmotionFromFriend(Message message){
        //没有群聊自动添加（新建相关控件并添加到容器里面）
        if(!ClientControlCollection.cardOfGroupChat.containsKey(message.getMessageTypeDetail())){
            GroupChatLable lable=new GroupChatLable(message.getFriendsName(),message.getMessageTypeDetail());
            ClientControlCollection.cardOfGroupChat.put(message.getMessageTypeDetail(),lable);
            //输出群聊信息到本地，使再次登录能有这个群聊
            ClientControlCollection.fileHandler.outPutGroupChatName(ClientControlCollection.myFrame.myName,message.getMessageTypeDetail(),message.getFriendsName());
            //添加面板
            ClientControlCollection.myFrame.groupChat.add(lable.friendCard);
            //更新界面
            ClientControlCollection.myFrame.updatePanel();
        }
        //是否已经打开窗口，没有，添加窗口
        if(!ClientControlCollection.windowOfGroupChat.containsKey(message.getMessageTypeDetail())){
            ClientControlCollection.windowOfGroupChat.put(message.getMessageTypeDetail(),new GroupChatFrame(ClientControlCollection.myFrame.myName,message.getFriendsName(),message.getMessageTypeDetail()));
        }
        try {
            GroupChatFrame myFrame = ClientControlCollection.windowOfGroupChat.get(message.getMessageTypeDetail());
            System.out.println("对话框名字" + myFrame.chatFrameName + "   好友名字：" + myFrame.myFriendNames + "\n 消息类型" + message.getMessageType() + "  好友名字 ：" + message.getFriendsName() + "  收获者： " + message.getGetter() + " 详细类型： " + message.getMessageTypeDetail());
            //这段代码的作用是显示日期
            insert("时间："+message.getTime()+"\n",message.getSender(),1,myFrame);
            insert(message.getSender()+":",message.getSender(),myFrame);
            //获取目前面板内容的长度，以用于插入图片
            myFrame.chat.setCaretPosition(myFrame.doc.getLength());
            //插入表情
            myFrame.chat.insertIcon(new ImageIcon(MessageHandler.class.getResource(message.getContent())));
            //暂停一会并重新分配线程，防止死锁
            try {
                Thread.sleep(100);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //中间空一行
            myFrame.doc.insertString(myFrame.doc.getLength(), "\n\n", getAttributeSet(myFrame));
            //输出聊天记录
            ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.chatFrameName,message.getTime()+"\n"+message.getSender()+": \n"
                    +message.getContent()+"\n","EMOTION");
        }catch (BadLocationException e){
            e.printStackTrace();
        }
    }
    /**
     * 展现好友发送来的文件
     * @param message 消息包
     * @param selectedFile 发送来的文件
     */
     private static void showFileFromFriend(Message message,File selectedFile){
        //如果面板不存在，则新建
        if(!ClientControlCollection.windowOfFriends.containsKey(message.getSender())){
            ClientControlCollection.addChatFrame(message.getSender(),new ChatFrame(message.getSender(),message.getGetter()));
        }
        ChatFrame myFrame = ClientControlCollection.getChatFrame(message.getSender());
        //获取路径
        String filePath = selectedFile.getPath();
        Image image = null;
        try {
            //判断是否为图片
            image = ImageIO.read(selectedFile);
        } catch (IOException e) {
            System.out.println("不是图片");
        }
         insert(message.getTime()+"\n",message.getSender(),1,myFrame);
         //为图片
         if(null != image){
             insert(message.getSender()+": 图片\n",message.getSender(),myFrame);
            myFrame.chat.setCaretPosition(myFrame.doc.getLength());
            myFrame.chat.insertIcon(new ImageIcon(getScaledImage(image,myFrame.getWidth() /3,myFrame.getHeight()/3)));
            //重新分配线程，防止死锁
            try {
                Thread.sleep(100);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             //输出聊天记录
             ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.myFriendName,message.getTime()+"\n"+message.getSender()+": 图片\n"
                     +filePath+"\n"+selectedFile.getName()+"\n","PICTURE");
         }
        //为文件
        else{
            insert(message.getSender()+": 文件\n",message.getSender(),myFrame);
            myFrame.chat.setCaretPosition(myFrame.doc.getLength());
            myFrame.chat.insertIcon(new ImageIcon("Client/src/images/文件图片.png"));
            //重新分配线程，防止死锁
            try {
                Thread.sleep(100);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //输出聊天记录
             ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.myFriendName,message.getTime()+"\n"+message.getSender()+": 文件\n"
                     +selectedFile.getName()+"\n","FILE");
         }
         insert("\n",message.getSender(),myFrame);
         //文件名
         insert(selectedFile.getName(),message.getSender(),myFrame);
         //换行
         insert("\n\n",message.getSender(),myFrame);
         myFrame.chat.select(myFrame.doc.getLength(), myFrame.doc.getLength());
     }

    /**
     * 接受文件展示方法（群聊版）
     * @param message 文件信息
     * @param selectedFile 接受到的文件
     */
    private static void showGroupFileFromFriend(Message message, File selectedFile) {
        //没有群聊自动添加（新建相关控件并添加到容器里面）
        if(!ClientControlCollection.cardOfGroupChat.containsKey(message.getMessageTypeDetail())){
            GroupChatLable lable=new GroupChatLable(message.getFriendsName(),message.getMessageTypeDetail());
            ClientControlCollection.cardOfGroupChat.put(message.getMessageTypeDetail(),lable);
            //输出群聊信息
            ClientControlCollection.fileHandler.outPutGroupChatName(ClientControlCollection.myFrame.myName,message.getMessageTypeDetail(),message.getFriendsName());
            //添加面板
            ClientControlCollection.myFrame.groupChat.add(lable.friendCard);
            ClientControlCollection.myFrame.updatePanel();
        }
        //是否已经打开窗口，没有，添加窗口
        if(!ClientControlCollection.windowOfGroupChat.containsKey(message.getMessageTypeDetail())){
            ClientControlCollection.windowOfGroupChat.put(message.getMessageTypeDetail(),new GroupChatFrame(ClientControlCollection.myFrame.myName,message.getFriendsName(),message.getMessageTypeDetail()));
        }
        GroupChatFrame myFrame = ClientControlCollection.windowOfGroupChat.get(message.getMessageTypeDetail());
        System.out.println("对话框名字"+myFrame.chatFrameName+"   好友名字："+myFrame.myFriendNames +"\n 消息类型"+message.getMessageType()+"  好友名字 ："+message.getFriendsName()+"  收获者： "+message.getGetter()+" 详细类型： "+message.getMessageTypeDetail());
        //获取路径
        String filePath = selectedFile.getPath();
        Image image = null;
        try {
            //判断是否为图片
            image = ImageIO.read(selectedFile);
        } catch (IOException e) {
            System.out.println("不是图片");
        }
        //暂停一会并重新分配线程，防止堵塞
        try {
            Thread.sleep(100);
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //为图片
        insert(message.getTime()+"\n",message.getSender(),1,myFrame);
        if(null != image){
            insert(message.getSender()+": 图片\n",message.getSender(),myFrame);
            myFrame.chat.setCaretPosition(myFrame.doc.getLength());
            //插入图片（已经适当剪切大小防止过大）
            myFrame.chat.insertIcon(new ImageIcon(getScaledImage(image,myFrame.getWidth() /3,myFrame.getHeight()/3)));
            //重新分配线程，防止死锁
            try {
                Thread.sleep(100);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //输出聊天记录
            ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.chatFrameName,message.getTime()+"\n"+message.getSender()+": 图片\n"
                    +filePath+"\n"+selectedFile.getName()+"\n","PICTURE");
        }
        //为文件
        else{
            insert(message.getSender()+": 文件\n",message.getSender(),myFrame);
            myFrame.chat.setCaretPosition(myFrame.doc.getLength());
            myFrame.chat.insertIcon(new ImageIcon("Client/src/images/文件图片.png"));
            //重新分配线程，防止死锁
            try {
                Thread.sleep(100);
                Thread.yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //输出聊天记录
            ClientControlCollection.fileHandler.outPutHistory(myFrame.myName,myFrame.chatFrameName,message.getTime()+"\n"+message.getSender()+": 文件\n"
                   +selectedFile.getName()+"\n","FILE");
        }
        insert("\n",message.getSender(),myFrame);
        insert(selectedFile.getName(),message.getSender(),myFrame);
        insert("\n\n",message.getSender(),myFrame);
        myFrame.chat.select(myFrame.doc.getLength(), myFrame.doc.getLength());
    }
    /**
     * 上下线方法
     * @param message 信息包
     */
    private static void updateMyFriendCard(Message message) {
        //上线信息
        if (message.getMessageType().equals(Just_login)) {
            //先验证是否存在
            if (ClientControlCollection.getFriendCard(message.getSender())==null) {
                return;
            }
            ClientControlCollection.getFriendCard(message.getSender()).changeStatus(true);
            //上线提示两秒
            ClientControlCollection.threadPool.execute(() -> {
                try {
                    JustOnlineNotify notify =new JustOnlineNotify(message.getSender());
                    Thread.sleep(2000);
                    notify.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        //下线信息
        else if (message.getMessageType().equals(Just_OFF_Line)){
            //先验证是否存在
            if (ClientControlCollection.getFriendCard(message.getSender())==null) {
                return;
            }
            ClientControlCollection.getFriendCard(message.getSender()).changeStatus(false);
            //下线提示两秒
            ClientControlCollection.threadPool.execute(() -> {
                try {
                    JustOffLineNotify notify =new JustOffLineNotify(message.getSender());
                    Thread.sleep(2000);
                    notify.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        ClientControlCollection.myFrame.updatePanel();
    }
    /**
     * 插入文字方法
     */
     public static void insert(String string, String getter,ChatFrame frame){
        try {
            ChatFrame myFrame = frame;
            myFrame.doc.insertString(myFrame.doc.getLength(),string,getAttributeSet(myFrame));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    /**
     * 插入文字方法 （群聊版）
     */
    public static void insert(String string, String getter,GroupChatFrame frame){
        try {
            GroupChatFrame myFrame = frame;
            myFrame.doc.insertString(myFrame.doc.getLength(),string,getAttributeSet(myFrame));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    /**
     * 可调节对齐版本
     * @param string 要插入的文字
     */
     public static void insert(String string, String getter, int align,ChatFrame frame){
        try {
            ChatFrame myFrame = frame;
            myFrame.doc.insertString(myFrame.doc.getLength(),string,getAttributeSet(myFrame,align));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    /**
     * 可调节对齐版本(群聊版)
     * @param string 要插入的文字
     */
    public static void insert(String string, String getter, int align,GroupChatFrame frame){
        try {
            GroupChatFrame myFrame = frame;
            myFrame.doc.insertString(myFrame.doc.getLength(),string,getAttributeSet(myFrame,align));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    /**
     * 初始化好友列表
     * @param onlinePeople 在线成员
     */
    private static void initializeClientFrame(String onlinePeople){
        String[]  allOnlineUser =onlinePeople.split(" ");
        for (String name:allOnlineUser) {
            FriendLable card =ClientControlCollection.getFriendCard(name);
            if(card!=null){
                card.changeStatus(true);
            }
        }
    }
    /**
     *
     * @param srcImg 插入的图片
     * @param w 宽
     * @param h 高
     * @return 修改完的图片
     */
    private static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * 获取文字类型
     * @param myFrame 面板
     * @return 返回值
     */
    public static SimpleAttributeSet getAttributeSet(ChatFrame myFrame){
        //获取文字类型
        String fontFamily= (String) myFrame.fontName.getSelectedItem();
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
    /**
     * 获取文字类型(群聊版)
     * @param myFrame 面板
     * @return 返回值
     */
    public static SimpleAttributeSet getAttributeSet(GroupChatFrame myFrame) {
        //获取文字类型
        String fontFamily = (String) myFrame.fontName.getSelectedItem();
        //获取文字大小
        int size = Integer.parseInt((String) Objects.requireNonNull(myFrame.fontSize.getSelectedItem()));
        SimpleAttributeSet fontProperty = new SimpleAttributeSet();
        if (fontFamily != null) {
            StyleConstants.setFontFamily(fontProperty, fontFamily);
        } else {
            StyleConstants.setFontFamily(fontProperty, "微软雅黑");
        }
        StyleConstants.setFontSize(fontProperty, size);
        return fontProperty;
    }
        /**
         * 可设置对齐的类型
         * @return 类型集
         */
    public static SimpleAttributeSet getAttributeSet(ChatFrame myFrame, int align){
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
    public static String getTimeAndName(Message message){
        return "时间："+message.getTime()+"\n"+message.getSender()+":";
    }
    /**
     * 可设置对齐的类型 （群聊版）
     * @return 类型集
     */
    public static SimpleAttributeSet getAttributeSet(GroupChatFrame myFrame, int align){
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
     * 添加好友的消息处理器
     * @param message 消息包
     */
    private static void showAddResult(Message message){
        //获取添加面板
        addFrame=ClientControlCollection.addFrame;
        String result=message.getContent();
        //ass为助手简写assistant
        FileHandler ass = ClientControlCollection.fileHandler;
        //存在
        if (Register_Fail_alreadyExist.equals(result)) {
            JOptionPane.showMessageDialog(null, "添加好友成功");
            //发送消息获取好友的在线情况
            message.setMessageType(Make_SURE_USER_ISONLINE);
            boolean isOnline = ClientControlCollection.myManage.isOnline(message);
            //好友列表设置格式为用户名+ofFriends
            ass.setPath("ClientOfFriends/" +ClientControlCollection.myFrame.myName + "ofFriends.txt");
            //将好友存起来
            ass.outputFriends(addFriendFrame.wantToAddUserName);
            FriendLable lable = new FriendLable(addFriendFrame.wantToAddUserName, false);
            //加入控件集合
            ClientControlCollection.addFriendCard(addFriendFrame.wantToAddUserName, lable);
            //加入面板
            ClientControlCollection.myFrame.singleChat.add(lable.friendCard);
            //关闭资源
            ClientControlCollection.addFrame.dispose();
            ClientControlCollection.addFrame=null;
            //不存在
        } else if (NO_EXIST.equals(result)) {
            addFrame.input.setText("");
            JOptionPane.showMessageDialog(null, "用户不存在");
            //在线
        } else if (Login.equals(result)) {
            ClientControlCollection.getFriendCard(message.getGetter()).changeStatus(true);
            //不在线
        } else if (NoLogin.equals(result)) {
            //
        }
    }
}
