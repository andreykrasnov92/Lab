package client.model;

import client.entities.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Students implements Serializable {

    private static final long serialVersionUID = 123456789098760020L;

    @XmlElementWrapper
    @XmlElement
    private List<Student> students;

    public Students() {
        this.students = new ArrayList<>();
    }

    public Students(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return students;
    }
}
