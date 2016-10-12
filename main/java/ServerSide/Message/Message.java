package ServerSide.Message;

import Client.Account;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * Created by derkote on 05.10.2016.
 */
public class Message extends AbstractMessage implements Serializable {

    protected MessageType messageType;

    public Message(Account account) {
        this.account = account;
        this.messageType = null;
        this.message = null;
        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();
        date = Date.from(instant);
    }

    public Message(Account account, String message) {
        this(account);
        this.message = message;
    }

    public Message(Account account, String message, MessageType messageType) {
        this(account, message);
        this.messageType = messageType;
    }
    public String getNickname() {
        return account.getNickName();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public enum MessageType {SERVER, AUTH, CLIENT, ERROR, SYSTEM}
}
