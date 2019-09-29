package shared.util;

import entities.Group;
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
import org.w3c.dom.Node;
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

    public static Element convertStringToXML(String string) {
        try {
            InputSource inputSource = new InputSource(new StringReader(string));
            Document document = documentBuilder.parse(inputSource);
            return document.getDocumentElement();
        } catch (Exception ex) {
            printException(ex);
            return null;
        }
    }

    public static String convertXMLToString(Node node) {
        try {
            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            DOMSource domSource = new DOMSource(node);
            transformer.transform(domSource, streamResult);
            return stringWriter.toString();
        } catch (Exception ex) {
            printException(ex);
            return null;
        }
    }

    public static Element getGroupsXmlElement(List<Group> groups) {
        Document document = documentBuilder.newDocument();
        Element groupsElement = document.createElement("root");
        for (Group group : groups) {
            groupsElement.appendChild(getGroupXmlElement(document, group));
        }
        return groupsElement;
    }

    public static Element getStudentsXmlElement(List<Student> students) {
        Document document = documentBuilder.newDocument();
        Element studentsElement = document.createElement("root");
        for (Student student : students) {
            studentsElement.appendChild(getStudentXmlElement(document, student));
        }
        return studentsElement;
    }

    private static Element getGroupXmlElement(Document document, Group group) {
        Element element = document.createElement("group");
        element.setAttribute("groupid", String.valueOf(group.getGroupId()));
        element.setAttribute("groupname", group.getGroupName());
        return element;
    }

    private static Element getStudentXmlElement(Document document, Student student) {
        Element element = document.createElement("student");
        element.setAttribute("studentid", String.valueOf(student.getStudentId()));
        element.setAttribute("studentname", student.getStudentName());
        element.setAttribute("groupid", String.valueOf(student.getGroupId()));
        return element;
    }
}
