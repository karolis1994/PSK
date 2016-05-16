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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Karolis
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
    @NamedQuery(name = "Principals.findByAddress", query = "SELECT p FROM Principals p WHERE p.address = :address"),
    @NamedQuery(name = "Principals.findByPhonenumber", query = "SELECT p FROM Principals p WHERE p.phonenumber = :phonenumber"),
    @NamedQuery(name = "Principals.findByBirthdate", query = "SELECT p FROM Principals p WHERE p.birthdate = :birthdate"),
    @NamedQuery(name = "Principals.findByAbout", query = "SELECT p FROM Principals p WHERE p.about = :about"),
    @NamedQuery(name = "Principals.findByPasswordhash", query = "SELECT p FROM Principals p WHERE p.passwordhash = :passwordhash"),
    @NamedQuery(name = "Principals.findBySalt", query = "SELECT p FROM Principals p WHERE p.salt = :salt"),
    @NamedQuery(name = "Principals.findByGroupno", query = "SELECT p FROM Principals p WHERE p.groupno = :groupno"),
    @NamedQuery(name = "Principals.findByIsadmin", query = "SELECT p FROM Principals p WHERE p.isadmin = :isadmin"),
    @NamedQuery(name = "Principals.findByIsapproved", query = "SELECT p FROM Principals p WHERE p.isapproved = :isapproved"),
    @NamedQuery(name = "Principals.findByIsdeleted", query = "SELECT p FROM Principals p WHERE p.isdeleted = :isdeleted"),
    @NamedQuery(name = "Principals.findByMembershipuntill", query = "SELECT p FROM Principals p WHERE p.membershipuntill = :membershipuntill"),
    @NamedQuery(name = "Principals.findByVersion", query = "SELECT p FROM Principals p WHERE p.version = :version")})
public class Principals implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "principalid")
    private List<Reservations> reservationsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "principalid")
    private List<Payments> paymentsList;

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
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "phonenumber")
    private String phonenumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "birthdate")
    private String birthdate;
    @Size(max = 255)
    @Column(name = "about")
    private String about;
    @Size(max = 255)
    @Column(name = "passwordhash")
    private String passwordhash;
    @Size(max = 255)
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
    @Column(name = "membershipuntill")
    @Temporal(TemporalType.DATE)
    private Date membershipuntill;
    @Basic(optional = false)
    @Column(name = "version")
    private int version;

    public Principals() {
    }

    public Principals(Integer id) {
        this.id = id;
    }

    public Principals(Integer id, String email, String firstname, String lastname, String address, String phonenumber, String birthdate, int version) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phonenumber = phonenumber;
        this.birthdate = birthdate;
        this.version = version;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public Date getMembershipuntill() {
        return membershipuntill;
    }

    public void setMembershipuntill(Date membershipuntill) {
        this.membershipuntill = membershipuntill;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    @XmlTransient
    public List<Reservations> getReservationsList() {
        return reservationsList;
    }

    public void setReservationsList(List<Reservations> reservationsList) {
        this.reservationsList = reservationsList;
    }

    @XmlTransient
    public List<Payments> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<Payments> paymentsList) {
        this.paymentsList = paymentsList;
    }
    
}
