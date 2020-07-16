package viewOfClient;
import messageHandlerOfClient.ClientControlCollection;
import javax.swing.*;
import java.awt.*;

/**
 * 聊天记录面板
 * @author Florence
 */
public class ShowChatHistory  {
    JFrame chatHistoryFrame = new JFrame();
    JTextPane containerPane = new JTextPane();
    JScrollPane chatHistoryPane = new JScrollPane(containerPane);
    public ShowChatHistory(JButton button,String myName,String fileName){
        chatHistoryFrame.setTitle("与"+fileName+"的聊天记录");
        chatHistoryFrame.add(chatHistoryPane);
        containerPane.setEditable(false);
        ClientControlCollection.fileHandler.inPutHistory(myName,fileName,containerPane);
        chatHistoryPane.setPreferredSize(new Dimension(500, 500));
        chatHistoryFrame.pack();
        chatHistoryFrame.setLocationRelativeTo(button);
        chatHistoryFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Images/聊天.png")));
        chatHistoryFrame.setVisible(true);
    }
}