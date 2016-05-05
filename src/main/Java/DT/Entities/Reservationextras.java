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

/**
 *
 * @author Laurynas
 */
@Entity
@Table(name = "reservationextras")
@NamedQueries({
    @NamedQuery(name = "Reservationextras.findAll", query = "SELECT r FROM Reservationextras r"),
    @NamedQuery(name = "Reservationextras.findById", query = "SELECT r FROM Reservationextras r WHERE r.id = :id")})
public class Reservationextras implements Serializable {
    @Basic(optional = false)
    @Column(name = "version")
    @Version
    private int version;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "extraid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Extras extraid;
    @JoinColumn(name = "reservationid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Reservations reservationid;

    public Reservationextras() {
    }

    public Reservationextras(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Extras getExtraid() {
        return extraid;
    }

    public void setExtraid(Extras extraid) {
        this.extraid = extraid;
    }

    public Reservations getReservationid() {
        return reservationid;
    }

    public void setReservationid(Reservations reservationid) {
        this.reservationid = reservationid;
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
        if (!(object instanceof Reservationextras)) {
            return false;
        }
        Reservationextras other = (Reservationextras) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Reservationextras[ id=" + id + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
}
