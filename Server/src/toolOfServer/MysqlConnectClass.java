package toolOfServer;
import dataPackageClass.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static dataPackageClass.MySqlStatement.*;

/**
 * @author Florence
 * 数据库的处理类
 */
public class MysqlConnectClass {
    private PreparedStatement readyStatment ;
    private ResultSet alreadyGetResult;
    private Connection myConnection ;
    public void init(){
        //加载驱动
        try {
            //自定义方法从配置文件Database.properties中获取
            Class.forName(getMySqlConfig.getProperty("DRIVERCLASS"));
            if(myConnection==null){
                //自定义建表方法，可以免除手动建表的麻烦，但需要数据库里有mysql这个数据库
                createMyChatroomDatabase();
            }
            //创建数据库预备语句,并且在首次登录建立Users表
            Statement command= myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            command.executeUpdate(CREATE_TABLE);
            if (!myConnection.isClosed()) {
                System.out.println("数据库连接成功");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("驱动包装载有问题");
            e.printStackTrace();
        }catch (SQLException e) {
            System.out.println("数据库连接失败，请检查数据库服务是否有打开");
            e.printStackTrace();
        }
    }
    public MysqlConnectClass(){
        this.init();
    }
    /**
     * 获取用户信息
     * @param Name 用户名
     * @return 返回结果集
     */
    public ResultSet GetUser(String Name){
        try {
            readyStatment = myConnection.prepareStatement(GET_USER_DETAIL);
            readyStatment.setString(1, Name);
            alreadyGetResult = readyStatment.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alreadyGetResult;
    }

    /**
     * 检查用户是否存在
     * @param Name 检索的用户名
     * @return 是为true否为false
     */
    public boolean userIsExist(String Name){
        try {
            readyStatment=myConnection.prepareStatement(USER_IFEXIST);
            readyStatment.setString(1,Name);
            ResultSet rs = readyStatment.executeQuery();
            if (rs.next()) {
                //不存在
                //存在
                return rs.getInt(1) != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 注册用户信息
     * @param user 用户名
     * @return 是否注册成功
     */
    public boolean Register(User user){
        boolean b = true;
        try {

            readyStatment = myConnection.prepareStatement(INSERT_NEW_USER);
            readyStatment.setString(1, user.getName());
            readyStatment.setString(2, user.getPassWords());
            // 执行sql语句
            if(readyStatment.executeUpdate()!=1)
            {
                b=false;
            }
        } catch (SQLException e) {
            b = false;
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 是否登录进行判断
     * @param user 用户名
     * @param isLogin 登录状态
     * @return 是否成功
     */
    public boolean Update_IsLogin(User user,int isLogin){
        boolean b = true;
        try {
            readyStatment = myConnection.prepareStatement(UPDATE_THE_ONLINE_STATUS);
            readyStatment.setInt(1, isLogin);
            readyStatment.setString(2, user.getName());
            // 执行sql语句
            if(readyStatment.executeUpdate()!=1)
            {
                b=false;
            }
        } catch (SQLException e) {
            b = false;
            e.printStackTrace();
        }
        return b;
    }
    /**
     * 建表方法，如果存在不会建表
     * {
     * 思路：因为Mysql都自带mysql数据库（如果师兄师姐们没有删除的话~）
     * 首先要先连接mysql数据库，然后监测chatroom数据库是否存在
     * 如果不存在，则新建chatroom数据库，并在里面建立一张users表
     * 如果存在直接退出
     * }
     */
    private void createMyChatroomDatabase(){
        try {
            //先连接每一个Mysql本地都有的mysql数据库
            myConnection= DriverManager.getConnection(getMySqlConfig.getProperty("JDBCURLPREPARE")
                    ,getMySqlConfig.getProperty("USER")
                    ,getMySqlConfig.getProperty("PASSWORD"));
            Statement subStatement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            //建库，如果存在，则不会建库
            subStatement.executeUpdate(CREATE_DATABASES);
            //重新连接chatRoom数据库
            myConnection = DriverManager.getConnection(getMySqlConfig.getProperty("JDBCURL")
                    ,getMySqlConfig.getProperty("USER")
                    ,getMySqlConfig.getProperty("PASSWORD"));


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("服务器未打开或者是连接数量太多无法进行连接，未进行正常的连接");
        }

    }
    /*--------------------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        //test
        MysqlConnectClass a= new MysqlConnectClass();
        boolean s= a.userIsExist("111");
        User b=new User();
        b.setName("admin");
        a.Update_IsLogin(b,0);
        if(s){
            System.out.println("存在");
        }
        else{
            System.out.println("不存在");
        }
    }
    ******************************************************************************************
     */
    /**
     *获取所有用户
     */
    public List<String> getAllUsers(){
        List<String> list= new ArrayList<>();
        try {
            readyStatment=myConnection.prepareStatement(GET_ALL_USERS);
            ResultSet rs = readyStatment.executeQuery();
            while (rs.next()){
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 关闭数据库资源
     */
    public void close()
    {
        try {
            if(alreadyGetResult!=null) {
                alreadyGetResult.close();
            }
            if(readyStatment!=null) {
                readyStatment.close();
            }
            if(myConnection!=null) {
                myConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
