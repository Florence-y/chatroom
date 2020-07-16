package dataPackageClass;

import java.io.Serializable;

/**
 * @author Florence
 * 用户信息包，网络传输时传输用户信息
 */
public class User implements Serializable {
    private static final long serialVersionUID = 4625916383824095403L;
    private String Name;
    private String PassWords;
    private String Type;

    public User(){

    }

    public User(String Name,String PassWords){
        this.Name = Name;
        this.PassWords = PassWords;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassWords() {
        return PassWords;
    }

    public void setPassWords(String passWords) {
        PassWords = passWords;
    }


}