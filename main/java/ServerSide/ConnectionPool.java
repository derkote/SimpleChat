package ServerSide;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by derkote on 04.10.2016.
 */
public class ConnectionPool{
    private Map<Integer, ConnectionToClient> connectionToClientMap;

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

    public void getMap() {
    }

    public ConnectionPool() {
        this.connectionToClientMap = new ConcurrentHashMap<>(10, 0.8f);
    }
}
