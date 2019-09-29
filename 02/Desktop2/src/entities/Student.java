package entities;

import java.io.Serializable;

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    private int studentId;
    private String studentName;
    private Group group;

    public Student() {
    }

    public Student(Integer studentId) {
        this.studentId = studentId;
    }

    public Student(Integer studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (new Integer(studentId)).hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        return this.studentId == other.studentId;
    }

    @Override
    public String toString() {
        return "entities.Student[studentId=" + studentId
                + ",studentName=" + studentName + ",group=" + group + "]";
    }
}
