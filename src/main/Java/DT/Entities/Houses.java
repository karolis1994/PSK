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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Laurynas
 */
@Entity
@Table(name = "houses")
@NamedQueries({
    @NamedQuery(name = "Houses.findAll", query = "SELECT h FROM Houses h"),
    @NamedQuery(name = "Houses.findById", query = "SELECT h FROM Houses h WHERE h.id = :id"),
    @NamedQuery(name = "Houses.findByTitle", query = "SELECT h FROM Houses h WHERE h.title = :title"),
    @NamedQuery(name = "Houses.findByDescription", query = "SELECT h FROM Houses h WHERE h.description = :description"),
    @NamedQuery(name = "Houses.findByAddress", query = "SELECT h FROM Houses h WHERE h.address = :address"),
    @NamedQuery(name = "Houses.findByIsclosed", query = "SELECT h FROM Houses h WHERE h.isclosed = :isclosed"),
    @NamedQuery(name = "Houses.findByIsdeleted", query = "SELECT h FROM Houses h WHERE h.isdeleted = :isdeleted")})
public class Houses implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "availablefrom")
    private int availablefrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "availableto")
    private int availableto;

    @Column(name = "capacity")
    private Integer capacity;
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
    @Size(max = 255)
    @Column(name = "title")
    private String title;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Column(name = "isclosed")
    private Boolean isclosed;
    @Column(name = "isdeleted")
    private Boolean isdeleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "houseid")
    private List<Reservations> reservationsList;
    @OneToMany(mappedBy = "houseid", fetch=FetchType.EAGER)
    private List<Paidservices> paidservicesList;
    @OneToMany(mappedBy = "houseid", fetch=FetchType.EAGER)
    private List<Extras> extrasList;

    public Houses() {
    }

    public Houses(Integer id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsclosed() {
        return isclosed;
    }

    public void setIsclosed(Boolean isclosed) {
        this.isclosed = isclosed;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public List<Reservations> getReservationsList() {
        return reservationsList;
    }

    public void setReservationsList(List<Reservations> reservationsList) {
        this.reservationsList = reservationsList;
    }

    public List<Paidservices> getPaidservicesList() {
        return paidservicesList;
    }

    public void setPaidservicesList(List<Paidservices> paidservicesList) {
        this.paidservicesList = paidservicesList;
    }

    public List<Extras> getExtrasList() {
        return extrasList;
    }

    public void setExtrasList(List<Extras> extrasList) {
        this.extrasList = extrasList;
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
        if (!(object instanceof Houses)) {
            return false;
        }
        Houses other = (Houses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Houses[ id=" + id + " ]";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public int getAvailablefrom() {
        return availablefrom;
    }

    public void setAvailablefrom(int availablefrom) {
        this.availablefrom = availablefrom;
    }

    public int getAvailableto() {
        return availableto;
    }

    public void setAvailableto(int availableto) {
        this.availableto = availableto;
    }
    
}
