package server.util;

import entities.Gruppa;
import entities.Student;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import static shared.util.Utils.*;

public class XmlUtils {

    private static DocumentBuilder documentBuilder;
    private static Transformer transformer;

    static {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static Element convertUtfToXmlElement(String string) {
        try {
            InputSource inputSource = new InputSource(new StringReader(string));
            Document document;
            synchronized (documentBuilder) {
                document = documentBuilder.parse(inputSource);
            }
            return document.getDocumentElement();
        } catch (Exception ex) {
            printException(ex);
            return null;
        }
    }

    public static Element convertIntegerToXmlElement(int integerData) {
        Document document = createDocument();
        Element element = document.createElement("root");
        element.setAttribute("integerdata", Integer.toString(integerData));
        return element;
    }

    public static Element convertStringToXmlElement(String stringData) {
        Document document = createDocument();
        Element element = document.createElement("root");
        element.setAttribute("stringdata", stringData);
        return element;
    }

    public static Element convertGroupToXmlElement(Gruppa group) {
        Document document = createDocument();
        return convertGroupToXmlElement(document, group);
    }

    public static Element convertStudentToXmlElement(Student student) {
        Document document = createDocument();
        return convertStudentToXmlElement(document, student);
    }

    public static Element convertGroupsToXmlElement(List<Gruppa> groups) {
        Document document = createDocument();
        Element groupsElement = document.createElement("root");
        for (Gruppa group : groups) {
            groupsElement.appendChild(convertGroupToXmlElement(document, group));
        }
        return groupsElement;
    }

    public static Element convertStudentsToXmlElement(List<Student> students) {
        Document document = createDocument();
        Element studentsElement = document.createElement("root");
        for (Student student : students) {
            studentsElement.appendChild(convertStudentToXmlElement(document, student));
        }
        return studentsElement;
    }

    public static String convertXmlElementToUtf(Element element) {
        try {
            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            DOMSource domSource = new DOMSource(element);
            transformer.transform(domSource, streamResult);
            return stringWriter.toString();
        } catch (Exception ex) {
            printException(ex);
            return null;
        }
    }

    public static String convertXmlElementToString(Element element) {
        String stringData = element.getAttribute("stringdata");
        return stringData;
    }

    private static Element convertGroupToXmlElement(Document document, Gruppa group) {
        Element element = document.createElement("group");
        element.setAttribute("groupid", String.valueOf(group.getGroupid()));
        element.setAttribute("groupname", group.getGroupname());
        return element;
    }

    private static Element convertStudentToXmlElement(Document document, Student student) {
        Element element = document.createElement("student");
        element.setAttribute("studentid", String.valueOf(student.getStudentid()));
        element.setAttribute("studentname", student.getStudentname());
        element.setAttribute("groupid", String.valueOf(student.getGroupid().getGroupid()));
        return element;
    }

    private static Document createDocument() {
        Document document;
        synchronized (documentBuilder) {
            document = documentBuilder.newDocument();
        }
        return document;
    }
}
