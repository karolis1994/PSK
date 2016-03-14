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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ace
 */
@Entity
@Table(name = "HANGAR")
@NamedQueries({
    @NamedQuery(name = "Hangar.findAll", query = "SELECT h FROM Hangar h"),
    @NamedQuery(name = "Hangar.findByHangarId", query = "SELECT h FROM Hangar h WHERE h.hangarId = :hangarId"),
    @NamedQuery(name = "Hangar.findByAddress", query = "SELECT h FROM Hangar h WHERE h.address = :address")})
public class Hangar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "hangar_id")
    private Integer hangarId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "address")
    private String address;
    @JoinTable(name = "HANGAR_KEEPER", joinColumns = {
        @JoinColumn(name = "hangar_id", referencedColumnName = "hangar_id")}, inverseJoinColumns = {
        @JoinColumn(name = "keeper_id", referencedColumnName = "keeper_id")})
    @ManyToMany
    private List<Keeper> keeperList;
    @OneToMany(mappedBy = "hangarId")
    private List<Plane> planeList;
    @OneToMany(mappedBy = "hangarId")
    private List<Keeper> keeperList1;

    public Hangar() {
    }

    public Hangar(Integer hangarId) {
        this.hangarId = hangarId;
    }

    public Hangar(Integer hangarId, String address) {
        this.hangarId = hangarId;
        this.address = address;
    }

    public Integer getHangarId() {
        return hangarId;
    }

    public void setHangarId(Integer hangarId) {
        this.hangarId = hangarId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Keeper> getKeeperList() {
        return keeperList;
    }

    public void setKeeperList(List<Keeper> keeperList) {
        this.keeperList = keeperList;
    }

    public List<Plane> getPlaneList() {
        return planeList;
    }

    public void setPlaneList(List<Plane> planeList) {
        this.planeList = planeList;
    }

    public List<Keeper> getKeeperList1() {
        return keeperList1;
    }

    public void setKeeperList1(List<Keeper> keeperList1) {
        this.keeperList1 = keeperList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (address != null ? address.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hangar)) {
            return false;
        }
        Hangar other = (Hangar) object;
        if ((this.address == null && other.address != null) || (this.address != null && !this.address.equals(other.address))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aumi1132.mif.vu.lt.pirmas.Hangar[ address=" + address + " ]";
    }
    
}
