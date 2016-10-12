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
 * Created by derkote on 16.09.2016.
 */
public class Properties {

    private File propertyFile = new File(String.valueOf(getClass().getClassLoader().getResource("property.xml")));
    private Map<PropertyName, Object> propertyContainer;
    private int serverInternetPort;
    private int maxConnection;
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
    public enum PropertyName {
        INETADRESS,
        INETPORT,
        MAXCONNECTION
    }


    public void reloadProperty() throws JDOMException, IOException {
        propertyContainer = loadFromXMLFile(propertyFile);
        loadProperties();
    }


    public Properties() throws IOException, JDOMException {
        System.out.println(getClass().getClassLoader().getResource("property.xml"));
        propertyContainer = loadFromXMLFile(propertyFile);
        loadProperties();

    }

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

    private void loadProperties() throws UnknownHostException {
        serverInternetPort =  Integer.parseInt((String)propertyContainer.get(PropertyName.INETPORT));
        inetAddress = InetAddress.getByName((String) propertyContainer.get(PropertyName.INETADRESS));
        maxConnection = Integer.parseInt((String)propertyContainer.get(PropertyName.MAXCONNECTION));
    }



}
