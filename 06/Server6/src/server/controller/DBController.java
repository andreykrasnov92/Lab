package server.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Locale;
import java.util.Observable;
import java.util.Set;
import java.util.TimeZone;
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

public class DBController extends Observable {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE", USER = "system", PASSWORD = "12345";
    private static final Set<Integer> LOCKED_GROUPS = new HashSet<>(), LOCKED_STUDENTS = new HashSet<>();

    static {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
    }

    public DBController() {
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
        clearChanged();
    }

    public void serializeModel(String filepath)
            throws IOException, SQLException {
        Model model = new Model(readGroups().getGroups(), readStudents().getStudents());
        try (ObjectOutputStream out
                = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(model);
            out.flush();
        }
    }

    public void deserializeModel(String filepath)
            throws ClassNotFoundException, IOException, SQLException {
        Model model;
        try (ObjectInputStream in
                = new ObjectInputStream(new FileInputStream(filepath))) {
            model = (Model) in.readObject();
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stat = conn.createStatement();
        stat.executeUpdate("delete from gruppa");
        stat.executeUpdate("delete from student");
        PreparedStatement pstat = null;
        for (Gruppa group : model.getGroups()) {
            pstat = conn.prepareStatement("insert into gruppa values(?, ?)");
            pstat.setInt(1, group.getGroupId());
            pstat.setString(2, group.getGroupName());
            pstat.executeUpdate();
        }
        for (Student student : model.getStudents()) {
            pstat = conn.prepareStatement("insert into student values(?, ?, ?)");
            pstat.setInt(1, student.getStudentId());
            pstat.setString(2, student.getStudentName());
            pstat.setInt(3, student.getGroupId());
            pstat.executeUpdate();
        }
        if (pstat != null) {
            pstat.close();
        }
        stat.close();
        conn.close();
    }

    public void createGroup(int groupId, String groupName, int clientId)
            throws PreexistingEntityException, SQLException {
        Gruppa group = readGroup(groupId);
        if (group != null) {
            throw new PreexistingEntityException("Group with id " + groupId + " is already exists!");
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat;
        pstat = conn.prepareStatement("insert into gruppa values(?, ?)");
        pstat.setInt(1, groupId);
        pstat.setString(2, groupName);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
        notifyObservers(new ObserverMessage(Message.GroupCreated, clientId));
    }

    public void updateGroup(int groupId, String groupName, int clientId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        unlockGroup(groupId);
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement("update gruppa set groupname = ? where groupid = ?");
        pstat.setString(1, groupName);
        pstat.setInt(2, groupId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
        notifyObservers(new ObserverMessage(Message.GroupUpdated, clientId));
    }

    public void deleteGroup(int groupId, int clientId)
            throws IllegalOrphanException, NonexistentEntityException, SQLException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        Students students = readStudents();
        for (Student student : students.getStudents()) {
            if (student.getGroupId() == groupId) {
                throw new IllegalOrphanException("This group has students and cannot be destroyed!");
            }
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement("delete from gruppa where groupid = ?");
        pstat.setInt(1, groupId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
        notifyObservers(new ObserverMessage(Message.GroupDeleted, clientId));
    }

    public Gruppa readGroup(int groupId) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement("select * from gruppa where groupid = ?");
        pstat.setInt(1, groupId);
        ResultSet res = pstat.executeQuery();
        Gruppa group = null;
        if (res.next()) {
            group = new Gruppa(groupId, res.getString("groupname"));
        }
        res.close();
        pstat.close();
        conn.close();
        return group;
    }

    public Groups readGroups() throws SQLException {
        Groups groups = new Groups();
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stat = conn.createStatement();
        ResultSet res;
        res = stat.executeQuery("select * from gruppa");
        while (res.next()) {
            groups.getGroups().add(new Gruppa(res.getInt("groupid"), res.getString("groupname")));
        }
        res.close();
        stat.close();
        conn.close();
        return groups;
    }

    public int readGroupsCount() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stat = conn.createStatement();
        ResultSet res;
        res = stat.executeQuery("select count(*) from gruppa");
        res.next();
        int count = res.getInt(1);
        res.close();
        stat.close();
        conn.close();
        return count;
    }

    public Gruppa startGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        lockGroup(groupId);
        return group;
    }

    public void cancelGroupUpdating(int groupId)
            throws NonexistentEntityException, SQLException {
        Gruppa group = readGroup(groupId);
        if (group != null) {
            try {
                unlockGroup(groupId);
            } catch (DataUpdatingException ex) {
            }
        }
    }

    public void createStudent(int studentId, String studentName, int groupId, int clientId)
            throws NonexistentEntityException, PreexistingEntityException, SQLException {
        Student student = readStudent(studentId);
        if (student != null) {
            throw new PreexistingEntityException("Student with id " + studentId + " is already exists!");
        }
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat;
        pstat = conn.prepareStatement("insert into student values(?, ?, ?)");
        pstat.setInt(1, studentId);
        pstat.setString(2, studentName);
        pstat.setInt(3, groupId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
        notifyObservers(new ObserverMessage(Message.StudentCreated, clientId));
    }

    public void updateStudent(int studentId, String studentName, int groupId, int clientId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException("The student with id " + studentId + " no exists!");
        }
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException("The group with id " + groupId + " no exists!");
        }
        unlockStudent(studentId);
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement("update student set studentname = ?, groupid = ? where studentid = ?");
        pstat.setString(1, studentName);
        pstat.setInt(2, groupId);
        pstat.setInt(3, studentId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
        notifyObservers(new ObserverMessage(Message.StudentUpdated, clientId));
    }

    public void deleteStudent(int studentId, int clientId)
            throws NonexistentEntityException, SQLException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException("The student with id " + studentId + " no exists!");
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement("delete from student where studentid = ?");
        pstat.setInt(1, studentId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
        notifyObservers(new ObserverMessage(Message.StudentDeleted, clientId));
    }

    public Student readStudent(int studentId) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement("select * from student where studentid = ?");
        pstat.setInt(1, studentId);
        ResultSet res = pstat.executeQuery();
        Student student = null;
        if (res.next()) {
            student = new Student(studentId, res.getString("studentname"), res.getInt("groupid"));
        }
        res.close();
        pstat.close();
        conn.close();
        return student;
    }

    public Students readStudents() throws SQLException {
        Students students = new Students();
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stat = conn.createStatement();
        ResultSet res;
        res = stat.executeQuery("select * from student");
        while (res.next()) {
            students.getStudents().add(new Student(res.getInt("studentid"), res.getString("studentname"), res.getInt("groupid")));
        }
        res.close();
        stat.close();
        conn.close();
        return students;
    }

    public int readStudentsCount() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stat = conn.createStatement();
        ResultSet res;
        res = stat.executeQuery("select count(*) from student");
        res.next();
        int count = res.getInt(1);
        res.close();
        stat.close();
        conn.close();
        return count;
    }

    public Student startStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException("The student with id " + studentId + " no exists!");
        }
        lockStudent(studentId);
        return student;
    }

    public void cancelStudentUpdating(int studentId)
            throws NonexistentEntityException, SQLException {
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
