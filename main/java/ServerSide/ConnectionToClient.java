package ServerSide;

import Client.Account;
import ServerSide.Message.Message;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by derkote on 16.09.2016.
 */
public class ConnectionToClient<M> implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;
    private int id;
    private boolean runned = false;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private MessagePool messagePool;

    public int getId() {
        return id;
    }

    public boolean isRunned() {
        return runned;
    }

    public void setRunned(boolean set) {
        runned = set;
    }

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
            System.out.println("Отправляем последние десять сообщений");
            ArrayList<M> lastTenMessage = messagePool.getLast();
            for (int i = 0; i < lastTenMessage.size(); i++) {
                sendMessage(lastTenMessage.get(i));
            }
            Account acc = new Account();
            acc.setNickName("Server");
            sendMessage((M) new Message(acc, "Hi! Please enter a message. Enter 'exit' to exit"));

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
