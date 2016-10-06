package ServerSide.Message;

import java.io.Serializable;

/**
 * Created by derkote on 06.10.2016.
 */
public class ServerMessage extends AbstractMessage implements Serializable {

    public ServerMessage(String message) {
        this.message = message;
        this.account = null;
        this.date = null;
    }
}
