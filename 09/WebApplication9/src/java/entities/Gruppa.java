package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "GRUPPA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gruppa.findAll", query = "SELECT g FROM Gruppa g"),
    @NamedQuery(name = "Gruppa.findByGroupid", query = "SELECT g FROM Gruppa g WHERE g.groupid = :groupid"),
    @NamedQuery(name = "Gruppa.findByGroupname", query = "SELECT g FROM Gruppa g WHERE g.groupname = :groupname")})
public class Gruppa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "GROUPID")
    private Integer groupid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 99)
    @Column(name = "GROUPNAME")
    private String groupname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupid")
    private Collection<Student> studentCollection;

    public Gruppa() {
    }

    public Gruppa(Integer groupid) {
        this.groupid = groupid;
    }

    public Gruppa(Integer groupid, String groupname) {
        this.groupid = groupid;
        this.groupname = groupname;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @XmlTransient
    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupid != null ? groupid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gruppa)) {
            return false;
        }
        Gruppa other = (Gruppa) object;
        if ((this.groupid == null && other.groupid != null) || (this.groupid != null && !this.groupid.equals(other.groupid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Gruppa[ groupid=" + groupid + " ]";
    }
}
