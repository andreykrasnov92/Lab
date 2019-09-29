package server.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import server.util.ObserverMessage;
import shared.database.entities.Gruppa;
import shared.database.entities.Student;
import shared.database.model.Groups;
import shared.database.model.Model;
import shared.database.model.Students;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import shared.util.Message;
import static shared.util.Utils.*;

public class DBController extends Observable {

    private static final String XML_DATA_FILE = "./xmltools/model.xml";
    private static final Set<Integer> LOCKED_GROUPS = new HashSet<>(), LOCKED_STUDENTS = new HashSet<>();

    private static JAXBContext context = null;

    static {
        try {
            context = JAXBContext.newInstance(Model.class);
        } catch (JAXBException ex) {
            printException(ex);
        }
    }

    public DBController() {
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
        clearChanged();
    }

    private void writeModelToXML(Model model) {
        try {
            OutputStream os = new FileOutputStream(XML_DATA_FILE);
            Marshaller m = context.createMarshaller();
            m.marshal(model, os);
            os.flush();
            os.close();
        } catch (FileNotFoundException | JAXBException ex) {
            printException(ex);
        } catch (IOException ex) {
            printException(ex);
        }
    }

    private Model readModelFromXML() {
        try {
            InputStream is = new FileInputStream(XML_DATA_FILE);
            Unmarshaller um = context.createUnmarshaller();
            Model model = (Model) um.unmarshal(is);
            is.close();
            return model;
        } catch (FileNotFoundException | JAXBException ex) {
            printException(ex);
        } catch (IOException ex) {
            printException(ex);
        }
        return new Model();
    }

    public void serializeModel(String filepath) throws IOException {
        Model model = readModelFromXML();
        try (ObjectOutputStream out
                = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(model);
            out.flush();
        }
    }

    public void deserializeModel(String filepath)
            throws ClassNotFoundException, IOException {
        Model model;
        try (ObjectInputStream in
                = new ObjectInputStream(new FileInputStream(filepath))) {
            model = (Model) in.readObject();
        }
        writeModelToXML(model);
    }

    public void createGroup(int groupId, String groupName, int clientId)
            throws PreexistingEntityException {
        Model model = readModelFromXML();
        Gruppa group = readGroup(model, groupId);
        if (group != null) {
            throw new PreexistingEntityException("Group with id " + groupId + " is already exists!");
        }
        group = new Gruppa(groupId, groupName);
        model.getGroups().add(group);
        writeModelToXML(model);
        notifyObservers(new ObserverMessage(Message.GroupCreated, clientId));
    }

