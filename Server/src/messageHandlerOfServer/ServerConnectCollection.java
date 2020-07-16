package messageHandlerOfServer;
import dataPackageClass.User;
import toolOfServer.MysqlConnectClass;
import toolOfServer.userLable;
import viewOfServer.ServerFrame;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 服务器端的集合线程类
 * @author Florence
 */
public class ServerConnectCollection {
    /**
     * 服务器后台管理器
     */
    public static ServerManage myManage;
    /**
     * 服务器端界面面板
     */
    public static ServerFrame serverFrame;
    /**
     * 存用户信息面板
     */
    private static ConcurrentHashMap<String, userLable> onlineClientCard=new ConcurrentHashMap<>();
    /**
     * 存用户联系线程
     */
    private static ConcurrentHashMap<String,ContactWithClientThread> onlineClientThread=new ConcurrentHashMap<>();

    /**
     *创建线程池
     */
    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 20, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
    /**
     * 添加线程进入集合
     * @param name 用户名
     * @param serverConClient 用户通信线程
     */
    public static void addServerConnectClientThreadCollection(String name, ContactWithClientThread serverConClient){
        onlineClientThread.put(name,serverConClient);
    }

    /**
     * 获取线程
     * @param name 用户名
     * @return 得到的线程
     */
    public static ContactWithClientThread getServerContinueConnectClient(String name){
        return onlineClientThread.get(name);
    }

    /**
     * 添加面板
     * @param name 用户名
     * @param card 面板
     */
    public static void addUserCard(String name,userLable card){
        onlineClientCard.put(name,card);
    }
    public static userLable getUserCard(String name){
        return onlineClientCard.get(name);
    }
    /**
     * 删除线程并退出
     * @param name 用户名
     */
    public static void RemoveServerContinueConnetClient(String name) {
        MysqlConnectClass connectMysql= new MysqlConnectClass();
        //删除用户在线头像区，
        if (!onlineClientCard.containsKey(name)) {
        } else {
            ServerConnectCollection.serverFrame.mainPanel.remove(getUserCard(name));
            ServerConnectCollection.serverFrame.updateFrame();
        }
        //更新数据库在线状态
        User u =new User();
        u.setName(name);
        connectMysql.Update_IsLogin(u,0);
        onlineClientThread.remove(name);
    }
    /**
     * 遍历集合返回在线用户
     * @return 带有在线用户的字符串
     */
    public static String GetOnline(){
        StringBuilder OnlineUsers = new StringBuilder();
        for (String s : onlineClientThread.keySet()) {
            OnlineUsers.append(s).append(" ");
        }
        return OnlineUsers.toString();
    }
}
