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
import javax.validation.constraints.Size;

/**
 *
 * @author Laurynas
 */
@Entity
@Table(name = "extras")
@NamedQueries({
    @NamedQuery(name = "Extras.findAll", query = "SELECT e FROM Extras e"),
    @NamedQuery(name = "Extras.findById", query = "SELECT e FROM Extras e WHERE e.id = :id"),
    @NamedQuery(name = "Extras.findByTitle", query = "SELECT e FROM Extras e WHERE e.title = :title"),
    @NamedQuery(name = "Extras.findByDescription", query = "SELECT e FROM Extras e WHERE e.description = :description"),
    @NamedQuery(name = "Extras.findByIsdeleted", query = "SELECT e FROM Extras e WHERE e.isdeleted = :isdeleted")})
public class Extras implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "extraid")
    private List<Reservationextras> reservationextrasList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "title")
    private String title;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "isdeleted")
    private Boolean isdeleted;
    @OneToMany(mappedBy = "extrasid", fetch=FetchType.EAGER)
    private List<Paidservices> paidservicesList;
    @JoinColumn(name = "houseid", referencedColumnName = "id")
    @ManyToOne
    private Houses houseid;

    public Extras() {
    }

    public Extras(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public List<Paidservices> getPaidservicesList() {
        return paidservicesList;
    }

    public void setPaidservicesList(List<Paidservices> paidservicesList) {
        this.paidservicesList = paidservicesList;
    }

    public Houses getHouseid() {
        return houseid;
    }

    public void setHouseid(Houses houseid) {
        this.houseid = houseid;
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
        if (!(object instanceof Extras)) {
            return false;
        }
        Extras other = (Extras) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Extras[ id=" + id + " ]";
    }

    public List<Reservationextras> getReservationextrasList() {
        return reservationextrasList;
    }

    public void setReservationextrasList(List<Reservationextras> reservationextrasList) {
        this.reservationextrasList = reservationextrasList;
    }
    
}
