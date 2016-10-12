package ServerSide;

import org.jdom2.JDOMException;
import java.io.IOException;

/**
 * Серверная часть простого текстового консольного чата
 * Кодовое имя ГадкийIRC
 * Точка входа
 * @author derkote
 * @version 0.1
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
