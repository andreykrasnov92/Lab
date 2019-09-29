package client.model;

import client.entities.Gruppa;
import client.entities.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "university")
@XmlAccessorType(XmlAccessType.NONE)
public class Model implements Serializable {

    private static final long serialVersionUID = 123456789098760100L;

    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    private final List<Gruppa> groups;
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    private final List<Student> students;

    public Model() {
        groups = new ArrayList<>();
        students = new ArrayList<>();
    }

    public Model(List<Gruppa> groups, List<Student> students) {
        this.groups = groups;
        this.students = students;
    }

    public List<Gruppa> getGroups() {
        return groups;
    }

    public List<Student> getStudents() {
        return students;
    }
}
