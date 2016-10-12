package ServerSide;

import ServerSide.Message.Message;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Управляет созданием новых подключений, их валидацией и добавлением в пул
 * @author derkote
 * @version 0.0.0.0.1
 */
public class SimpleServer {


    private ServerSocket socket;
    /** Количество подключенных клиентов / Кол-во соединений */
    private int countOfClients;
    /** id клиента */
    private int idid;
    /** Контейнер с параметрами */
    private Properties properties;
    /** Контейнер с соединениями*/
    private ConnectionPool connectionToClientMap;


    public static ConnectionToClient getConnectionById(int id) {
        ConnectionToClient result = null;
        try {

            result = (ConnectionToClient) SimpleServer.class.getField("connectionToClientMap").get(id);
        } catch (NoSuchFieldException e) {
            System.err.println("Dont find connectionToClientMap");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("Access denied");
            e.printStackTrace();
        }
        return result;
    }
    public Properties getProperties() {
        return properties;
    }


    /**
    * Конструктор класса
    * */
    public SimpleServer() throws IOException, JDOMException {
        idid = 0;
        countOfClients = 0;
        properties = new Properties();
        try {
            socket = new ServerSocket(properties.getInetPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionToClientMap = ConnectionPool.getInstance();
    }


    /**
    * Создает и добавляет в пул новое подключение
     * @return успех создания потока
     * @throws IOException ошибка создания соединения
     * @throws TooManyConnectionException количество подключенных пользователей ограниченно
    * */
    protected boolean addClient() throws IOException, TooManyConnectionException {
        System.err.println("addClient");
        idid++;
        ConnectionToClient<Message> tempConnection = new ConnectionToClient(socket, idid);
        if (tempConnection.isRunned()) {
            System.err.println("addClient - isRunned");
            if (connectionToClientMap.size() >= properties.getMaxConnection()) {
//                tempConnection.sendMessage("Нет свободных мест на сервере");
                tempConnection.setRunned(false);
                throw new TooManyConnectionException("Количество подключенных пользователей ограниченно: " + properties.getMaxConnection());
            }
            connectionToClientMap.put(tempConnection.getId(), tempConnection);
            countOfClients++;
            new Thread(tempConnection).start();
            return true;
        } else return false;
    }


    public void start() throws IOException {
        System.err.println("start");
        startAdderNewConnection();
    }

    @Deprecated
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

    @Deprecated
    private void killConnection(int id) {
        System.err.println("killConnection");
        connectionToClientMap.remove(id);
        countOfClients--;
    }


    /**
    * Добавляет подключения, если заканчиваются свободные
    * */
    private void startAdderNewConnection() {
        System.err.println("startAdder");
        try {
            addClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TooManyConnectionException e) {
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




}

/**
 * Возникает при попытке создания подключений больше разрешенного количества
 */
class TooManyConnectionException extends Exception {

    public TooManyConnectionException(String message) {
        super(message);
    }
}