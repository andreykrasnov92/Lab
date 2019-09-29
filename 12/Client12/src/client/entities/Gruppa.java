package client.entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Gruppa implements Serializable {

    private static final long serialVersionUID = 123456789098760001L;

    @XmlAttribute
    private int groupId;
    @XmlAttribute
    private String groupName;

    @XmlAttribute
    @XmlID
    public String getUniversalID() {
        return "gruppa_" + groupId;
    }

    public Gruppa() {
    }

    public Gruppa(int groupId, String groupName) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (new Integer(groupId)).hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Gruppa)) {
            return false;
        }
        Gruppa other = (Gruppa) object;
        return this.groupId == other.groupId;
    }

    @Override
    public String toString() {
        return "entities.Group[groupId=" + groupId
                + ",groupName=" + groupName + "]";
    }
}
