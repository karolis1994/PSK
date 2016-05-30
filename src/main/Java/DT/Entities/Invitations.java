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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ace
 */
@Entity
@Table(name = "invitations")
@NamedQueries({
    @NamedQuery(name = "Invitations.findAll", query = "SELECT i FROM Invitations i"),
    @NamedQuery(name = "Invitations.findById", query = "SELECT i FROM Invitations i WHERE i.id = :id"),
    @NamedQuery(name = "Invitations.findBySenderId", query = "SELECT i FROM Invitations i WHERE i.senderid = :senderid"),
    @NamedQuery(name = "Invitations.findByReceiverEmail", query = "SELECT i FROM Invitations i WHERE i.receiveremail = :receiveremail"),
    @NamedQuery(name = "Invitations.findByVersion", query = "SELECT i FROM Invitations i WHERE i.version = :version")})
public class Invitations implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "receiveremail")
    private String receiveremail;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "version")
    @Version
    private int version;
    @JoinColumn(name = "senderid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Principals senderid;

    public Invitations() {
    }

    public Invitations(Integer id) {
        this.id = id;
    }

    public Invitations(Integer id, int version) {
        this.id = id;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invitations)) {
            return false;
        }
        Invitations other = (Invitations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Invitations[ id=" + id + " ]";
    }

    public String getReceiveremail() {
        return receiveremail;
    }

    public void setReceiveremail(String receiveremail) {
        this.receiveremail = receiveremail;
    }
    
}
