package viewOfServer;

import messageHandlerOfServer.ServerConnectCollection;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florence
 * 强制下线界面
 */
public class focusOffLineFrame extends JFrame implements ActionListener {
    public static String wantToDeleteUserName;
    public JTextField input = new JTextField(14);
    JLabel reminder = new JLabel("输入强制下线用户名字：");
    JButton sure = new JButton("确认");
    public focusOffLineFrame() {
        reminder.setIcon(new ImageIcon(this.getClass().getResource("/images/人.png")));
        input.setPreferredSize(new Dimension(200,35));
        reminder.setPreferredSize(new Dimension(200,70));
        //设置边框
        Border border = BorderFactory.createDashedBorder(Color.CYAN, 15f, 10);
        Border myBorder = BorderFactory.createTitledBorder(border);
        input.setBorder(myBorder);
        add(reminder);
        add(input);
        add(sure);
        sure.addActionListener(this);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(sure);
        this.setPreferredSize(new Dimension(250,200));
        this.pack();
        this.setResizable(false);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(focusOffLineFrame.class.getResource("/Images/服务器监控.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==sure){
            sure.setEnabled(false);
            //用户名为空
            if("".equals(input.getText())){
                JOptionPane.showMessageDialog(null,"请输入好友名字（不能为空）");
                sure.setEnabled(true);
                return;
            }
            //用户不在线
            if(ServerConnectCollection.getServerContinueConnectClient(input.getText())==null){
                JOptionPane.showMessageDialog(null,"用户不在线");
                sure.setEnabled(true);
                return;
            }
            //移除该被强制下线的用户的线程
            ServerConnectCollection.getServerContinueConnectClient(input.getText()).CloseThread();
            ServerConnectCollection.RemoveServerContinueConnetClient(input.getText());
            sure.setEnabled(true);
            this.dispose();
        }
    }
/*------------------------------------------------------------------------------
    #测试用例
    public static void main(String[] args) {
        new focusOffLineFrame();
    }
/*------------------------------------------------------------------------------*/
}
