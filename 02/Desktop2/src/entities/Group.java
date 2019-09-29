package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Group implements Serializable {

    private static final long serialVersionUID = 1L;
    private int groupId;
    private String groupName;
    private Collection<Student> studentsCollection = new ArrayList<>();

    public Group() {
    }

    public Group(int groupId) {
        this.groupId = groupId;
    }

    public Group(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Collection<Student> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Student> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (new Integer(groupId)).hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Group)) {
            return false;
        }
        Group other = (Group) object;
        return this.groupId == other.groupId;
    }

    @Override
    public String toString() {
        return "entities.Group[groupId=" + groupId
                + ",groupName=" + groupName + "]";
    }
}
