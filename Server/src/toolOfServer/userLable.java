package toolOfServer;

import javax.swing.*;

/**
 * 用户面板
 * @author Florence
 */
public class userLable extends JLabel {
        public userLable(String name){
            super.setText(name);
            super.setIcon(new ImageIcon(this.getClass().getResource("/images/人.png")));
        }
}
