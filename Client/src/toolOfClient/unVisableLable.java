package toolOfClient;

import javax.swing.*;
import java.awt.*;

/**
 * 该类用来创建一些不可见的组件  ，用来隔开一些控件，窗口美化的功能类
 * @author Florence
 */
public class unVisableLable extends JLabel{
   public Box container;

    /**
     *
     * @param widthSupport 提供的宽
     * @param heightSupport 提供的高
     */
   public unVisableLable(int widthSupport,int heightSupport){
       container=Box.createVerticalBox();
       container.add(Box.createHorizontalStrut(widthSupport));
       container.add(Box.createVerticalStrut(heightSupport));
       this.setPreferredSize(new Dimension(widthSupport,heightSupport));
       this.setVisible(false);
   }
   public unVisableLable(){
       this.setPreferredSize(new Dimension(32,32));
       this.setVisible(false);
   }
}
