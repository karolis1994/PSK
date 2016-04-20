/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Laurynas
 */
@Entity
@Table(name = "invitations")
@NamedQueries({
    @NamedQuery(name = "Invitations.findAll", query = "SELECT i FROM Invitations i"),
    @NamedQuery(name = "Invitations.findByRecieverid", query = "SELECT i FROM Invitations i WHERE i.invitationsPK.recieverid = :recieverid"),
    @NamedQuery(name = "Invitations.findBySenderid", query = "SELECT i FROM Invitations i WHERE i.invitationsPK.senderid = :senderid"),
    @NamedQuery(name = "Invitations.findByUrlcode", query = "SELECT i FROM Invitations i WHERE i.urlcode = :urlcode")})
public class Invitations implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvitationsPK invitationsPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "urlcode")
    private String urlcode;
    @JoinColumn(name = "recieverid", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Principals principals;
    @JoinColumn(name = "senderid", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Principals principals1;

    public Invitations() {
    }

    public Invitations(InvitationsPK invitationsPK) {
        this.invitationsPK = invitationsPK;
    }

    public Invitations(InvitationsPK invitationsPK, String urlcode) {
        this.invitationsPK = invitationsPK;
        this.urlcode = urlcode;
    }

    public Invitations(int recieverid, int senderid) {
        this.invitationsPK = new InvitationsPK(recieverid, senderid);
    }

    public InvitationsPK getInvitationsPK() {
        return invitationsPK;
    }

    public void setInvitationsPK(InvitationsPK invitationsPK) {
        this.invitationsPK = invitationsPK;
    }

    public String getUrlcode() {
        return urlcode;
    }

    public void setUrlcode(String urlcode) {
        this.urlcode = urlcode;
    }

    public Principals getPrincipals() {
        return principals;
    }

    public void setPrincipals(Principals principals) {
        this.principals = principals;
    }

    public Principals getPrincipals1() {
        return principals1;
    }

    public void setPrincipals1(Principals principals1) {
        this.principals1 = principals1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invitationsPK != null ? invitationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invitations)) {
            return false;
        }
        Invitations other = (Invitations) object;
        if ((this.invitationsPK == null && other.invitationsPK != null) || (this.invitationsPK != null && !this.invitationsPK.equals(other.invitationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Invitations[ invitationsPK=" + invitationsPK + " ]";
    }
    
}
