package Run;
import toolOfServer.BeautyTool;
import viewOfServer.ServerFrame;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Florence
 * 入口方法
 */
public class Server {
    public static void main(String[] args) {
        BeautyTool.makeBeauty();
        try {
            //在EDT线程中运行，防止界面初始化错误
            SwingUtilities.invokeAndWait(() -> new ServerFrame());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
