package ServerSide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by derkote on 16.09.2016.
 */
public class ConnectionToClient implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;
    private int id;
    private boolean runned = false;
    private InputStream inputStream;
    private OutputStream outputStream;

    public int getId() {
        return id;
    }
    public boolean isRunned() {
        return runned;
    }

    public ConnectionToClient(ServerSocket serverSocket, int id) {
        this.serverSocket = serverSocket;
        this.id = id + 1;
        try {
            socket = this.serverSocket.accept();
            System.err.println("Create new connection id:" + id);
            runned = true;
        }catch (IOException e) {

        }
    } 
    
    @Override
    public void run() {

        System.err.println("get Message" + " " + socket);
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            Scanner inputScanner = new Scanner(inputStream);
            PrintWriter outputWriter = new PrintWriter(outputStream, true);

            outputWriter.println("Hi! Please enter a message. Enter 'exit' to exit");

                /*
                * Избавляемся от "мусорной" информации
                * */
            byte[] b = new byte[100];
            inputStream.read(b);
            System.out.println(b);

            boolean isMessagetail = false;
            String tempMessage = "";
            while (!isMessagetail && inputScanner.hasNextLine()) {
                tempMessage = inputScanner.nextLine();

                System.out.println("Echo: " + tempMessage);
//                outputWriter.println("Echo: " + tempMessage);
//                отправляем не одному в ответ, а всем
                if (tempMessage.trim().equalsIgnoreCase("exit")) {
                    isMessagetail = true;
                    runned = false;
                }
                ConnectionPool.getInstance().sendMessageToAll("Echo: " + tempMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println("Connection with client are closed");
        }
    }

    public void sendMessage(String message) {
        PrintWriter outputWriter = new PrintWriter(outputStream, true);
        outputWriter.println(message);
    }
}
