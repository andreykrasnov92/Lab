package entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlTransient;

public class Student implements Serializable {

    @XmlTransient
    private static final long serialVersionUID = 1L;
    private int studentId;
    private String studentName;
    private int groupId;

    public Student() {
    }

    public Student(int studentId, String studentName, int groupId) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.groupId = groupId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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
                + ",studentName=" + studentName + ",groupId=" + groupId + "]";
    }
}