    public void updateGroup(int groupId, String groupName, int clientId)
            throws DataUpdatingException, NonexistentEntityException {
        Model model = readModelFromXML();
        Gruppa group = readGroup(model, groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        unlockGroup(groupId);
        Collection<Gruppa> groups = model.getGroups();
        for (Gruppa group1 : groups) {
            if (group1.getGroupId() == groupId) {
                group1.setGroupId(groupId);
                group1.setGroupName(groupName);
                break;
            }
        }
        writeModelToXML(model);
        notifyObservers(new ObserverMessage(Message.GroupUpdated, clientId));
    }

    public void deleteGroup(int groupId, int clientId)
            throws IllegalOrphanException, NonexistentEntityException {
        Model model = readModelFromXML();
        Gruppa group = readGroup(model, groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        Students students = readStudents();
        for (Student student : students.getStudents()) {
            if (student.getGroupId() == groupId) {
                throw new IllegalOrphanException("This group has students and cannot be destroyed!");
            }
        }
        model.getGroups().remove(group);
        writeModelToXML(model);
        notifyObservers(new ObserverMessage(Message.GroupDeleted, clientId));
    }

    public Gruppa readGroup(int groupId) {
        return readGroup(readModelFromXML(), groupId);
    }

    public Gruppa readGroup(Model model, int groupId) {
        Collection<Gruppa> groups = model.getGroups();
        for (Gruppa group : groups) {
            if (group.getGroupId() == groupId) {
                return group;
            }
        }
        return null;
    }

    public Groups readGroups() {
        return new Groups(readModelFromXML().getGroups());
    }

    public int readGroupsCount() {
        return readModelFromXML().getGroups().size();
    }

    public Gruppa startGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException {
        Model model = readModelFromXML();
        Gruppa group = readGroup(model, groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        lockGroup(groupId);
        return group;
    }

    public void cancelGroupUpdating(int groupId)
            throws NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group != null) {
            try {
                unlockGroup(groupId);
            } catch (DataUpdatingException ex) {
            }
        }
    }

    public void createStudent(int studentId, String studentName, int groupId, int clientId)
            throws NonexistentEntityException, PreexistingEntityException {
        Model model = readModelFromXML();
        Student student = readStudent(model, studentId);
        if (student != null) {
            throw new PreexistingEntityException("Student with id " + studentId + " is already exists!");
        }
        Gruppa group = readGroup(model, groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        student = new Student(studentId, studentName, groupId);
        model.getStudents().add(student);
        writeModelToXML(model);
        notifyObservers(new ObserverMessage(Message.StudentCreated, clientId));
    }

    public void updateStudent(int studentId, String studentName, int groupId, int clientId)
            throws DataUpdatingException, NonexistentEntityException {
        Model model = readModelFromXML();
        Student student = readStudent(model, studentId);
        if (student == null) {
            throw new NonexistentEntityException("The student with id " + studentId + " no exists!");
        }
        Gruppa group = readGroup(model, groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        unlockStudent(studentId);
        Collection<Student> students = model.getStudents();
        for (Student student1 : students) {
            if (student1.getStudentId() == studentId) {
                student1.setStudentId(studentId);
                student1.setStudentName(studentName);
                student1.setGroupId(groupId);
                break;
            }
        }
        writeModelToXML(model);
        notifyObservers(new ObserverMessage(Message.StudentUpdated, clientId));
    }

    public void deleteStudent(int studentId, int clientId)
            throws NonexistentEntityException {
        Model model = readModelFromXML();
        Student student = readStudent(model, studentId);
        if (student == null) {
            throw new NonexistentEntityException("The student with id " + studentId + " no exists!");
        }
        model.getStudents().remove(student);
        writeModelToXML(model);
        notifyObservers(new ObserverMessage(Message.StudentDeleted, clientId));
    }

    public Student readStudent(int studentId) {
        return readStudent(readModelFromXML(), studentId);
    }

    public Student readStudent(Model model, int studentId) {
        Collection<Student> students = model.getStudents();
        for (Student student : students) {
            if (student.getStudentId() == studentId) {
                return student;
            }
        }
        return null;
    }

    public Students readStudents() {
        return new Students(readModelFromXML().getStudents());
    }

    public int readStudentsCount() {
        return readModelFromXML().getStudents().size();
    }

    public Student startStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException {
        Model model = readModelFromXML();
        Student student = readStudent(model, studentId);
        if (student == null) {
            throw new NonexistentEntityException("The student with id " + studentId + " no exists!");
        }
        lockStudent(studentId);
        return student;
    }

    public void cancelStudentUpdating(int studentId)
            throws NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student != null) {
            try {
                unlockStudent(studentId);
            } catch (DataUpdatingException e) {
            }
        }
    }

    private void lockGroup(int groupId) throws DataUpdatingException {
        if (LOCKED_GROUPS.contains(groupId)) {
            throw new DataUpdatingException("This group is updated now by another client!");
        } else {
            LOCKED_GROUPS.add(groupId);
        }
    }

    private void unlockGroup(int groupId) throws DataUpdatingException {
        if (LOCKED_GROUPS.contains(groupId)) {
            LOCKED_GROUPS.remove(groupId);
        } else {
            throw new DataUpdatingException("You cannot update group until you've locked it!");
        }
    }

    private void lockStudent(int studentId) throws DataUpdatingException {
        if (LOCKED_STUDENTS.contains(studentId)) {
            throw new DataUpdatingException("This student is updated now by another client!");
        } else {
            LOCKED_STUDENTS.add(studentId);
        }
    }

    private void unlockStudent(int studentId) throws DataUpdatingException {
        if (LOCKED_STUDENTS.contains(studentId)) {
            LOCKED_STUDENTS.remove(studentId);
        } else {
            throw new DataUpdatingException("You cannot update student until you've locked it!");
        }
    }
}
