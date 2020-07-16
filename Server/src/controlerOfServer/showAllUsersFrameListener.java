package controlerOfServer;

import viewOfServer.focusOffLineFrame;
import viewOfServer.showAllUsersFrame;
import viewOfServer.showLogFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florence
 * 控制台监听器
 */
public class showAllUsersFrameListener implements ActionListener {
    private showAllUsersFrame myframe;
    public showAllUsersFrameListener(showAllUsersFrame frame) {
        myframe=frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //强制下线
        if(e.getSource()==myframe.focusOffline){
            new focusOffLineFrame();
        }
        //日志
        if(e.getSource()==myframe.log){
            new showLogFrame(myframe.log);
        }
    }
}
