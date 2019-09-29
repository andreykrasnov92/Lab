package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "LOGRECORD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logrecord.findAll", query = "SELECT l FROM Logrecord l"),
    @NamedQuery(name = "Logrecord.findByLogrecordid", query = "SELECT l FROM Logrecord l WHERE l.logrecordid = :logrecordid"),
    @NamedQuery(name = "Logrecord.findByEntitytype", query = "SELECT l FROM Logrecord l WHERE l.entitytype = :entitytype"),
    @NamedQuery(name = "Logrecord.findByEntityid", query = "SELECT l FROM Logrecord l WHERE l.entityid = :entityid"),
    @NamedQuery(name = "Logrecord.findByEntityname", query = "SELECT l FROM Logrecord l WHERE l.entityname = :entityname"),
    @NamedQuery(name = "Logrecord.findByLogrecordtime", query = "SELECT l FROM Logrecord l WHERE l.logrecordtime = :logrecordtime")})
public class Logrecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOGRECORDID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer logrecordid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 99)
    @Column(name = "ENTITYTYPE")
    private String entitytype;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENTITYID")
    private int entityid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 99)
    @Column(name = "ENTITYNAME")
    private String entityname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOGRECORDTIME")
    private long logrecordtime;

    public Logrecord() {
    }

    public Logrecord(Integer logrecordid) {
        this.logrecordid = logrecordid;
    }

    public Logrecord(Integer logrecordid, String entitytype, int entityid, String entityname, long logrecordtime) {
        this.logrecordid = logrecordid;
        this.entitytype = entitytype;
        this.entityid = entityid;
        this.entityname = entityname;
        this.logrecordtime = logrecordtime;
    }

    public Integer getLogrecordid() {
        return logrecordid;
    }

    public void setLogrecordid(Integer logrecordid) {
        this.logrecordid = logrecordid;
    }

    public String getEntitytype() {
        return entitytype;
    }

    public void setEntitytype(String entitytype) {
        this.entitytype = entitytype;
    }

    public int getEntityid() {
        return entityid;
    }

    public void setEntityid(int entityid) {
        this.entityid = entityid;
    }

    public String getEntityname() {
        return entityname;
    }

    public void setEntityname(String entityname) {
        this.entityname = entityname;
    }

    public long getLogrecordtime() {
        return logrecordtime;
    }

    public void setLogrecordtime(long logrecordtime) {
        this.logrecordtime = logrecordtime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logrecordid != null ? logrecordid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logrecord)) {
            return false;
        }
        Logrecord other = (Logrecord) object;
        if ((this.logrecordid == null && other.logrecordid != null) || (this.logrecordid != null && !this.logrecordid.equals(other.logrecordid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Logrecord[ logrecordid=" + logrecordid + " ]";
    }
}
