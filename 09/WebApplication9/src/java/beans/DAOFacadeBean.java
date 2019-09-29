package beans;

import entities.Gruppa;
import entities.Student;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Model;
import server.util.DBUtils;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import shared.util.Utils;

@Stateless(mappedName = "DAOFacadeBean9")
public class DAOFacadeBean {

    @PersistenceContext(unitName = "WebApplication9PU")
    private EntityManager em;
    @Resource(mappedName = "jms/LabQueueConnectionFactory")
    private QueueConnectionFactory labQueueConnectionFactory;
    @Resource(mappedName = "jms/LabQueue")
    private Queue labQueue;

    public DAOFacadeBean() {
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    protected QueueConnectionFactory getLabQueueConnectionFactory() {
        return labQueueConnectionFactory;
    }

    protected Queue getLabQueue() {
        return labQueue;
    }

    public void createGroup(int groupId, String groupName)
            throws PreexistingEntityException {
        Gruppa group = readGroup(groupId);
        if (group != null) {
            throw new PreexistingEntityException(
                    "Group with id " + groupId + " is already exists!");
        }
        group = new Gruppa(groupId, groupName);
        getEntityManager().persist(group);
        log(DBUtils.GROUP_TABLE_NAME, group.getGroupid(), group.getGroupname());
    }

    public void updateGroup(int groupId, String groupName)
            throws DataUpdatingException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no exists!");
        }
        DBUtils.unlockGroup(groupId);
        group.setGroupid(groupId);
        group.setGroupname(groupName);
        getEntityManager().merge(group);
        log(DBUtils.GROUP_TABLE_NAME, group.getGroupid(), group.getGroupname());
    }

    public void deleteGroup(int groupId)
            throws IllegalOrphanException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no exists!");
        }
        List<Student> students = readStudents();
        for (Student student : students) {
            if (student.getGroupid().getGroupid() == groupId) {
                throw new IllegalOrphanException("This group has students and cannot be destroyed!");
            }
        }
        getEntityManager().remove(group);
        log(DBUtils.GROUP_TABLE_NAME, group.getGroupid(), group.getGroupname());
    }

    public Gruppa readGroup(int groupId) {
        return getEntityManager().find(Gruppa.class, groupId);
    }

    public List<Gruppa> readGroups() {
        return getEntityManager().createNamedQuery("Gruppa.findAll").getResultList();
    }

    public List<Gruppa> readGroupsByRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(Gruppa.class));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public List<Gruppa> readGroupsByName(String groupName) {
        return getEntityManager().createNamedQuery("Gruppa.findByGroupname").setParameter("groupname", groupName).getResultList();
    }

    public int readGroupsCount() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<Gruppa> rt = cq.from(Gruppa.class);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Gruppa startGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no exists!");
        }
        DBUtils.lockGroup(groupId);
        return group;
    }

    public void cancelGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group != null) {
            try {
                DBUtils.unlockGroup(groupId);
            } catch (DataUpdatingException ex) {
            }
        }
    }

    public void createStudent(int studentId, String studentName, int groupId)
            throws NonexistentEntityException, PreexistingEntityException {
        Student student = readStudent(studentId);
        if (student != null) {
            throw new PreexistingEntityException(
                    "Student with id " + studentId + " is already exists!");
        }
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no exists!");
        }
        student = new Student(studentId, studentName);
        student.setGroupid(group);
        getEntityManager().persist(student);
        log(DBUtils.STUDENT_TABLE_NAME, student.getStudentid(), student.getStudentname());
    }

    public void updateStudent(int studentId, String studentName, int groupId)
            throws DataUpdatingException, NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no exists!");
        }
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no exists!");
        }
        DBUtils.unlockStudent(studentId);
        student.setStudentid(studentId);
        student.setStudentname(studentName);
        student.setGroupid(group);
        getEntityManager().merge(student);
        log(DBUtils.STUDENT_TABLE_NAME, student.getStudentid(), student.getStudentname());
    }

    public void deleteStudent(int studentId)
            throws NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no exists!");
        }
        getEntityManager().remove(student);
        log(DBUtils.STUDENT_TABLE_NAME, student.getStudentid(), student.getStudentname());
    }

    public Student readStudent(int studentId) {
        return getEntityManager().find(Student.class, studentId);
    }

    public List<Student> readStudents() {
        return getEntityManager().createNamedQuery("Student.findAll").getResultList();
    }

    public List<Student> readStudentsByRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(Student.class));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public List<Student> readStudentsByName(String studentName) {
        return getEntityManager().createNamedQuery("Student.findByStudentname").setParameter("studentname", studentName).getResultList();
    }

    public int readStudentsCount() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<Student> rt = cq.from(Student.class);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Student startStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no exists!");
        }
        DBUtils.lockStudent(studentId);
        return student;
    }

    public void cancelStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student != null) {
            try {
                DBUtils.unlockStudent(studentId);
            } catch (DataUpdatingException e) {
            }
        }
    }

    public void serializeModel(String filepath) throws IOException {
        Model model = new Model(readGroups(), readStudents());
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
        getEntityManager().createQuery("DELETE FROM Gruppa").executeUpdate();
        getEntityManager().createQuery("DELETE FROM Student").executeUpdate();
        for (Gruppa group : model.getGroups()) {
            getEntityManager().persist(group);
        }
        for (Student student : model.getStudents()) {
            getEntityManager().persist(student);
        }
    }

    public void log(String entityType, int entityId, String entityName) {
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        try {
            queueConnection = getLabQueueConnectionFactory().createQueueConnection();
            queueConnection.start();
            queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            MapMessage mapMessage = queueSession.createMapMessage();
            mapMessage.setString("entitytype", entityType);
            mapMessage.setInt("entityid", entityId);
            mapMessage.setString("entityname", entityName);
            mapMessage.setLong("logrecordtime", (long) (System.currentTimeMillis() / 1000));
            QueueSender queueSender = queueSession.createSender(getLabQueue());
            queueSender.send(mapMessage);
        } catch (JMSException ex) {
            Utils.printException(ex);
        } finally {
            try {
                if (queueSession != null) {
                    queueSession.close();
                }
            } catch (JMSException ex) {
                Utils.printException(ex);
            }
            try {
                if (queueConnection != null) {
                    queueConnection.close();
                }
            } catch (JMSException ex) {
                Utils.printException(ex);
            }
        }
    }
}
