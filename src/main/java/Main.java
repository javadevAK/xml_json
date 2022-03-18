import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String fileName = "data.xml";

        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);

        writeString(json);

    }

    private static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));

        Node root = doc.getDocumentElement();

        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (node_.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            NodeList childNode = node_.getChildNodes();
            long id = 0;
            String firstName = "";
            String lastName = "";
            String country = "";
            int age = 0;

            for (int j = 0; j < childNode.getLength(); j++) {
                Node childNode_ = childNode.item(j);
                if (childNode_.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                switch (childNode.item(j).getNodeName()) {
                    case "id" -> {
                        id = Long.parseLong(childNode.item(j).getTextContent());
                    }
                    case "firstName" -> {
                        firstName = childNode.item(j).getTextContent();
                    }
                    case "lastName" -> {
                        lastName = childNode.item(j).getTextContent();
                    }
                    case "country" -> {
                        country = childNode.item(j).getTextContent();
                    }
                    case "age" -> {
                        age = Integer.parseInt(childNode.item(j).getTextContent());
                    }
                }
            }
            Employee employee = new Employee(id, firstName, lastName, country, age);
            list.add(employee);
        }
        return list;
    }

    private static void writeString(String json) {

        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }
}


