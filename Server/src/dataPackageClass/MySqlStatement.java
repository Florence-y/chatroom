package dataPackageClass;

/**
 * 数据库常用语句，封装起来，防止太繁杂
 * @author Florence
 */
public interface MySqlStatement {
    /**
     * 建表语句
     */
     String CREATE_TABLE = "create table if not exists Users(" +"\n"+
            "`Name` nvarchar(30) primary key," +'\n'+
            "`Password` nvarchar(30) not null," +'\n'+
            "`IsLogin` bit not null DEFAULT 0 " +'\n'+
            ")";
    /**
     *建库语句
     */
     String CREATE_DATABASES = "create  database if not exists chatroom";
    /**
     *获取用户语句
     */
     String GET_USER_DETAIL ="select * from Users where Name=?";
    /**
     * 插入新用户
     */
     String INSERT_NEW_USER ="insert into Users(Name,PassWord) values(?,?)";
    /**
     * 更新用户登录信息
     */
     String UPDATE_THE_ONLINE_STATUS ="update Users set IsLogin=? where Name=?";
    /**
     * 用户是否存在
     */
     String USER_IFEXIST ="select count(*) from Users where Name = ? ";
    /**
     * 设置全部在线状态
     */
     String UPDATE_ALL="update users set IsLogin = ? ";
    /**
     * 获取全部用户
     */
    String GET_ALL_USERS="SELECT * FROM USERS";
}
