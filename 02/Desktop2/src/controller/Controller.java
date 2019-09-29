package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import entities.Group;
import entities.Student;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.Model;

public class Controller {

    private Model model;

    public static final int GROUP_COLUMNS_COUNT = 2;
    public static final String[] GROUP_COLUMNS_NAMES = {
        "Group ID", "Group name"
    };
    public static final String NO_GROUPS = "No groups in database!";
    public static final int STUDENT_COLUMNS_COUNT = 3;
    public static final String[] STUDENT_COLUMNS_NAMES = {
        "Student ID", "Student name", "Group ID"
    };
    public static final String NO_STUDENTS = "No students in database!";

    private Controller() {
        model = new Model();
    }

    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    public void serializeModel(String filepath) throws IOException {
        try (ObjectOutputStream out
                = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(model);
        }
    }

    public void deserializeModel(String filepath)
            throws ClassNotFoundException, IOException {
        try (ObjectInputStream in
                = new ObjectInputStream(new FileInputStream(filepath))) {
            model = (Model) in.readObject();
        }
    }

    public void createGroup(int groupId, String groupName)
            throws PreexistingEntityException {
        if (readGroup(groupId) != null) {
            throw new PreexistingEntityException(
                    "Group with id " + groupId + " is already exists.");
        }
        Group group = new Group(groupId, groupName);
        model.getGroups().add(group);
    }

    public Group readGroup(int groupId) {
        Collection<Group> groups = model.getGroups();
        for (Group group : groups) {
            if (group.getGroupId() == groupId) {
                return group;
            }
        }
        return null;
    }

    public void updateGroup(int groupId, String groupName)
            throws NonexistentEntityException {
        Group group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        if (groupName != null) {
            group.setGroupName(groupName);
        }
    }

    public void deleteGroup(int groupId)
            throws IllegalOrphanException, NonexistentEntityException {
        Group group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        List<String> illegalOrphanMessages = null;
        Collection<Student> studentsCollectionOrphanCheck = group.getStudentsCollection();
        if (studentsCollectionOrphanCheck != null) {
            for (Student studentsCollectionOrphanCheckStudent
                    : studentsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add(
                        "This group cannot be destroyed since the student "
                        + studentsCollectionOrphanCheckStudent
                        + " in its collection has a non-nullable groupID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
        }
        model.getGroups().remove(group);
    }

    public ArrayList<Group> readGroups() {
        return model.getGroups();
    }

    public int readGroupsCount() {
        return model.getGroups().size();
    }

    public void createStudent(int studentId, String studentName, int groupId)
            throws NonexistentEntityException, PreexistingEntityException {
        if (readStudent(studentId) != null) {
            throw new PreexistingEntityException(
                    "Student with id " + studentId + " is already exists.");
        }
        Group group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        Student student = new Student(studentId, studentName);
        student.setGroup(group);
        group.getStudentsCollection().add(student);
        model.getStudents().add(student);
    }

    public Student readStudent(int studentId) {
        Collection<Student> students = model.getStudents();
        for (Student student : students) {
            if (student.getStudentId() == studentId) {
                return student;
            }
        }
        return null;
    }

    public void updateStudent(int studentId, String studentName, int groupId)
            throws NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        Group group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        student.setStudentName(studentName);
        student.setGroup(group);
    }

    public void deleteStudent(int studentId)
            throws NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        model.getStudents().remove(student);
    }

    public ArrayList<Student> readStudents() {
        return model.getStudents();
    }

    public int readStudentsCount() {
        return model.getStudents().size();
    }

    private static class ControllerHolder {

        private static final Controller INSTANCE = new Controller();
    }
}
