package beans;

import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import entities.Group;
import entities.Student;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface DAOBeanRemote {

    public void createGroup(int groupId, String groupName) throws PreexistingEntityException, SQLException;

    public Group readGroup(int groupId) throws SQLException;

    public List<Group> readGroupsByName(String groupName) throws SQLException;

    public Group startGroupUpdating(int groupId) throws DataUpdatingException, NonexistentEntityException, SQLException;

    public void cancelGroupUpdating(int groupId) throws DataUpdatingException, NonexistentEntityException, SQLException;

    public void updateGroup(int groupId, String groupName) throws DataUpdatingException, NonexistentEntityException, SQLException;

    public void deleteGroup(int groupId) throws IllegalOrphanException, NonexistentEntityException, SQLException;

    public List<Group> readGroups() throws SQLException;

    public int readGroupsCount() throws SQLException;

    public void createStudent(int studentId, String studentName, int groupId) throws NonexistentEntityException, PreexistingEntityException, SQLException;

    public Student readStudent(int studentId) throws SQLException;

    public List<Student> readStudentsByName(String studentName) throws SQLException;

    public Student startStudentUpdating(int studentId) throws DataUpdatingException, NonexistentEntityException, SQLException;

    public void cancelStudentUpdating(int studentId) throws DataUpdatingException, NonexistentEntityException, SQLException;

    public void updateStudent(int studentId, String studentName, int groupId) throws DataUpdatingException, NonexistentEntityException, SQLException;

    public void deleteStudent(int studentId) throws NonexistentEntityException, SQLException;

    public List<Student> readStudents() throws SQLException;

    public int readStudentsCount() throws SQLException;
}
