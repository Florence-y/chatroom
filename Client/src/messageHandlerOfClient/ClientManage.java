package messageHandlerOfClient;
import Run.Client;
import dataPackageClass.Message;
import dataPackageClass.User;
import threadOfClient.contactWithServerThread;
import viewOfClient.ClientFrame;
import java.io.*;
import java.net.*;
import static dataPackageClass.MessageTypeInterface.*;

/**
 * @author Florence
 * 发送少量信息和主要管理登录注册处理的类
 */
public class ClientManage {
    /**
     * 输出输入接口
     */
    private ObjectOutputStream os;
    private ObjectInputStream ois;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    private Socket s;
    private boolean isConnect = true;
    private ServerSocket ss;

    public ClientManage(){
        try {
            //连接服务器
            s = new Socket("127.0.0.1", 9999);
            System.out.println("接口已经打开");
        } catch(ConnectException e){
            System.out.println("false");
            isConnect = false;
        } catch (IOException e) {
            e.printStackTrace();
            isConnect = false;
        }
    }

    /**
     * 注册方法
     * @param user 要注册的用户
     * @return 注册结果
     */
    public String Register(User user){
        //未连接服务器
        if(!IsConnect()){
            return notConnect_Databases;
        }
        String registerStatu = null;
        try {
            if(s!=null){
                os = new ObjectOutputStream(s.getOutputStream());
                //设置为为登录的用户信息包
                user.setType(UserRegister);
                os.writeObject(user);
                ois = new ObjectInputStream(s.getInputStream());
                Message mes = (Message)ois.readObject();
                //获取结果
                registerStatu=mes.getMessageType();
            }
        } catch (SocketException e){
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //返回结果，让监听器反应给界面
        return registerStatu;
    }

    /**
     * 登录方法
     * @param user 要登录的用户
     * @param model 模式（有未在线登录模式与在线登录模式（后者用于验证用户是否在线））
     * @return 登录的结果
     */
    public String Login(User user ,boolean model){
        String loginStatu = null;
        //未连接服务器
        if(!IsConnect()){
            return notConnect_Databases;
        }
        if(s!=null){
            try {
                //在另外的线程执行，防止EDT线程堵塞
                ClientControlCollection.threadPool.execute(() -> {
                    try {
                        os=new ObjectOutputStream(s.getOutputStream());
                        user.setType(UserLogin);
                        os.writeObject(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                ois = new ObjectInputStream(s.getInputStream());
                Message mes = (Message)ois.readObject();
                loginStatu=mes.getMessageType();
            } catch (SocketException e){
                e.printStackTrace();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        assert loginStatu != null;
        //登录成功，将相关线程加入容器中，并将界面和处理器的指针赋给容器类里的静态对象
        if(loginStatu.equals(Login_Success)&& model){
            ClientControlCollection.myManage=this;
            ClientControlCollection.setClientFrame(new ClientFrame(user.getName()));
            contactWithServerThread thread = new contactWithServerThread(s,this,user.getName());
            ClientControlCollection.myThread=thread;
            ClientControlCollection.threadPool.execute(thread);
        }
        return loginStatu;
    }

    /*
     * 检验用户存在的方法
     * @param name 用户名
     * @return 是否存在 是为true 否为false

    public boolean isUserExist(String name){
        User friend =new User();
        friend.setName(name);
        String status = this.Register(friend);
        //向服务器请求是否用户存在
        return Register_Fail_alreadyExist.equals(status);
    }
*/
    /**
     * 登录后的确认用户是否存在
     * @param mess 信息报
     * @return 是否存在
     */
     public boolean isUserExist(Message mess){
        if(!IsConnect()){
            System.out.println("未连接服务器");
            return false;
        }
        try {
            os=new ObjectOutputStream(s.getOutputStream());
            os.writeObject(mess);
            System.out.println("发送申请成功");
        } catch (SocketException e){
            e.printStackTrace();
            isConnect=false;
            System.out.println("与服务器断开连接");
        } catch (IOException e) {
            e.printStackTrace();
            isConnect=false;
            System.out.println("与服务器断开连接");
        }
         return true;
    }
    /**
     * 返回用户的在线情况
     * @param mess 消息包
     * @return 是否在线
     */
    public boolean isOnline(Message mess){
        if(!IsConnect()){
            System.out.println("未连接服务器");
            return false;
        }
        try {
            os=new ObjectOutputStream(s.getOutputStream());
            os.writeObject(mess);
        } catch (SocketException e){
            e.printStackTrace();
            isConnect=false;
            System.out.println("与服务器断开连接");
        } catch (IOException e) {
            e.printStackTrace();
            isConnect=false;
            System.out.println("与服务器断开连接");
        }
        return true;
    }

    /**
     * 判断是否连接到服务器
     * @return 是否连接到服务器
     */
    public boolean IsConnect(){
        return isConnect;
    }
    public void setIsConnect(Boolean status){ isConnect= status;}
    /**
     *发送下线信息
     */
    public void sentOffLine(String myName){
        try {
            os=new ObjectOutputStream(s.getOutputStream());
            Message mess= new Message();
            mess.setMessageType(Just_OFF_Line);
            mess.setSender(myName);
            os.writeObject(mess);
        } catch (SocketException e){
            e.printStackTrace();
            isConnect=false;
            System.out.println("与服务器断开连接");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            isConnect=false;
            System.out.println("与服务器断开连接");
            System.exit(0);
        }
    }
    /**
     * 关闭所有资源，在退出的时候调用
     */
    public void CloseResource(){
        try {
            if(s!=null){
                s.close();
            }if(os!=null){
                os.close();
            }
            if(ois!=null){
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息包方法 ，后台交互要用到
     * @param mess 想要发送的消息
     */
    public void sendMessage(Message mess){
        if(s!=null){
            try {
                os = new ObjectOutputStream(s.getOutputStream());
                os.writeObject(mess);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接受消息的方法，后台服务类要用到
     * @return
     */
    public Message reciveMessage(){
        Message mess = null;
        if(s!=null&&!s.isClosed()&&isConnect){
            try {
                ois = new ObjectInputStream(s.getInputStream());
                mess = (Message)ois.readObject();
            } catch (SocketException e){
                isConnect=false;
                e.printStackTrace();
            } catch (EOFException e){
                isConnect=false;
                e.printStackTrace();
            } catch(IOException e){
                isConnect = false;
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        return mess;

    }

    /**
     * 接受文件的方法
     * @param mess 发送来的文件信息
     * @return 返回收到的文件
     */
    public File reciveFile(Message mess){
        Socket s1 = null;
        //文件默认存储路径
        File file = new File("Client/src/TempFile/ClientOfFile/"+mess.getGetter()+"/");
        try {
            //打开接受Socket
            ss = new ServerSocket(7777);
            s1 = ss.accept();
            //判断文件夹是否存在
            if(!file.exists()){
                file.mkdirs();
            }
            bis = new BufferedInputStream(s1.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream("Client/src/TempFile/ClientOfFile/"+mess.getGetter()+"/"+mess.getContent()));
            byte[] bys = new byte[1024];
            int len;
            //接受文件
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(ss!=null){
                    ss.close();
                }
                if(bis!=null){
                    bis.close();
                }if(s1!=null){
                    s1.close();
                }if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  new File("Client/src/TempFile/ClientOfFile/"+mess.getGetter()+"/"+mess.getContent());
    }
    /**
     * 发送文件的方法
     * @param path 文件路径
     */
    public void sendFile(String path){
        Socket socket = null;
        try {
            //新创建一个TCP协议发送文件
            socket = new Socket("127.0.0.1", 8888);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            if (socket != null) {
                bos = new BufferedOutputStream(socket.getOutputStream());
            }
            bis = new BufferedInputStream(new FileInputStream(path));
            byte[] bys = new byte[1024];
            int len;
            //发送文件
            while ((len = bis.read(bys)) != -1) {
                bos.write(bys, 0, len);
                bos.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(socket!=null){
                try {
                    socket.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        ClientManage test = new ClientManage();
        Scanner in = new Scanner(System.in);
        in.next();
    }
     ----------------------------------------------------------------------------------*/
}
