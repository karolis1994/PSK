/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aumi1132.mif.vu.lt.pirmas;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ace
 */
@Entity
@Table(name = "PLANE")
@NamedQueries({
    @NamedQuery(name = "Plane.findAll", query = "SELECT p FROM Plane p"),
    @NamedQuery(name = "Plane.findByPlaneId", query = "SELECT p FROM Plane p WHERE p.planeId = :planeId"),
    @NamedQuery(name = "Plane.findByTitle", query = "SELECT p FROM Plane p WHERE p.title = :title"),
    @NamedQuery(name = "Plane.findBySerialNumber", query = "SELECT p FROM Plane p WHERE p.serialNumber = :serialNumber")})
public class Plane implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "plane_id")
    private Integer planeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "serial_number")
    private String serialNumber;
    @JoinColumn(name = "hangar_id", referencedColumnName = "hangar_id")
    @ManyToOne
    private Hangar hangarId;

    public Plane() {
    }

    public Plane(Integer planeId) {
        this.planeId = planeId;
    }

    public Plane(Integer planeId, String title, String serialNumber) {
        this.planeId = planeId;
        this.title = title;
        this.serialNumber = serialNumber;
    }

    public Integer getPlaneId() {
        return planeId;
    }

    public void setPlaneId(Integer planeId) {
        this.planeId = planeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
        hash += (serialNumber != null ? serialNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plane)) {
            return false;
        }
        Plane other = (Plane) object;
        if ((this.serialNumber == null && other.serialNumber != null) || (this.serialNumber != null && !this.serialNumber.equals(other.serialNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aumi1132.mif.vu.lt.pirmas.Plane[ serialNumber=" + serialNumber + " ]";
    }
    
}
