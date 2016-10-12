package ServerSide;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранит необходимые параметры
 * @author derkote
 * @version 0.1
 */
public class Properties {

    /** Файл с настройками */
    private File propertyFile = new File(String.valueOf(getClass().getClassLoader().getResource("property.xml")));
    /** Контейнер с именами значений и значениями*/
    private Map<PropertyName, Object> propertyContainer;
    /** Порт для подключения*/
    private int serverInternetPort;
    /** Максимальное количество одновременных соединений*/
    private int maxConnection;
    /** IP адрес сервера*/
    private InetAddress inetAddress;


    public int getMaxConnection() {
        return maxConnection;
    }
    public int getInetPort() {
        return serverInternetPort;
    }
    public InetAddress getInetAddress() {
        return inetAddress;
    }
    /**
     * Виды параметров
     * INETADRESS    - ip сервера
     * INETPORT      - порт
     * MAXCONNECTION - кол-во одновременных подключений
    * */
    public enum PropertyName {
        INETADRESS,
        INETPORT,
        MAXCONNECTION
    }


    /**
     * Конструктор класса
     * Читает значение из xml и распихивает по полям класса
     *
     * @throws JDOMException ошибка парсинга XML из файла
     * @throws IOException ошибки чтения файла
    * */
    public Properties() throws IOException, JDOMException {
        System.out.println(getClass().getClassLoader().getResource("property.xml"));
        propertyContainer = loadFromXMLFile(propertyFile);
        loadProperties();
    }

    /**
     * Читает значения из файла, загружает в контейнер и возвращает его
     * @param file файл из которого грузим настройки
     * @throws JDOMException ошибка парсинга XML из файла
     * @throws IOException ошибки чтения файла
     * */
    private Map<PropertyName, Object> loadFromXMLFile(File file) throws JDOMException, IOException {
        SAXBuilder jdomBuilder = new SAXBuilder();
        Document jdomDocument = jdomBuilder.build(file.getPath());
        Element xmlDoc = jdomDocument.getRootElement();
        List<Element> property = xmlDoc.getChildren();
        Map<PropertyName, Object> tempContainer = new HashMap<>();
        for (Element element : property) {
            tempContainer.put(PropertyName.valueOf(element.getName()), element.getAttribute("value").getValue());
        }
        return tempContainer;
    }


    /**
     * Записывает значения настроек из контейнера в поля класса
     * @throws UnknownHostException значение INETADRESS загруженное из файла не может быть распознано
     */
    private void loadProperties() throws UnknownHostException {
        serverInternetPort =  Integer.parseInt((String)propertyContainer.get(PropertyName.INETPORT));
        inetAddress = InetAddress.getByName((String) propertyContainer.get(PropertyName.INETADRESS));
        maxConnection = Integer.parseInt((String)propertyContainer.get(PropertyName.MAXCONNECTION));
    }



}
