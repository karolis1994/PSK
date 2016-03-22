/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author donatas
 */
@Embeddable
public class InvitationsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "recieverid")
    private int recieverid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "senderid")
    private int senderid;

    public InvitationsPK() {
    }

    public InvitationsPK(int recieverid, int senderid) {
        this.recieverid = recieverid;
        this.senderid = senderid;
    }

    public int getRecieverid() {
        return recieverid;
    }

    public void setRecieverid(int recieverid) {
        this.recieverid = recieverid;
    }

    public int getSenderid() {
        return senderid;
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) recieverid;
        hash += (int) senderid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvitationsPK)) {
            return false;
        }
        InvitationsPK other = (InvitationsPK) object;
        if (this.recieverid != other.recieverid) {
            return false;
        }
        if (this.senderid != other.senderid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.InvitationsPK[ recieverid=" + recieverid + ", senderid=" + senderid + " ]";
    }
    
}
