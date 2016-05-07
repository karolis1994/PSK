/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Laurynas
 */
@Entity
@Table(name = "payments")
@NamedQueries({
    @NamedQuery(name = "Payments.findAll", query = "SELECT p FROM Payments p"),
    @NamedQuery(name = "Payments.findById", query = "SELECT p FROM Payments p WHERE p.id = :id"),
    @NamedQuery(name = "Payments.findByPaymentno", query = "SELECT p FROM Payments p WHERE p.paymentno = :paymentno"),
    @NamedQuery(name = "Payments.findByCreatedat", query = "SELECT p FROM Payments p WHERE p.createdat = :createdat"),
    @NamedQuery(name = "Payments.findByPayedat", query = "SELECT p FROM Payments p WHERE p.payedat = :payedat"),
    @NamedQuery(name = "Payments.findByAmmount", query = "SELECT p FROM Payments p WHERE p.ammount = :ammount"),
    @NamedQuery(name = "Payments.findByIspaid", query = "SELECT p FROM Payments p WHERE p.ispaid = :ispaid")})
public class Payments implements Serializable {

    @Column(name = "paidwithpoints")
    private Boolean paidWithPoints;
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
    @Column(name = "paymentno")
    private Integer paymentno;
    @Column(name = "createdat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdat;
    @Column(name = "payedat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payedat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ammount")
    private double ammount;
    @Column(name = "ispaid")
    private Boolean ispaid;
    @JoinColumn(name = "paidserviceid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Paidservices paidserviceid;
    @JoinColumn(name = "principalid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Principals principalid;

    public Payments() {
    }

    public Payments(Integer id) {
        this.id = id;
    }

    public Payments(Integer id, double ammount) {
        this.id = id;
        this.ammount = ammount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaymentno() {
        return paymentno;
    }

    public void setPaymentno(Integer paymentno) {
        this.paymentno = paymentno;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getPayedat() {
        return payedat;
    }

    public void setPayedat(Date payedat) {
        this.payedat = payedat;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

    public Boolean getIspaid() {
        return ispaid;
    }

    public void setIspaid(Boolean ispaid) {
        this.ispaid = ispaid;
    }

    public Paidservices getPaidserviceid() {
        return paidserviceid;
    }

    public void setPaidserviceid(Paidservices paidserviceid) {
        this.paidserviceid = paidserviceid;
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
        if (!(object instanceof Payments)) {
            return false;
        }
        Payments other = (Payments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Payments[ id=" + id + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Boolean getPaidWithPoints() {
        return paidWithPoints;
    }

    public void setPaidWithPoints(Boolean paidWithPoints) {
        this.paidWithPoints = paidWithPoints;
    }

}
