package ServerSide;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by derkote on 16.09.2016.
 */
public class SimpleServer {

    private ServerSocket socket;
    private int countOfClients;
    private int idid;
    private PropertiesInternetConnection properties;
    private ConnectionPool connectionToClientMap;

    public static ConnectionToClient getConnectionById(int id) {
        ConnectionToClient result = null;
        try {

            result = (ConnectionToClient) SimpleServer.class.getField("connectionToClientMap").get(id);
        }catch (NoSuchFieldException e) {
            System.err.println("Dont find connectionToClientMap");
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            System.err.println("Access denied");
            e.printStackTrace();
        }
        return result;
    }

    public PropertiesInternetConnection getProperties() {
        return properties;
    }

    public SimpleServer() {
        idid = 0;
        countOfClients = 0;
        properties = new PropertiesInternetConnection();
        try {
            socket = new ServerSocket(properties.getServerInternetPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionToClientMap = ConnectionPool.getInstance();
    }

    protected boolean addClient() throws IOException {
        System.err.println("addClient");
        idid++;
        ConnectionToClient tempConnection = new ConnectionToClient(socket, idid);
        System.out.println(tempConnection);
        if (tempConnection.isRunned()) {
            System.err.println("addClient - isRunned");
            connectionToClientMap.put(tempConnection.getId(), tempConnection);
            countOfClients++;
            new Thread(tempConnection).start();
            return true;
        }else return false;
    }

    public void start() throws IOException {
        System.err.println("start");
        //startKillerDeadConnectionToClient();
        startAdderNewConnection();
    }


    private void startKillerDeadConnectionToClient() {
        System.err.println("startKiller");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < connectionToClientMap.size(); i++) {

                    }
                    /*for (Map.Entry<Integer, ConnectionToClient> integerConnectionToClientEntry : connectionToClientMap.entrySet()) {
                        if(!integerConnectionToClientEntry.getValue().isRunned())
                            System.out.println("killing situation");
                            killConnection(integerConnectionToClientEntry.getKey());
                    }
//                    System.out.println("killer is worked!!");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
*/
                }
            }
        }).start();
    }

    private void killConnection(int id) {
        System.err.println("killConnection");
        connectionToClientMap.remove(id);
        countOfClients--;
    }

    private void startAdderNewConnection() {
        System.err.println("startAdder");
        try {
            addClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            if (countOfClients == connectionToClientMap.size()) {
                try {
                    addClient();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("adder error 2");
                }
            }
        }
    }

    /*public void sendMessageById(int id, String message) {
        ConnectionToClient c = getConnectionById(id);
        c.sendMessage(message);
    }*/

}

