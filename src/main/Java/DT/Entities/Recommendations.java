/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Karolis
 */
@Entity
@Table(name = "recommendations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recommendations.findAll", query = "SELECT r FROM Recommendations r"),
    @NamedQuery(name = "Recommendations.findById", query = "SELECT r FROM Recommendations r WHERE r.id = :id"),
    @NamedQuery(name = "Recommendations.findByUrlcode", query = "SELECT r FROM Recommendations r WHERE r.urlcode = :urlcode"),
    @NamedQuery(name = "Recommendations.findByIsactivated", query = "SELECT r FROM Recommendations r WHERE r.isactivated = :isactivated"),
    @NamedQuery(name = "Recommendations.findBySenderid", query = "SELECT r FROM Recommendations r Where r.senderid.id = :senderid"),
    @NamedQuery(name = "Recommendations.findByRecieverid", query = "SELECT r FROM Recommendations r Where r.recieverid.id = :recieverid"),
    @NamedQuery(name = "Recommendations.findByVersion", query = "SELECT r FROM Recommendations r WHERE r.version = :version"),
    @NamedQuery(name = "Recommendations.findByApprovedSender", query = "SELECT r FROM Recommendations r WHERE r.senderid.id = :senderid AND r.isactivated = 't'")})
    
public class Recommendations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "urlcode")
    private String urlcode;
    @Column(name = "isactivated")
    private Boolean isactivated;
    @Basic(optional = false)
    @Column(name = "version")
    private int version;
    @JoinColumn(name = "senderid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Principals senderid;
    @JoinColumn(name = "recieverid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Principals recieverid;

    public Recommendations() {
    }

    public Recommendations(Integer id) {
        this.id = id;
    }

    public Recommendations(Integer id, String urlcode, int version) {
        this.id = id;
        this.urlcode = urlcode;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrlcode() {
        return urlcode;
    }

    public void setUrlcode(String urlcode) {
        this.urlcode = urlcode;
    }

    public Boolean getIsactivated() {
        return isactivated;
    }

    public void setIsactivated(Boolean isactivated) {
        this.isactivated = isactivated;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Principals getSenderid() {
        return senderid;
    }

    public void setSenderid(Principals senderid) {
        this.senderid = senderid;
    }

    public Principals getRecieverid() {
        return recieverid;
    }

    public void setRecieverid(Principals recieverid) {
        this.recieverid = recieverid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recommendations)) {
            return false;
        }
        Recommendations other = (Recommendations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Recommendations[ id=" + id + " ]";
    }
    
}
