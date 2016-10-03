package Client;

import ServerSide.ConnectionToClient;
import ServerSide.PropertiesInternetConnection;
import com.sun.org.apache.xpath.internal.SourceTree;

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
    private volatile String message;

    public ConnectionToServer() {
        properties = new PropertiesInternetConnection();
//        createStreams();

    }

    @Override
    public void run() {
        createStreams();

        System.out.println(getMessage());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner(System.in);

        while (true) {
            if (in.hasNextLine())
            sendMessage(in.nextLine());
//            message = getMessage();
            try{
                Thread.sleep(20);
            }catch (Exception e) {
                System.err.println("Не может заснуть поток");
                e.printStackTrace();
            }
//            sendMessage(message);

        }

    }

    public void sendMessage(String message) {
//        Writer writer = new OutputStreamWriter(System.out);
        PrintWriter writer = new PrintWriter(out, true);
        writer.println(message);

    }

    public String getMessage() {
        Scanner r = new Scanner(input);
        StringBuilder result = new StringBuilder();
        if (r.hasNextLine()) {
            result.append(r.nextLine());
            try{
                Thread.sleep(20);
            }catch (Exception e) {
                System.err.println("Не может заснуть поток");
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    private void createStreams() {
        try{
            socket  = new Socket(properties.getInetAddress(), properties.getServerInternetPort());
            this.input = socket.getInputStream();
            this.out = socket.getOutputStream();
            System.out.println("Потоки клиента создались успешно");
        } catch (IOException e) {
            System.out.println("Потоки клиента не создались");
            e.printStackTrace();

        }
    }



}
