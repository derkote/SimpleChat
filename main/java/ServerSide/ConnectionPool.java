package ServerSide;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by derkote on 04.10.2016.
 */
public class ConnectionPool{
    private Map<Integer, ConnectionToClient> connectionToClientMap;
    private static ConnectionPool ourInstance = new ConnectionPool();

    public static ConnectionPool getInstance() {
        return ourInstance;
    }

    public boolean put(int id, ConnectionToClient connetion) {
        connectionToClientMap.put(id, connetion);
        return connectionToClientMap.containsKey(id);
    }

    public ConnectionToClient get(int id) {
        if (connectionToClientMap.containsKey(id))
            return connectionToClientMap.get(id);
        else return null;
    }

    public boolean remove(int id) {
        if (connectionToClientMap.containsKey(id))
            connectionToClientMap.remove(id);
        if (!connectionToClientMap.containsKey(id))
            return true;
        else return false;
    }

    public int size() {
        return connectionToClientMap.size();
    }

    private ConnectionPool() {
        this.connectionToClientMap = new ConcurrentHashMap<>(10, 0.8f);
        new ConnectionKiller();
    }

    public void sendMessageToAll(String message) {
        for (ConnectionToClient client : connectionToClientMap.values()) {System.out.println("Into iter");
            client.sendMessage(message);

        }

    }



    class ConnectionKiller {
        public ConnectionKiller() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        for (Map.Entry<Integer, ConnectionToClient> integerConnectionToClientEntry : connectionToClientMap.entrySet()) {
                            if(!integerConnectionToClientEntry.getValue().isRunned()) {
                                System.out.println("killing situation");
                                remove(integerConnectionToClientEntry.getKey());
                            }

                        }
//                    System.out.println("killer is worked!!");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }




}

