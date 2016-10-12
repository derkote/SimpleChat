package ServerSide;

import ServerSide.Message.Message;
import java.util.ArrayList;

/**
 * Синглтон
 * Контейнер хранит сообщения клиентов типа М
 * @author derkote
 * @version 0.1
 */
public class MessagePool<M> {

    /** Список всех сообщений */
    private volatile ArrayList<M> messagePool;
    private static MessagePool ourInstance = new MessagePool<Message>();
    public static synchronized MessagePool getInstance() {
        return ourInstance;
    }

    private MessagePool() {
        messagePool = new ArrayList<>(20);
        /*for (int i = 0; i < 10; i++) {
            Account acc = new Account();
            acc.setNickName("Server");
            messagePool.add(i,  (M) new Message(acc, String.valueOf(i)));
        }*/
    }


    public int size() {
        return messagePool.size();
    }

    public synchronized void addMessage(M message) {
        messagePool.add(message);
    }

    /**
     * Возвращает последние сообщения, максимум 10 штук
     * @return список сообщений типа M
     * */
    public synchronized ArrayList<M> getLast() {
        ArrayList<M> temp;
        int length = messagePool.size();
        System.out.println(messagePool.size());
        if (length < 10) {
            temp = new ArrayList<>(length);
            for (int i = length; i > 0; i--) {
                temp.add(length-i, messagePool.get(length - i));
            }
        } else {
            temp  = new ArrayList<>(10);
            for (int i = 10; i > 0; i--) {
                temp.add(10-i, messagePool.get(length - i));
            }
        }
        return temp;
    }

}
