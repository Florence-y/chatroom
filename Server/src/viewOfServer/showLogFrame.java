package viewOfServer;

import toolOfServer.LogFunction;

import javax.swing.*;
import java.awt.*;

/**
 * @author Florence
 * 显示日志界面
 */
public class showLogFrame  {
    JFrame chatHistoryFrame = new JFrame();
    JTextPane containerPane = new JTextPane();
    JScrollPane chatHistoryPane = new JScrollPane(containerPane);
    public showLogFrame(JButton button) {
        chatHistoryFrame.setTitle("用户日志");
        chatHistoryFrame.add(chatHistoryPane);
        containerPane.setEditable(false);
        //读取日志
        LogFunction.readLog(containerPane);
        chatHistoryPane.setPreferredSize(new Dimension(500, 500));
        chatHistoryFrame.pack();
        chatHistoryFrame.setLocationRelativeTo(button);
        chatHistoryFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(showLogFrame.class.getResource("/Images/服务器监控.png")));
        chatHistoryFrame.setVisible(true);
    }
}
