package ServerSide;

import Client.Account;
import ServerSide.Message.Message;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Подключение к клиенту
 * Каждое в отдельном потоке
 * @author derkote
 * @version 0.1
 */
public class ConnectionToClient<M> implements Runnable {
    private ServerSocket serverSocket;
    /** Сокет на который подключается клиент */
    private Socket socket;
    private int id;
    /** Состояние соединения */
    private boolean runned = false;
    /** Поток ввода объекта */
    private ObjectInputStream inputStream;
    /** Поток вывода объекта */
    private ObjectOutputStream outputStream;
    /** Контейнер хранит список сообщений всех клиентов */
    private MessagePool messagePool;

    public int getId() {
        return id;
    }

    /**
     * @return состояние соединения */

    public boolean isRunned() {
        return runned;
    }
    /**
     * @param set устанавливаемое состояние соединения */
    public void setRunned(boolean set) {
        runned = set;
    }

    /**
     * @param id id подключения */
    public ConnectionToClient(ServerSocket serverSocket, int id) {
        this.serverSocket = serverSocket;
        this.messagePool = MessagePool.getInstance();
        this.id = id + 1;
        try {

            socket = this.serverSocket.accept();
            System.err.println("Create new connection id:" + id);
            runned = true;
        } catch (IOException e) {

        }
    }

    /**
     * Создаем потоки ввода-вывода, отправляем историю сообщений,
     * отправляем инструкцию пользователя,
     * принимаем сообщения и рассылаем их остальным клиентам
     * */
    @Override
    public void run() {

        System.err.println("get Message" + " " + socket);
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            try {
                inputStream = new ObjectInputStream(is);
                outputStream = new ObjectOutputStream(os);
            } catch (EOFException e) {
                e.printStackTrace();
            }
            /**  последние десять сообщений */
            ArrayList<M> lastTenMessage = messagePool.getLast();
            for (int i = 0; i < lastTenMessage.size(); i++) {
                sendMessage(lastTenMessage.get(i));
            }

            /** инструкция */
            Account acc = new Account();
            acc.setNickName("Server");
            sendMessage((M) new Message(acc, "Hi! Please enter a message. Enter 'exit' to exit"));

            /** проверяем не выходит ли клиент */
            boolean isMessagetail = false;
            Message tempMessage = null;
            while (!isMessagetail) {
                try {
                    tempMessage = (Message) inputStream.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (tempMessage.getMessage().trim().equalsIgnoreCase("exit")) {
                    System.out.println("Приняли команду на выход");
                    isMessagetail = true;
                    runned = false;
                }
                System.out.println(tempMessage.getNickname() + ": " + tempMessage);
                messagePool.addMessage(tempMessage);
                /** пересылаем сообщения другим клиентам */
                tempMessage.setMessage(tempMessage.getMessage());
                ConnectionPool.getInstance().sendMessageToAll(tempMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            runned = false;
        }
    }

    public void sendMessage(String message) {
        PrintWriter outputWriter = new PrintWriter(outputStream, true);
        outputWriter.println(message);
    }

    /**
     * Отправляем сообщение клиенту
     * @param message сообщение типа М*/
    public void sendMessage(M message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Не можем отправить сообщение");
        }
    }
}
