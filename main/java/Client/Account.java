package Client;

import java.io.Serializable;

/**
 * Контейнер с информацией о отправителе сообщения
 * @author derkote
 * @version 0.1
 * TODO: хранить пароль в хешированном виде
 */
public class Account implements Serializable {
    private String nickName;
    private String password;
    public int versionID = 1;


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
