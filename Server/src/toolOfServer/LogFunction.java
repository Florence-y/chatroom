package toolOfServer;


import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Florence
 * 日志功能类
 */
public class LogFunction {
    static BufferedWriter out;
    static BufferedReader in;
    static String deliverString;
    static SimpleAttributeSet fontProperty = new SimpleAttributeSet();

    /**
     * 读取日志
     * @param pane 面板
     */
    public static void readLog(JTextPane pane){
        File file = new File("Server/src/LogHistory.txt");
        StringBuilder history = new StringBuilder();
        //如果文件不存在
        if(!file.exists()){
            try {
                file.createNewFile();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //插入日志面板
        try {
            FileReader inFile = new FileReader(file);
            in = new BufferedReader(inFile);
            //循环读取
            while ((deliverString=in.readLine())!=null){
                history.append(deliverString).append("\n");
            }
            StyledDocument doc =pane.getStyledDocument();
            doc.insertString(doc.getLength(),history.toString(),fontProperty);
            in.close();
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出日志
     * @param history 日志
     */
    public static void outLog(String history){
        File file = new File("Server/src/LogHistory.txt");
        //如果文件不存在
        if(!file.exists()){
            try {
                file.createNewFile();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //输出日志
        try {
            FileWriter outFile = new FileWriter(file,true);
            out= new BufferedWriter(outFile);
            out.write(getTime()+history+"\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取时间的类
     * @return 时间
     */
    private static String getTime(){
        Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        return formatter.format(time)+" ";
    }
}
