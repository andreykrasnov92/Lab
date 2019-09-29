package beans;

import entities.Gruppa;
import entities.Logrecord;
import entities.Student;
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
import server.util.DBUtils;
import shared.util.Utils;

@Stateless(mappedName = "DAOFacadeBean12")
public class DAOFacadeBean {

    @PersistenceContext(unitName = "WebApplication12PU")
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

    public void createGroup(Gruppa group) {
        getEntityManager().persist(group);
        log(DBUtils.GROUP_TABLE_NAME, group.getGroupid(), group.getGroupname());
    }

    public void updateGroup(Gruppa group) {
        getEntityManager().merge(group);
        log(DBUtils.GROUP_TABLE_NAME, group.getGroupid(), group.getGroupname());
    }

    public void deleteGroup(Gruppa group) {
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

    public void createStudent(Student student) {
        getEntityManager().persist(student);
        log(DBUtils.STUDENT_TABLE_NAME, student.getStudentid(), student.getStudentname());
    }

    public void updateStudent(Student student) {
        getEntityManager().merge(student);
        log(DBUtils.STUDENT_TABLE_NAME, student.getStudentid(), student.getStudentname());
    }

    public void deleteStudent(Student student) {
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

    public Logrecord readLogrecord(int logrecordId) {
        return getEntityManager().find(Logrecord.class, logrecordId);
    }

    public List<Logrecord> readLogrecords() {
        return getEntityManager().createNamedQuery("Logrecord.findAll").getResultList();
    }

    public List<Logrecord> readLogrecordsByRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(Logrecord.class));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public List<Logrecord> readLogrecordsByName(String entityName) {
        return getEntityManager().createNamedQuery("Logrecord.findByEntityname").setParameter("entityname", entityName).getResultList();
    }

    public int readLogrecordsCount() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<Logrecord> rt = cq.from(Logrecord.class);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
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
