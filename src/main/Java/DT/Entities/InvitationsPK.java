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
import javax.validation.constraints.Size;

/**
 *
 * @author Laurynas
 */
@Embeddable
public class InvitationsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "recieveremail")
    private String recieveremail;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "senderid")
    private int senderid;

    public InvitationsPK() {
    }

    public InvitationsPK(String recieveremail, int senderid) {
        this.recieveremail = recieveremail;
        this.senderid = senderid;
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
       // hash += (int) recieveremail;
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
        if (this.recieveremail != other.recieveremail) {
            return false;
        }
        if (this.senderid != other.senderid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.InvitationsPK[ recieveremail=" + recieveremail + ", senderid=" + senderid + " ]";
    }

    public String getRecieveremail() { return recieveremail; }

    public void setRecieveremail(String recieveremail) { this.recieveremail = recieveremail; }
    
}
