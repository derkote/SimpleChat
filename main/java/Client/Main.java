package Client;

import ServerSide.ConnectionToClient;
import ServerSide.PropertiesInternetConnection;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by derkote on 16.09.2016.
 */
public class Main {


    public static void main(String[] args) {

        ConnectionToServer c = new ConnectionToServer();
        try {
            c.run();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
