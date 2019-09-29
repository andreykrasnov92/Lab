package model;

import entities.Group;
import entities.Student;
import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable {

    private static final long serialVersionUID = 1L;
    private final ArrayList<Group> groups;
    private final ArrayList<Student> students;

    public Model() {
        groups = new ArrayList<>();
        students = new ArrayList<>();
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
