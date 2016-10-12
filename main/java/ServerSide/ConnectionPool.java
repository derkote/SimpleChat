package ServerSide;

import ServerSide.Message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Синглтон
 * Контейнер хранит все текущие подключения клиентов
 * Следит за состоянием подключений и удаляет неактивные
 * Выполняет функцию почтового хаба, рассылая сообщения по всем клиентам
 * @autor derkote
 * @version 0.1
 */
public class ConnectionPool<M> {
    /** Коллекция всех текущих подключений */
    private volatile Map<Integer, ConnectionToClient> connectionToClientMap;
    private static ConnectionPool ourInstance = new ConnectionPool<Message>();

    public static ConnectionPool getInstance() {
        return ourInstance;
    }

    /**
     * Добавляет подключение в контейнер, возвращает успешность выполнения
     * @param id id подключения
     * @param connetion подключение*/
    public synchronized boolean put(int id, ConnectionToClient connetion) {
        connectionToClientMap.put(id, connetion);
        return connectionToClientMap.containsKey(id);
    }

    /**
     * Возвращает подключение по идентификатору
     * @param id идентификатор требуемого подключения */
    public ConnectionToClient get(int id) {
        if (connectionToClientMap.containsKey(id))
            return connectionToClientMap.get(id);
        else return null;
    }

    /**
     * Удаляет подключение из контейнера, возвращает успешность выполнения
     * @param id идентификатор удаляемого подключения */
    public synchronized boolean remove(int id) {
        if (connectionToClientMap.containsKey(id))
            connectionToClientMap.remove(id);
        if (!connectionToClientMap.containsKey(id))
            return true;
        else return false;
    }

    public int size() {
        return connectionToClientMap.size();
    }

    /**
     * Инициализируем колелкцию в которой храним подключения
     * TODO: протестировать различные значения loadfactor'a
     * Запускаем убийцу невалидных потоков
     */
    private ConnectionPool() {
        this.connectionToClientMap = new ConcurrentHashMap<>(10, 0.8f);
        new ConnectionKiller();
    }

    /**
     * Рассылает сообщение по всем поключениям в пуле
     * @param message сообщение которое необходимо разослать
     */
    public synchronized void sendMessageToAll(M message) {
        System.out.println(connectionToClientMap.size());
        for (ConnectionToClient client : connectionToClientMap.values()) {
            System.out.println(client);
            client.sendMessage(message);

        }

    }


    /**
     * Отдельный поток следит за валидностью подключений в пуле
     * Если подключение не работает, то удаляет его из пула
     */
    class ConnectionKiller {
        public ConnectionKiller() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        for (Map.Entry<Integer, ConnectionToClient> integerConnectionToClientEntry : connectionToClientMap.entrySet()) {
                            if (!integerConnectionToClientEntry.getValue().isRunned()) {
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

