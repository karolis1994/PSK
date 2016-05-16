/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 *
 * @author donatas
 */
@Entity
@Table(name = "reservations")
@NamedQueries({
    @NamedQuery(name = "Reservations.findAll", query = "SELECT r FROM Reservations r"),
    @NamedQuery(name = "Reservations.findById", query = "SELECT r FROM Reservations r WHERE r.id = :id"),
    @NamedQuery(name = "Reservations.findByReservedfrom", query = "SELECT r FROM Reservations r WHERE r.reservedfrom = :reservedfrom"),
    @NamedQuery(name = "Reservations.findByReservedto", query = "SELECT r FROM Reservations r WHERE r.reservedto = :reservedto"),
    @NamedQuery(name = "Reservations.findByIscanceled", query = "SELECT r FROM Reservations r WHERE r.iscanceled = :iscanceled")})
public class Reservations implements Serializable {
    @JoinColumn(name = "extraid", referencedColumnName = "id")
    @ManyToOne
    private Extras extraid;
    @JoinColumn(name = "paymentid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Payments paymentid;
    @Basic(optional = false)
    @Column(name = "version")
    @Version
    private int version;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservationid", fetch=FetchType.EAGER)
    private List<Reservationextras> reservationextrasList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reservedfrom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservedfrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reservedto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservedto;
    @Column(name = "iscanceled")
    private Boolean iscanceled;
    @JoinColumn(name = "houseid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Houses houseid;
    @JoinColumn(name = "principalid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Principals principalid;

    public Reservations() {
    }

    public Reservations(Integer id) {
        this.id = id;
    }

    public Reservations(Integer id, Date reservedfrom, Date reservedto) {
        this.id = id;
        this.reservedfrom = reservedfrom;
        this.reservedto = reservedto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReservedfrom() {
        return reservedfrom;
    }

    public void setReservedfrom(Date reservedfrom) {
        this.reservedfrom = reservedfrom;
    }

    public Date getReservedto() {
        return reservedto;
    }

    public void setReservedto(Date reservedto) {
        this.reservedto = reservedto;
    }

    public Boolean getIscanceled() {
        return iscanceled;
    }

    public void setIscanceled(Boolean iscanceled) {
        this.iscanceled = iscanceled;
    }

    public Houses getHouseid() {
        return houseid;
    }

    public void setHouseid(Houses houseid) {
        this.houseid = houseid;
    }

    public Principals getPrincipalid() {
        return principalid;
    }

    public void setPrincipalid(Principals principalid) {
        this.principalid = principalid;
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
        if (!(object instanceof Reservations)) {
            return false;
        }
        Reservations other = (Reservations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Reservations[ id=" + id + " ]";
    }

    public List<Reservationextras> getReservationextrasList() {
        return reservationextrasList;
    }

    public void setReservationextrasList(List<Reservationextras> reservationextrasList) {
        this.reservationextrasList = reservationextrasList;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Payments getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(Payments paymentid) {
        this.paymentid = paymentid;
    }

    public Extras getExtraid() {
        return extraid;
    }

    public void setExtraid(Extras extraid) {
        this.extraid = extraid;
    }
    
}
