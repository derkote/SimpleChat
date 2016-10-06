package ServerSide.Message;

import Client.Account;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by derkote on 06.10.2016.
 */
public abstract class AbstractMessage implements Serializable {
    protected String message;
    protected Account account;
    protected Date date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
