package viewOfServer;
import controlerOfServer.showAllUsersFrameListener;
import toolOfServer.ServerConnectDatabase;
import toolOfServer.userLable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Florence
 * 显示所有用户界面
 */
public class showAllUsersFrame extends JFrame {
    public JButton log= new JButton("用户日志");
    public JButton focusOffline = new JButton("强制下线");
    public JPanel mainPanel = new JPanel();
    private ServerConnectDatabase handler = new ServerConnectDatabase();
    JLabel allUsers = new JLabel("所有成员");
    Box firstLine;
    Box onlineBox;
    showAllUsersFrameListener listener=new showAllUsersFrameListener(this);
    public showAllUsersFrame(){
        super.setTitle("控制台");
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,10));
        firstLine=Box.createHorizontalBox();
        firstLine.add(Box.createHorizontalStrut(100));
        firstLine.add(log);
        firstLine.add(Box.createHorizontalStrut(20));
        firstLine.add(focusOffline);
        firstLine.add(Box.createHorizontalStrut(20));
        firstLine.setBorder(BorderFactory.createEmptyBorder(12, 12, 12,12));
        onlineBox=Box.createHorizontalBox();
        onlineBox.add(Box.createHorizontalStrut(100));
        onlineBox.add(allUsers);
        onlineBox.add(Box.createHorizontalStrut(100));
        mainPanel.add(onlineBox);
        initMainPanel();

        //添加监听器和全局设置并且开启线程

        setListener();
        this.add(firstLine,BorderLayout.NORTH);
        this.add(mainPanel);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(ServerFrame.class.getResource("/images/服务器监控.png")));
        this.setSize(400,600);
        this.setLocationRelativeTo(null);
        this.setLocation(900,200);
        this.setVisible(true);
    }
/*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        new showAllUsersFrame();
    }
  *------------------------------------------------------------------------------ */

    private void setListener(){
        focusOffline.addActionListener(listener);
        log.addActionListener(listener);
    }

    /**
     * 初始化控制台面板
     */
    private void initMainPanel(){
        List<String> list=handler.getUsers();
        for (String userName:list){
            mainPanel.add(new userLable(userName));
        }
        mainPanel.updateUI();
    }
}
