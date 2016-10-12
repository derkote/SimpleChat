package ServerSide.Message;

import Client.Account;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * Сообщения пересылаемые клиентами
 * @author derkote
 * @version 0.1
 */
public class Message extends AbstractMessage implements Serializable {

    public int versionID = 1;
    /**
     * Тип сообщения
     * @see ServerSide.Message.Message.MessageType
     * */
    protected MessageType messageType;

    /**
     * Новое сообщение без типа и тела
     * @param account отправитель сообщения
     */
    public Message(Account account) {
        this.account = account;
        this.messageType = MessageType.CLIENT;
        this.message = null;
        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();
        date = Date.from(instant);
    }

    /**
     * Новое сообщение без типа
     * @param account отправитель сообщения
     * @param message тело сообщения
     */
    public Message(Account account, String message) {
        this(account);
        this.message = message;
    }

    /**
     * Новое сообщение
     * @param account отправитель сообщения
     * @param message тело сообщения
     * @param messageType тип сообщения {@link ServerSide.Message.Message.MessageType}
     */
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

    /**
     * Типы сообщений
     * SERVER  - Сообщение от сервера
     * AUTH    - Авторизация
     * CLIENT  - Клиентское
     * ERROR   - Ошибка
     * SYSTEM  - Системное
     */
    public enum MessageType {SERVER, AUTH, CLIENT, ERROR, SYSTEM}
}
