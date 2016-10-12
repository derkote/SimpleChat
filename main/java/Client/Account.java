package Client;

import java.io.Serializable;

/**
 * Created by derkote on 05.10.2016.
 */
public class Account implements Serializable {
    private String nickName;
    private String password;


    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }




    public Account() {
        this.nickName = "derkote";
    }
}
