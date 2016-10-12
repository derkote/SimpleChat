package Client;

import ServerSide.Message.Message;
import ServerSide.Properties;
import org.jdom2.JDOMException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Подключение к серверу
 * Отправляет на сервер сообщения
 * Принимает сообщения от сервера
 * @author derkote
 * @version 0.1
 */
public class ConnectionToServer implements Runnable {
    /** Контейнер с параметрами */
    private Properties properties;
    /** Через него поднимаем соединение */
    private Socket socket;
    private InputStream input;
    private OutputStream out;
    /** Поток ввода сообщенийц */
    private ObjectInputStream objIn;
    /** Поток вывода сообщений */
    private ObjectOutputStream objOut;
    /** Информация об отправителе */
    private Account account;


    public ConnectionToServer(Account account) throws IOException, JDOMException {
        properties = new Properties();
        this.account = account;
    }

    @Override
    public void run() {
        /** Создаем потоки ввода-вывода */
        createStreams();
        /** TODO: Реализовать отправку информации об отправителе один раз!  */
        /*try {
            objOut.writeObject(account);
            objOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("Waiting for you message");
        //Читаем ввод пользователя


        /** Поток считывает сообщения и отправляет на сервер */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Scanner in = new Scanner(System.in);
                    Message tempMessage;
                    if (in.hasNextLine()) {
                        tempMessage = new Message(account, in.nextLine(), Message.MessageType.CLIENT);
                        sendMessage(tempMessage);
                    }
                }
            }
        }).start();

        /**
         * Поток принимает сообщения с сервера
         * Выводит их */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Message tempMessage = null;
                    try {
                        tempMessage = (Message) objIn.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    printMessage(tempMessage);
                }
            }
        }).start();

    }

    /**
     * Обработка сообщений в зависимости от типа
     * @param message отрабатываемое сообщение
     * TODO: реализовать механизм типов сообщений после обновления на стороне сервера
     * */
    private void processingMessage(Message message) {
        switch (message.getMessageType()) {
            /** Серверное */
            case SERVER:
                System.err.printf("Server message: %s", message.getMessage());
                break;
            /** Авторизиция*/
            case AUTH:
                break;
            /** Клиентское */
            case CLIENT:
                printMessage(message);
                break;
            /** Ошибка */
            case ERROR:
                System.err.println(message.getMessage());
                break;
            /** Системное */
            case SYSTEM:
                break;
        }
    }

    /**
     * Выводит сообщение в консоль
     * @param message выводимое сообщение
     */
    public void printMessage(Message message) {
        System.out.println(message.getNickname() + ": " + message.getMessage());
    }

    /**
     * Отправляет сообщение на сервер
     * @param message отправляемое сообщение
     */
    public void sendMessage(Message message) {
        try {
            objOut.writeObject(message);
            objOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ошибка потока вывода объекта");
        }
    }


    public Message getMessage() {
        Message temp;
        try {
            temp = (Message) objIn.readObject();
            return temp;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ошибочка с созданием потока чтения объекта");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("пришли не валидные данные");
        }
        return null;
    }


    /**
     * Создает потоки ввода-вывода
     */
    private void createStreams() {
        try {
            socket = new Socket(properties.getInetAddress(), properties.getInetPort());
            this.input = socket.getInputStream();
            this.out = socket.getOutputStream();

            this.objOut = new ObjectOutputStream(out);
            this.objIn = new ObjectInputStream(input);
        } catch (IOException e) {
            System.err.println("Потоки клиента не создались");
            e.printStackTrace();
        }
    }
}
