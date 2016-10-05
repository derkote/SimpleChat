package ServerSide;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

/**
 * Created by derkote on 05.10.2016.
 */
public class MessagePool {
    private ArrayList<String> messagePool;

    private static MessagePool ourInstance = new MessagePool();

    public static MessagePool getInstance() {
        return ourInstance;
    }

    private MessagePool() {
        messagePool = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            messagePool.add(Integer.toString(i));
        }
    }

    public void addMessage(String message) {
        messagePool.add(message);
    }

    public String[] getLast() {
        String[] temp = new String[10];
        int length = messagePool.size();
        System.out.println(messagePool.size());
        for (int i = 10; i > 0; i--) {
            temp[10-i] = messagePool.get(length-i);
        }
        return temp;
    }

}
