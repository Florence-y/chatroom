package messageHandlerOfClient;

import threadOfClient.contactWithServerThread;
import toolOfClient.FileHandler;
import toolOfClient.FriendLable;
import toolOfClient.GroupChatLable;
import toolOfClient.addFriendFrame;
import viewOfClient.ChatFrame;
import viewOfClient.ClientFrame;
import viewOfClient.GroupChatFrame;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Florence
 * 控件的存放容器
 */
public class ClientControlCollection {
    /**
     * 文件处理类
     */
    public static FileHandler fileHandler=new FileHandler();
    /**
     * 添加好友面板
     */
    public static addFriendFrame addFrame ;
    /**
     * 线程对象
     */
    public static contactWithServerThread myThread;
    /**
     * 登录端管理类对象
     */
    public static ClientManage myManage;
    /**
     * 用户添加删除好友界面
     */
    public static ClientFrame myFrame;
    /**
     * 群聊名片
     */
    public static ConcurrentHashMap<String, GroupChatLable> cardOfGroupChat=new ConcurrentHashMap<>();
    /**
     * 用户名片
     */
    private static ConcurrentHashMap<String, FriendLable> cardOfFriends=new ConcurrentHashMap<>();
    /**
     * 用户聊天界面
     */
    public static ConcurrentHashMap<String, ChatFrame> windowOfFriends=new ConcurrentHashMap<>();
    /**
     * 群聊界面
     */
    public static ConcurrentHashMap<String, GroupChatFrame> windowOfGroupChat=new ConcurrentHashMap<>();
    /**
     * 线程池
     */
    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 20, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    /**
     * 获取好友名片
     */
    public static FriendLable getFriendCard(String name){
        return cardOfFriends.get(name);
    }
    /**
     * 输入友好名片
     */
    public static void addFriendCard(String name,FriendLable card){
        cardOfFriends.put(name, card);
    }
    /**
     * 获取聊天窗口
     */
    public static ChatFrame getChatFrame(String name){
        return windowOfFriends.get(name);
    }
    /**
     * 删除名片
     */
    public static void removeFriendCard(String name){
        cardOfFriends.remove(name);
    }
    /**
     * 加入聊天窗口
     */
    public static void addChatFrame(String name, ChatFrame frame){
        windowOfFriends.put(name, frame);
    }
    /**
     * 设置用户主窗口
     */
    public static void setClientFrame(ClientFrame frame){
        myFrame=frame;
    }
    /**
     * 得到用户主窗口
     */
    public static ClientFrame getClientFrame(){
        if (myFrame==null) {
            return null;
        }
        return myFrame;
    }
}
