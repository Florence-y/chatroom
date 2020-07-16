package viewOfClient;

import javax.swing.*;
import java.awt.*;

/**
 * @author Florence
 * 下线界面
 */
public class JustOffLineNotify extends JFrame {

        private JLabel cardOfFriend = new JLabel();
        private JLabel tip= new JLabel();
        private static Insets screenProperty;
        private static Dimension screenSize;
        Box container = Box.createVerticalBox();
        Box firstLine  =Box.createHorizontalBox();
        Box secondLine = Box.createHorizontalBox();
        public  JustOffLineNotify(String name){
            super.setTitle("下线提示");
            //获取屏幕属性
            screenProperty= Toolkit.getDefaultToolkit().getScreenInsets(
                    this.getGraphicsConfiguration());
            //
            screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            tip.setText("您的好友："+name+"下线了！");
            cardOfFriend.setText(name);
            cardOfFriend.setAlignmentX(CENTER_ALIGNMENT);
            cardOfFriend.setIcon(new ImageIcon(this.getClass().getResource("/images/人.png")));

            //组件设置
            firstLine.add(Box.createHorizontalStrut(20));
            firstLine.add(tip);
            secondLine.add(Box.createHorizontalStrut(10));
            secondLine.add(cardOfFriend);
            container.add(firstLine);
            container.add(secondLine);
            container.setBounds(20,20,20,20);

            add(container);
            setPreferredSize(new Dimension(300,200));
            pack();
            //右下角设置
            setLocation((int)screenSize.getWidth()-300-6,(int)screenSize.getHeight()-screenProperty.bottom-200-6);
            this.setIconImage(Toolkit.getDefaultToolkit().getImage(viewOfClient.JustOnlineNotify.class.getResource("/images/上线提示.PNG")));
            setVisible(true);
        }
/*------------------------------------------------------------------------------
    #测试用例
        public static void main(String[] args) {
        }
/*------------------------------------------------------------------------------*/
    }


