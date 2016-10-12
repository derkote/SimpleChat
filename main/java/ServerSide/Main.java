package ServerSide;


import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by derkote on 16.09.2016.
 */
public class Main {

    public static void main(String[] args) {
        SimpleServer server = null;
        try {
            server = new SimpleServer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        server.toString();

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
