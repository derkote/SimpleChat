package ServerSide.Message;

import Client.Account;

import java.io.Serializable;
import java.util.Date;

/**
 * Абстрактное сообщение
 * @author derkote
 * @version 0.1
 * TODO: А нужно ли оно тут вообще? Изначально планировал по другому, тяну давно
 * TODO: Убрать если будет реализация с одним видом сообщения и типами внутри
 * TODO: Оставить если будет несколько различных потомков
 */
public abstract class AbstractMessage implements Serializable {
    /** Текстовое тело сообщения */
    protected String message;
    /**
     *  Информация об отправителе
     *  TODO: Убрать, когда реализую отправку инфы об отправителе единоразого
     *  */
    protected Account account;
    /** Дата создания сообщения */
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
