package toolOfServer;
import dataPackageClass.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static dataPackageClass.MessageTypeInterface.*;


/**
 * @author DELL
 * 代理处理服务器接口的类，为了简化客户端处理注册与登录的代码长度
 */
public class ServerConnectDatabase {
    private ResultSet rs = null;
    /**
     * 检查是否登陆成功的方法
     * @param user 用户对象
     * @return 返回是否登陆成功
     */
    public String CheckLogin(User user){
        String statu=null;
        MysqlConnectClass databaseManage = new MysqlConnectClass();
        rs = databaseManage.GetUser(user.getName());
        try {
            rs.next();
            String password = rs.getString(2);
            if(password.equals(user.getPassWords())){
                statu=Login_Success;
            }
            else if(!password.equals(user.getPassWords())){
                statu=Login_Fail_PasswordWrong;
            }
            else {
                statu=Login_Fail;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            databaseManage.close();
        }
        return statu;
    }

    /**
     * 检查是否注册成功的方法
     * @param user 用户对象
     * @return 返回是否注册成功
     */
    public boolean CheckRegister(User user){
        MysqlConnectClass  databaseManage = new MysqlConnectClass();
        return databaseManage.Register(user);
    }

    /**
     * 检查用户是否存在
     * @param user 用户对象
     * @return 返回是否存在
     */
    public boolean CheckIsExist(User user){
        MysqlConnectClass  databaseManage =  new MysqlConnectClass();

        return databaseManage.userIsExist(user.getName());
    }


    /**
     * 检查是否重复登陆的方法
     * @param user 用户对象
     * @return 是否重复登陆,登陆过的返回true,否则返回false
     */
    public boolean Check_IsLogin(User user){
        boolean b = false;
        MysqlConnectClass  databaseManage = new MysqlConnectClass();
        rs = databaseManage.GetUser(user.getName());
        try {
            rs.next();
            int isLogin = rs.getInt(3);
            if(isLogin==0){
                b =false;
            }else if(isLogin==1){
                b = true;
            }
        } catch (SQLException e) {
            b = false;
            e.printStackTrace();
        }finally{
            databaseManage.close();
        }
        return b;
    }


    /**
     * 返回成功修改登陆情况
     * @param user 用户对象
     */
    public boolean  Update_IsLogin(User user,int isLogin){
        MysqlConnectClass  databaseManage = new MysqlConnectClass();
        return databaseManage.Update_IsLogin(user,isLogin);
    }

    /**
     * 获取所有用户
     * @return 所有用户的列表
     */
    public List<String> getUsers(){
        MysqlConnectClass databaseManage=new MysqlConnectClass();
        return databaseManage.getAllUsers();
    }
    /*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        ServerConnectDatabase a = new ServerConnectDatabase();
        User b= new User();
        b.setName("admin");
        a.Check_IsLogin(b);
    }

     -----------------------------------------------------------------------------*/
}

