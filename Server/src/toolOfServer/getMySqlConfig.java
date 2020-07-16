package toolOfServer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 用来从配置文件获取数据库配置的类
 * @author Florence
 */
public class getMySqlConfig {
    private static InputStream in = ClassLoader.getSystemResourceAsStream("Database.properties");
    private static Properties prop = new Properties();
    public static String getProperty(String name){
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(name);
    }
}
