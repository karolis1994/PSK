/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aumi1132.mif.vu.lt.pirmas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ace
 */
@Entity
@Table(name = "KEEPER")
@NamedQueries({
    @NamedQuery(name = "Keeper.findAll", query = "SELECT k FROM Keeper k"),
    @NamedQuery(name = "Keeper.findByKeeperId", query = "SELECT k FROM Keeper k WHERE k.keeperId = :keeperId"),
    @NamedQuery(name = "Keeper.findByNationalId", query = "SELECT k FROM Keeper k WHERE k.nationalId = :nationalId"),
    @NamedQuery(name = "Keeper.findByName", query = "SELECT k FROM Keeper k WHERE k.name = :name"),
    @NamedQuery(name = "Keeper.findByLastName", query = "SELECT k FROM Keeper k WHERE k.lastName = :lastName")})
public class Keeper implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "keeper_id")
    private Integer keeperId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "national_id")
    private String nationalId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "last_name")
    private String lastName;
    @ManyToMany(mappedBy = "keeperList")
    private List<Hangar> hangarList;
    @JoinColumn(name = "hangar_id", referencedColumnName = "hangar_id")
    @ManyToOne
    private Hangar hangarId;

    public Keeper() {
    }

    public Keeper(Integer keeperId) {
        this.keeperId = keeperId;
    }

    public Keeper(Integer keeperId, String nationalId, String name, String lastName) {
        this.keeperId = keeperId;
        this.nationalId = nationalId;
        this.name = name;
        this.lastName = lastName;
    }

    public Integer getKeeperId() {
        return keeperId;
    }

    public void setKeeperId(Integer keeperId) {
        this.keeperId = keeperId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Hangar> getHangarList() {
        return hangarList;
    }

    public void setHangarList(List<Hangar> hangarList) {
        this.hangarList = hangarList;
    }

    public Hangar getHangarId() {
        return hangarId;
    }

    public void setHangarId(Hangar hangarId) {
        this.hangarId = hangarId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nationalId != null ? nationalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Keeper)) {
            return false;
        }
        Keeper other = (Keeper) object;
        if ((this.nationalId == null && other.nationalId != null) || (this.nationalId != null && !this.nationalId.equals(other.nationalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aumi1132.mif.vu.lt.pirmas.Keeper[ nationalId=" + nationalId + " ]";
    }
    
}
