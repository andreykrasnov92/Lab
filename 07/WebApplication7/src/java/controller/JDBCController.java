package controller;

import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import entities.Group;
import entities.Student;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import model.Model;
import oracle.jdbc.driver.OracleDriver;
import static shared.util.Utils.*;

public class JDBCController {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE",
            USERNAME = "system", PASSWORD = "12345";

    private static final Set<Integer> lockedGroups = new HashSet<>(),
            lockedStudents = new HashSet<>();

    private JDBCController() {
        Locale.setDefault(Locale.ENGLISH);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+4"));
        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException ex) {
            printException(ex);
        }
    }

    public static JDBCController getInstance() {
        return JDBCControllerHolder.instance;
    }

    private static class JDBCControllerHolder {

        public static final JDBCController instance = new JDBCController();
    }

    public void serializeModel(String filepath) throws IOException, SQLException {
        Model model = new Model(readGroups(), readStudents());
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
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Statement stat = conn.createStatement();
        stat.executeUpdate("delete from gruppa");
        stat.executeUpdate("delete from student");
        PreparedStatement pstat = null;
        for (Group group : model.getGroups()) {
            pstat = conn.prepareStatement(
                    "insert into gruppa values(?, ?)");
            pstat.setInt(1, group.getGroupId());
            pstat.setString(2, group.getGroupName());
            pstat.executeUpdate();
        }
        for (Student student : model.getStudents()) {
            pstat = conn.prepareStatement(
                    "insert into student values(?, ?, ?)");
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

    public void createGroup(int groupId, String groupName)
            throws PreexistingEntityException, SQLException {
        if (readGroup(groupId) != null) {
            throw new PreexistingEntityException(
                    "Group with id " + groupId + " is already exists.");
        }
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat;
        pstat = conn.prepareStatement(
                "insert into gruppa values(?, ?)");
        pstat.setInt(1, groupId);
        pstat.setString(2, groupName);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
    }

    public Group readGroup(int groupId) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "select * from gruppa where groupid = ?");
        pstat.setInt(1, groupId);
        ResultSet res = pstat.executeQuery();
        Group group = null;
        if (res.next()) {
            group = new Group(groupId, res.getString("groupname"));
        }
        res.close();
        pstat.close();
        conn.close();
        return group;
    }

    public List<Group> readGroupsByName(String groupName) throws SQLException {
        List<Group> groups = new ArrayList<>();
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "select * from gruppa where groupname = ?");
        pstat.setString(1, groupName);
        ResultSet res = pstat.executeQuery();
        while (res.next()) {
            groups.add(new Group(res.getInt("groupid"), groupName));
        }
        res.close();
        pstat.close();
        conn.close();
        return groups;
    }

    public Group startGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Group group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        lockGroup(groupId);
        return group;
    }

    public void cancelGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Group group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        unlockGroup(groupId);
    }

    public void updateGroup(int groupId, String groupName)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        if (readGroup(groupId) == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        unlockGroup(groupId);
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "update gruppa set groupname = ? where groupid = ?");
        pstat.setString(1, groupName);
        pstat.setInt(2, groupId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
    }

    public void deleteGroup(int groupId)
            throws IllegalOrphanException, NonexistentEntityException, SQLException {
        if (readGroup(groupId) == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "delete from gruppa where groupid = ?");
        pstat.setInt(1, groupId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
    }

    public List<Group> readGroups() throws SQLException {
        List<Group> groups = new ArrayList<>();
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Statement stat = conn.createStatement();
        ResultSet res;
        res = stat.executeQuery("select * from gruppa");
        while (res.next()) {
            groups.add(new Group(res.getInt("groupid"), res.getString("groupname")));
        }
        res.close();
        stat.close();
        conn.close();
        return groups;
    }

    public int readGroupsCount() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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

    private void lockGroup(int groupId) throws DataUpdatingException {
        if (lockedGroups.contains(groupId)) {
            throw new DataUpdatingException("This group is updated now by another client!");
        } else {
            lockedGroups.add(groupId);
        }
    }

    private void unlockGroup(int groupId) throws DataUpdatingException {
        if (lockedGroups.contains(groupId)) {
            lockedGroups.remove(groupId);
        } else {
            throw new DataUpdatingException("You cannot update group until you've locked it.");
        }
    }

    public void createStudent(int studentId, String studentName, int groupId)
            throws NonexistentEntityException, PreexistingEntityException, SQLException {
        if (readStudent(studentId) != null) {
            throw new PreexistingEntityException(
                    "Student with id " + studentId + " is already exists.");
        }
        if (readGroup(groupId) == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat;
        pstat = conn.prepareStatement(
                "insert into student values(?, ?, ?)");
        pstat.setInt(1, studentId);
        pstat.setString(2, studentName);
        pstat.setInt(3, groupId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
    }

    public Student readStudent(int studentId) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "select * from student where studentid = ?");
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

    public List<Student> readStudentsByName(String studentName) throws SQLException {
        List<Student> students = new ArrayList<>();
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "select * from student where studentname = ?");
        pstat.setString(1, studentName);
        ResultSet res = pstat.executeQuery();
        while (res.next()) {
            students.add(new Student(res.getInt("studentid"), studentName, res.getInt("groupid")));
        }
        res.close();
        pstat.close();
        conn.close();
        return students;
    }

    public Student startStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        lockStudent(studentId);
        return student;
    }

    public void cancelStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        unlockStudent(studentId);
    }

    public void updateStudent(int studentId, String studentName, int groupId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        if (readStudent(studentId) == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        if (readGroup(groupId) == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        unlockStudent(studentId);
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "update student set studentname = ?, groupid = ? where studentid = ?");
        pstat.setString(1, studentName);
        pstat.setInt(2, groupId);
        pstat.setInt(3, studentId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
    }

    public void deleteStudent(int studentId)
            throws NonexistentEntityException, SQLException {
        if (readStudent(studentId) == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstat = conn.prepareStatement(
                "delete from student where studentid = ?");
        pstat.setInt(1, studentId);
        pstat.executeUpdate();
        pstat.close();
        conn.close();
    }

    public List<Student> readStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Statement stat = conn.createStatement();
        ResultSet res;
        res = stat.executeQuery("select * from student");
        while (res.next()) {
            students.add(new Student(res.getInt("studentid"), res.getString("studentname"), res.getInt("groupid")));
        }
        res.close();
        stat.close();
        conn.close();
        return students;
    }

    public int readStudentsCount() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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

    private void lockStudent(int studentId) throws DataUpdatingException {
        if (lockedStudents.contains(studentId)) {
            throw new DataUpdatingException("This student is updated now by another client!");
        } else {
            lockedStudents.add(studentId);
        }
    }

    private void unlockStudent(int studentId) throws DataUpdatingException {
        if (lockedStudents.contains(studentId)) {
            lockedStudents.remove(studentId);
        } else {
            throw new DataUpdatingException("You cannot update student until you've locked it.");
        }
    }
}
