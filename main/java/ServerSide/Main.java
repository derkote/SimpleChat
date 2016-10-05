package ServerSide;


import java.io.IOException;
import java.util.Scanner;

/**
 * Created by derkote on 16.09.2016.
 */
public class Main {

    public static void main(String[] args) {
        SimpleServer server = new SimpleServer();
        server.toString();

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
