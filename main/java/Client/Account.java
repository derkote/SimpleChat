package Client;

import java.io.Serializable;

/**
 * Created by derkote on 05.10.2016.
 */
public class Account implements Serializable {
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private String nickName;


    public Account() {
        this.nickName = "derkote";
    }
}
