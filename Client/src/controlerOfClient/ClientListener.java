package controlerOfClient;
import messageHandlerOfClient.ClientControlCollection;
import toolOfClient.addFriendFrame;
import toolOfClient.deleteFriendFrame;
import viewOfClient.ClientFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Florence
 * 客户端监听器
 */
public class ClientListener  implements ActionListener {
    ClientFrame myFrame;
    public ClientListener(ClientFrame frame){
        myFrame=frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //添加好友
        if(e.getSource()==myFrame.addFriends){
            ClientControlCollection.addFrame=new addFriendFrame(myFrame);
        }
        //删除好友
        if(e.getSource()==myFrame.removeFriends){
            new deleteFriendFrame();
        }
    }
}
