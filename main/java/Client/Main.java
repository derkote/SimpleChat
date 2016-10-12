package Client;

import org.jdom2.JDOMException;

import java.io.IOException;
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
        System.out.println("Введите ваш пароль!");
        account.setPassword(in.nextLine());

        ConnectionToServer c = null;
        try {
            c = new ConnectionToServer(account);
            c.run();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
