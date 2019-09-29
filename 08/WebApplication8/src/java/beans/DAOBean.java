package beans;

import controller.JDBCController;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import entities.Group;
import entities.Student;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class DAOBean implements DAOBeanRemote {

    @Override
    public void createGroup(int groupId, String groupName)
            throws PreexistingEntityException, SQLException {
        JDBCController.getInstance().createGroup(groupId, groupName);
    }

    @Override
    public Group readGroup(int groupId) throws SQLException {
        return JDBCController.getInstance().readGroup(groupId);
    }

    @Override
    public List<Group> readGroupsByName(String groupName) throws SQLException {
        return JDBCController.getInstance().readGroupsByName(groupName);
    }

    @Override
    public Group startGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        return JDBCController.getInstance().startGroupUpdating(groupId);
    }

    @Override
    public void cancelGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        JDBCController.getInstance().cancelGroupUpdating(groupId);
    }

    @Override
    public void updateGroup(int groupId, String groupName)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        JDBCController.getInstance().updateGroup(groupId, groupName);
    }

    @Override
    public void deleteGroup(int groupId)
            throws IllegalOrphanException, NonexistentEntityException, SQLException {
        JDBCController.getInstance().deleteGroup(groupId);
    }

    @Override
    public List<Group> readGroups() throws SQLException {
        return JDBCController.getInstance().readGroups();
    }

    @Override
    public int readGroupsCount() throws SQLException {
        return JDBCController.getInstance().readGroupsCount();
    }

    @Override
    public void createStudent(int studentId, String studentName, int groupId)
            throws NonexistentEntityException, PreexistingEntityException, SQLException {
        JDBCController.getInstance().createStudent(studentId, studentName, groupId);
    }

    @Override
    public Student readStudent(int studentId) throws SQLException {
        return JDBCController.getInstance().readStudent(studentId);
    }

    @Override
    public List<Student> readStudentsByName(String studentName) throws SQLException {
        return JDBCController.getInstance().readStudentsByName(studentName);
    }

    @Override
    public Student startStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        return JDBCController.getInstance().startStudentUpdating(studentId);
    }

    @Override
    public void cancelStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        JDBCController.getInstance().cancelStudentUpdating(studentId);
    }

    @Override
    public void updateStudent(int studentId, String studentName, int groupId)
            throws DataUpdatingException, NonexistentEntityException, SQLException {
        JDBCController.getInstance().updateStudent(studentId, studentName, groupId);
    }

    @Override
    public void deleteStudent(int studentId)
            throws NonexistentEntityException, SQLException {
        JDBCController.getInstance().deleteStudent(studentId);
    }

    @Override
    public List<Student> readStudents() throws SQLException {
        return JDBCController.getInstance().readStudents();
    }

    @Override
    public int readStudentsCount() throws SQLException {
        return JDBCController.getInstance().readStudentsCount();
    }
}
