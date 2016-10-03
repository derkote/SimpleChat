package ServerSide;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by derkote on 16.09.2016.
 */
public class PropertiesInternetConnection {

    private int serverInternetPort = 8189;
    private InetAddress inetAddress;

    public int getServerInternetPort() {
        return serverInternetPort;
    }

    public void setServerInternetPort(int serverInternetPort) {
        serverInternetPort = serverInternetPort;
    }

    public PropertiesInternetConnection() {
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }
}
