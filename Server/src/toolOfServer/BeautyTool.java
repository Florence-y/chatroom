package toolOfServer;

/**
 * 美化界面代码的封装，防止代码赘余
 * @author Florence
 */
public class BeautyTool {
    public static void makeBeauty(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }

    }
}
