package beans;

import static controller.JDBCController.*;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import entities.Gruppa;
import entities.Student;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Model;

@Stateless(mappedName = "DAOBean9")
public class DAOBean implements DAOBeanRemote {

    @PersistenceContext(unitName = "WebApplication9PU")
    private EntityManager em;

    @Override
    public void createGroup(int groupId, String groupName)
            throws PreexistingEntityException {
        Gruppa group = readGroup(groupId);
        if (group != null) {
            throw new PreexistingEntityException(
                    "Group with id " + groupId + " is already exists.");
        }
        group = new Gruppa(groupId, groupName);
        em.persist(group);
    }

    @Override
    public Gruppa readGroup(int groupId) {
        return em.find(Gruppa.class, groupId);
    }

    @Override
    public List<Gruppa> readGroupsByName(String groupName) {
        return em.createNamedQuery("Gruppa.findByGroupname").setParameter("groupname", groupName).getResultList();
    }

    @Override
    public Gruppa startGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        lockGroup(groupId);
        return group;
    }

    @Override
    public void cancelGroupUpdating(int groupId)
            throws DataUpdatingException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        unlockGroup(groupId);
    }

    @Override
    public void updateGroup(int groupId, String groupName)
            throws DataUpdatingException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        unlockGroup(groupId);
        group.setGroupid(groupId);
        group.setGroupname(groupName);
        em.merge(group);
    }

    @Override
    public void deleteGroup(int groupId)
            throws IllegalOrphanException, NonexistentEntityException {
        Gruppa group = readGroup(groupId);
        if (readGroup(groupId) == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        List<Student> students = readStudents();
        for (Student student : students) {
            if (student.getGroupid().getGroupid() == groupId) {
                throw new IllegalOrphanException("This group has students and cannot be destroyed!");
            }
        }
        em.remove(group);
    }

    @Override
    public List<Gruppa> readGroups() {
        return em.createNamedQuery("Gruppa.findAll").getResultList();
    }

    @Override
    public int readGroupsCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Gruppa> rt = cq.from(Gruppa.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void createStudent(int studentId, String studentName, int groupId)
            throws NonexistentEntityException, PreexistingEntityException {
        Student student = readStudent(studentId);
        if (student != null) {
            throw new PreexistingEntityException(
                    "Student with id " + studentId + " is already exists.");
        }
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        student = new Student(studentId, studentName);
        student.setGroupid(group);
        em.persist(student);
    }

    @Override
    public Student readStudent(int studentId) {
        return em.find(Student.class, studentId);
    }

    @Override
    public List<Student> readStudentsByName(String studentName) {
        return em.createNamedQuery("Student.findByStudentname").setParameter("studentname", studentName).getResultList();
    }

    @Override
    public Student startStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        lockStudent(studentId);
        return student;
    }

    @Override
    public void cancelStudentUpdating(int studentId)
            throws DataUpdatingException, NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        unlockStudent(studentId);
    }

    @Override
    public void updateStudent(int studentId, String studentName, int groupId)
            throws DataUpdatingException, NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        Gruppa group = readGroup(groupId);
        if (group == null) {
            throw new NonexistentEntityException(
                    "The group with id " + groupId + " no longer exists.");
        }
        unlockStudent(studentId);
        student.setStudentid(studentId);
        student.setStudentname(studentName);
        student.setGroupid(group);
        em.merge(student);
    }

    @Override
    public void deleteStudent(int studentId)
            throws NonexistentEntityException {
        Student student = readStudent(studentId);
        if (student == null) {
            throw new NonexistentEntityException(
                    "The student with id " + studentId + " no longer exists.");
        }
        em.remove(student);
    }

    @Override
    public List<Student> readStudents() {
        return em.createNamedQuery("Student.findAll").getResultList();
    }

    @Override
    public int readStudentsCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Student> rt = cq.from(Student.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void serializeModel(String filepath) throws IOException {
        Model model = new Model(readGroups(), readStudents());
        try (ObjectOutputStream out
                = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(model);
            out.flush();
        }
    }

    @Override
    public void deserializeModel(String filepath)
            throws ClassNotFoundException, IOException {
        Model model;
        try (ObjectInputStream in
                = new ObjectInputStream(new FileInputStream(filepath))) {
            model = (Model) in.readObject();
        }
        em.createQuery("DELETE FROM Gruppa");
        em.createQuery("DELETE FROM Student");
        for (Gruppa group : model.getGroups()) {
            em.persist(group);
        }
        for (Student student : model.getStudents()) {
            em.persist(student);
        }
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
