package Client;

import ServerSide.Message.Message;
import ServerSide.Properties;
import org.jdom2.JDOMException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by derkote on 17.09.2016.
 */
public class ConnectionToServer implements Runnable {
    private Properties properties;
    private Socket socket;
    private InputStream input;
    private OutputStream out;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Account account;

    public ConnectionToServer(Account account) throws IOException, JDOMException {
        properties = new Properties();
        this.account = account;
    }

    @Override
    public void run() {
        //Создаем потоки ввода\вывода
        createStreams();

        /*try {
            objOut.writeObject(account);
            objOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("Waiting for you message");
        //Читаем ввод пользователя


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

    private void processingMessage(Message message) {
        switch (message.getMessageType()) {
            case SERVER:
                System.err.printf("Server message: %s", message.getMessage());
                break;
            case AUTH:
                break;
            case CLIENT:
                System.out.println(message.getNickname() + ": " + message.getMessage());
                break;
            case ERROR:
                System.err.println(message.getMessage());
                break;
            case SYSTEM:

                break;
        }
    }

    public void printMessage(Message message) {


        System.out.println(message.getNickname() + ": " + message.getMessage());
    }

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
