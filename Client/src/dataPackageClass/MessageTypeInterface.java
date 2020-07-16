package dataPackageClass;

/**
 * 各种信息的包头，用来分类处理
 */
public interface MessageTypeInterface {
    //刚登录用户
    String Just_login="@Just_Login";
    //用户下线
    String Just_OFF_Line="@Just_OFF_line";
    //通知用户的下线信息已经发布完成，你可以下线了
    String YOU_CAN_OFF_LINE="@youCanOffLine";
    //用户登陆信息
    String UserLogin = "@UsersLogin";
    //用户注册信息
    String UserRegister = "@UserRegister";
    //登陆成功
    String Login_Success = "@Login_Success";
    //用户名存在但密码错误
    String Login_Fail_PasswordWrong="@Login_Fail_Wrong";
    //用户名不存在
    String Login_Fail_NoUser="@Login_Fail_NoUser";
    //登陆失败
    String Login_Fail = "@Login_Fail";
    //未连接数据库
    String notConnect_Databases="@NotConnect_Databases";
    //注册成功
    String Register_Success = "@Register_Success";
    //注册失败因为用户已存在
    String Register_Fail_alreadyExist="@Register_Fail_alreadyExist";
    //注册失败
    String Register_Fail = "@Register_Fail";
    //发送普通消息给所有人
    String Common_Message_ToGroup ="@Common_Message_ToAll";
    //发送普通消息给个人
    String Common_Message_ToPerson ="@Common_Message_ToPerson" ;
    //发送文件给所有人
    String Send_FileToGroup = "@Send_FileToAll";
    //发送文件给个人
    String Send_FileToPerson = "@Send_FileToPerson";
    //获得在线人员
    String Get_Online = "@Get_Online";
    //返回在线人员
    String Send_Online = "@Send_Online";
    //发送用户信息
    String SendUser = "@Send_User";
    //普通消息
    String CommonMessage ="@CommonMessage";
    //系统消息
    String System_Messages = "@System_Messages";
    //登陆过了
    String Login = "@Login";
    //没登陆过
    String NoLogin = "@NoLogin";
    //接收
    String Recive = "@Recive";
    //拒绝接收
    String NoRecive = "@NoRevice";
    //是否存在
    String Make_SURE_USER_ISEXIST="@IfExist";
    //是否在线
    String Make_SURE_USER_ISONLINE="@IFonline";
    //添加好友
    String ADD_Friend="@ADD";
    //不存在
    String NO_EXIST="@NoExist";
    //发送表情
    String SEND_EMOTION="@sendEmotion";
    //发送表情给所有人
    String SEND_EMOTIONToAll="@sendEmotionToAll";
}
