package ServerSide;

import Client.Account;
import ServerSide.Message.AbstractMessage;
import ServerSide.Message.ClientMessage;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

/**
 * Created by derkote on 05.10.2016.
 */
public class MessagePool<M> {
    private volatile ArrayList<M> messagePool;

    private static MessagePool ourInstance = new MessagePool<ClientMessage>();

    public static synchronized MessagePool getInstance() {
        return ourInstance;
    }

    private MessagePool() {
        messagePool = new ArrayList<>(20);
        for (int i = 0; i < 10; i++) {
            Account acc = new Account();
            acc.setNickName("Server");
            messagePool.add(i,  (M) new ClientMessage(acc, String.valueOf(i)));
        }
    }

    public int size() {
        return messagePool.size();
    }

    public synchronized void addMessage(M message) {
        messagePool.add(message);
    }

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
