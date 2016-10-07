package Client;

import ServerSide.Message.ClientMessage;
import ServerSide.PropertiesInternetConnection;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by derkote on 17.09.2016.
 */
public class ConnectionToServer implements Runnable {
    private PropertiesInternetConnection properties;
    private Socket socket;
    private InputStream input;
    private OutputStream out;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Account account;

    public ConnectionToServer(Account account) {
        properties = new PropertiesInternetConnection();
        this.account = account;
    }

    @Override
    public void run() {
        //Создаем потоки ввода\вывода
        createStreams();
        System.out.println("Waiting for you message");
        //Читаем ввод пользователя


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Scanner in = new Scanner(System.in);
                    ClientMessage tempMessage;
                    if (in.hasNextLine()) {
                        tempMessage = new ClientMessage(account, in.nextLine());
                        sendMessage(tempMessage);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ClientMessage tempMessage = null;
                    try {
                        tempMessage = (ClientMessage) objIn.readObject();
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

    public void printMessage(ClientMessage message) {
        System.out.println(message.getNickname() + ": " + message.getMessage());
    }

    public void sendMessage(ClientMessage message) {
        try {
            objOut.writeObject(message);
            objOut.flush();
            System.out.println("Otpravleno!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ошибка потока вывода объекта");
        }
    }

    public ClientMessage getMessage() {
        ClientMessage temp;
        System.err.println("into getMessage");
        try {
            temp = (ClientMessage) objIn.readObject();
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
            socket = new Socket(properties.getInetAddress(), properties.getServerInternetPort());
            this.input = socket.getInputStream();
            this.out = socket.getOutputStream();

            this.objOut = new ObjectOutputStream(out);
            this.objIn = new ObjectInputStream(input);
        } catch (IOException e) {
            System.out.println("Потоки клиента не создались");
            e.printStackTrace();
        }
    }
}
