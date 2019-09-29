package client.model;

import client.entities.Gruppa;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Groups implements Serializable {

    private static final long serialVersionUID = 123456789098760010L;

    @XmlElementWrapper
    @XmlElement
    private List<Gruppa> groups;

    public Groups() {
        this.groups = new ArrayList<>();
    }

    public Groups(List<Gruppa> groups) {
        this.groups = groups;
    }

    public List<Gruppa> getGroups() {
        return groups;
    }
}
