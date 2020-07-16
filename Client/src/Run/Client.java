package Run;

import toolOfClient.BeautyTool;
import viewOfClient.LoginFrame;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Florence
 * 主入口程序
 */
public class Client {
    public static void main(String[] args) {
        BeautyTool.makeBeauty();
        try {
            //在EDT线程中运行，防止界面初始化错误
            SwingUtilities.invokeAndWait(() -> new LoginFrame());
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
