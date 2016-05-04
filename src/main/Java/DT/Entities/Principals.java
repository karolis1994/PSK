/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Laurynas
 */
@Entity
@Table(name = "principals")
@NamedQueries({
    @NamedQuery(name = "Principals.findAll", query = "SELECT p FROM Principals p"),
    @NamedQuery(name = "Principals.findById", query = "SELECT p FROM Principals p WHERE p.id = :id"),
    @NamedQuery(name = "Principals.findByEmail", query = "SELECT p FROM Principals p WHERE p.email = :email"),
    @NamedQuery(name = "Principals.findByPoints", query = "SELECT p FROM Principals p WHERE p.points = :points"),
    @NamedQuery(name = "Principals.findByFirstname", query = "SELECT p FROM Principals p WHERE p.firstname = :firstname"),
    @NamedQuery(name = "Principals.findByLastname", query = "SELECT p FROM Principals p WHERE p.lastname = :lastname"),
    @NamedQuery(name = "Principals.findByPasswordhash", query = "SELECT p FROM Principals p WHERE p.passwordhash = :passwordhash"),
    @NamedQuery(name = "Principals.findBySalt", query = "SELECT p FROM Principals p WHERE p.salt = :salt"),
    @NamedQuery(name = "Principals.findByGroupno", query = "SELECT p FROM Principals p WHERE p.groupno = :groupno"),
    @NamedQuery(name = "Principals.findByIsadmin", query = "SELECT p FROM Principals p WHERE p.isadmin = :isadmin"),
    @NamedQuery(name = "Principals.findByIsapproved", query = "SELECT p FROM Principals p WHERE p.isapproved = :isapproved"),
    @NamedQuery(name = "Principals.findByIsdeleted", query = "SELECT p FROM Principals p WHERE p.isdeleted = :isdeleted")})
public class Principals implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Column(name = "points")
    private Integer points;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "firstname")
    private String firstname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "lastname")
    private String lastname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "passwordhash")
    private String passwordhash;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "salt")
    private String salt;
    @Column(name = "groupno")
    private Integer groupno;
    @Column(name = "isadmin")
    private Boolean isadmin;
    @Column(name = "isapproved")
    private Boolean isapproved;
    @Column(name = "isdeleted")
    private Boolean isdeleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "principalid")
    private List<Reservations> reservationsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "principals")
    private List<Invitations> invitationsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "principals1")
    private List<Invitations> invitationsList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "principalid")
    private List<Payments> paymentsList;

    public Principals() {
    }

    public Principals(Integer id) {
        this.id = id;
    }

    public Principals(Integer id, String email, String firstname, String lastname, String passwordhash) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.passwordhash = passwordhash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public Integer getGroupno() {
        return groupno;
    }

    public void setGroupno(Integer groupno) {
        this.groupno = groupno;
    }

    public Boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }

    public Boolean getIsapproved() {
        return isapproved;
    }

    public void setIsapproved(Boolean isapproved) {
        this.isapproved = isapproved;
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

    public List<Invitations> getInvitationsList() {
        return invitationsList;
    }

    public void setInvitationsList(List<Invitations> invitationsList) {
        this.invitationsList = invitationsList;
    }

    public List<Invitations> getInvitationsList1() {
        return invitationsList1;
    }

    public void setInvitationsList1(List<Invitations> invitationsList1) {
        this.invitationsList1 = invitationsList1;
    }

    public List<Payments> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<Payments> paymentsList) {
        this.paymentsList = paymentsList;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
        if (!(object instanceof Principals)) {
            return false;
        }
        Principals other = (Principals) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Principals[ id=" + id + " ]";
    }
    
}
