package viewOfClient;
import controlerOfClient.RegisterListener;
import messageHandlerOfClient.ClientManage;
import toolOfClient.BeautyTool;
import toolOfClient.MyPanel;
import toolOfClient.unVisableLable;
import javax.swing.*;
import java.awt.*;
/**
 * @author Florence
 */
public class RegisterFrame extends JFrame {
    public JButton register;
    public JButton clear;
    public JButton exit;
    public JTextField name;
    public JPasswordField firstPassword;
    public JPasswordField secondPassword;
    JLabel user;
    JLabel firsrPasswordPicture;
    JLabel secondPasswordPicture;
    MyPanel registerPanl;
    RegisterListener listener;
    unVisableLable unVisableView;
    public RegisterFrame(){
        super.setTitle("注册界面");
        /**
         * 控件对象声明,以及相关的图片设置
         */
        register=new JButton("确认");
        clear= new JButton("清空");
        exit= new JButton("退出");
        user= new JLabel(new ImageIcon(RegisterFrame.class.getResource("/images/注册账号.png")));
        firsrPasswordPicture=new JLabel(new ImageIcon(RegisterFrame.class.getResource("/images/第一次密码.png")));
        secondPasswordPicture=new JLabel(new ImageIcon(RegisterFrame.class.getResource("/images/第二次密码.png")));
        name= new JTextField(20);
        firstPassword= new JPasswordField(20);
        secondPassword= new JPasswordField(20);
        registerPanl= new MyPanel();
        /**
         * 这里声明监听器并传入该页面的指针
         */
        listener= new RegisterListener(this);
        unVisableView=new unVisableLable(300,20);
        /**
         * 控件设置大小
         */
        register.setPreferredSize(new Dimension(70,50));
        clear.setPreferredSize(new Dimension(70,50));
        exit.setPreferredSize(new Dimension(70,50));
        user.setPreferredSize(new Dimension(45,45));
        firsrPasswordPicture.setPreferredSize(new Dimension(45,45));
        secondPasswordPicture.setPreferredSize(new Dimension(45,45));
        name.setPreferredSize(new Dimension(40,40));
        firstPassword.setPreferredSize(new Dimension(40,40));
        secondPassword.setPreferredSize(new Dimension(40,40));
        /**
         * 全局设置以及控件添加,以及相关按钮的事件监听添加
         */
        registerPanl.setLayout(new FlowLayout(FlowLayout.CENTER));
        registerPanl.add(unVisableView.container);
        registerPanl.add(user);
        registerPanl.add(name);
        registerPanl.add(firsrPasswordPicture);
        registerPanl.add(firstPassword);
        registerPanl.add(secondPasswordPicture);
        registerPanl.add(secondPassword);
        registerPanl.add(register);
        registerPanl.add(clear);
        registerPanl.add(exit);
        //添加监听器
        setListener();
        this.add(registerPanl);
        this.getRootPane().setDefaultButton(register);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(330,300));
        this.pack();
        this.setResizable(false);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Images/聊天.png")));
    }
    private void setListener(){
        register.addActionListener(listener);
        clear.addActionListener(listener);
        exit.addActionListener(listener);
    }
    /*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        BeautyTool.makeBeauty();
    }
    /*------------------------------------------------------------------------------*/

}
