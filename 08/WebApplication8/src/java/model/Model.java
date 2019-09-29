package model;

import entities.Group;
import entities.Student;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "university")
public class Model implements Serializable {

    @XmlTransient
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "group", required = false)
    private final List<Group> groups;
    @XmlElement(name = "student", required = false)
    private final List<Student> students;

    public Model() {
        groups = new ArrayList<>();
        students = new ArrayList<>();
    }

    public Model(List<Group> groups, List<Student> students) {
        this.groups = groups;
        this.students = students;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Student> getStudents() {
        return students;
    }
}
