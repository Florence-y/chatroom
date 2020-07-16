package toolOfClient;

import javax.swing.*;
import java.awt.*;

/**
 * 窗口背景图片设置类
 * 可以实现自定义背景图片
 * @author Florence
 */
public class MyPanel extends JPanel{
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        /**
         * 绘制一张背景图片  jpg是图片的路径  自己设定为自己想要添加的图片
         */
        Image image = new ImageIcon(MyPanel.class.getResource("/Images/background.jpg")).getImage();
        image.getScaledInstance(100,100,Image.SCALE_SMOOTH);
        g.drawImage(image, 0, 0, this);
    }
}
