/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author donatas
 */
@Entity
@Table(name = "paidservices")
@NamedQueries({
    @NamedQuery(name = "Paidservices.findAll", query = "SELECT p FROM Paidservices p"),
    @NamedQuery(name = "Paidservices.findById", query = "SELECT p FROM Paidservices p WHERE p.id = :id"),
    @NamedQuery(name = "Paidservices.findByCost", query = "SELECT p FROM Paidservices p WHERE p.cost = :cost")})
public class Paidservices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost")
    private double cost;
    @JoinColumn(name = "extrasid", referencedColumnName = "id")
    @ManyToOne
    private Extras extrasid;
    @JoinColumn(name = "houseid", referencedColumnName = "id")
    @ManyToOne
    private Houses houseid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paidserviceid")
    private List<Payments> paymentsList;

    public Paidservices() {
    }

    public Paidservices(Integer id) {
        this.id = id;
    }

    public Paidservices(Integer id, double cost) {
        this.id = id;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Extras getExtrasid() {
        return extrasid;
    }

    public void setExtrasid(Extras extrasid) {
        this.extrasid = extrasid;
    }

    public Houses getHouseid() {
        return houseid;
    }

    public void setHouseid(Houses houseid) {
        this.houseid = houseid;
    }

    public List<Payments> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<Payments> paymentsList) {
        this.paymentsList = paymentsList;
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
        if (!(object instanceof Paidservices)) {
            return false;
        }
        Paidservices other = (Paidservices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Paidservices[ id=" + id + " ]";
    }
    
}
