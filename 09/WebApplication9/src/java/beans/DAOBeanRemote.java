package beans;

import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import entities.Gruppa;
import entities.Student;
import java.io.IOException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface DAOBeanRemote {

    public void createGroup(int groupId, String groupName) throws PreexistingEntityException;

    public Gruppa readGroup(int groupId);

    public List<Gruppa> readGroupsByName(String groupName);

    public Gruppa startGroupUpdating(int groupId) throws DataUpdatingException, NonexistentEntityException;

    public void cancelGroupUpdating(int groupId) throws DataUpdatingException, NonexistentEntityException;

    public void updateGroup(int groupId, String groupName) throws DataUpdatingException, NonexistentEntityException;

    public void deleteGroup(int groupId) throws IllegalOrphanException, NonexistentEntityException;

    public List<Gruppa> readGroups();

    public int readGroupsCount();

    public void createStudent(int studentId, String studentName, int groupId) throws NonexistentEntityException, PreexistingEntityException;

    public Student readStudent(int studentId);

    public List<Student> readStudentsByName(String studentName);

    public Student startStudentUpdating(int studentId) throws DataUpdatingException, NonexistentEntityException;

    public void cancelStudentUpdating(int studentId) throws DataUpdatingException, NonexistentEntityException;

    public void updateStudent(int studentId, String studentName, int groupId) throws DataUpdatingException, NonexistentEntityException;

    public void deleteStudent(int studentId) throws NonexistentEntityException;

    public List<Student> readStudents();

    public int readStudentsCount();

    public void serializeModel(String filepath) throws IOException;

    public void deserializeModel(String filepath) throws ClassNotFoundException, IOException;
}
