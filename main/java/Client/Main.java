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
        Account account = new Account();
        Scanner in = new Scanner(System.in);
        System.out.println("Введите ваш ник!");
        account.setNickName(in.nextLine());

        ConnectionToServer c = new ConnectionToServer(account);

        try {
            c.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
