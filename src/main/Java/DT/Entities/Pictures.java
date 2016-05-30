/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Karolis
 */
@Entity
@Table(name = "pictures")
@NamedQueries({
    @NamedQuery(name = "Pictures.findAll", query = "SELECT p FROM Pictures p"),
    @NamedQuery(name = "Pictures.findById", query = "SELECT p FROM Pictures p WHERE p.id = :id"),
    @NamedQuery(name = "Pictures.findByImagename", query = "SELECT p FROM Pictures p WHERE p.imagename = :imagename")})
public class Pictures implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "imagename")
    private String imagename;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "image")
    private byte[] image;
    @OneToMany(mappedBy = "picture")
    private Collection<Principals> principalsCollection;

    public Pictures() {
    }

    public Pictures(Integer id) {
        this.id = id;
    }

    public Pictures(Integer id, String imagename, byte[] image) {
        this.id = id;
        this.imagename = imagename;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Collection<Principals> getPrincipalsCollection() {
        return principalsCollection;
    }

    public void setPrincipalsCollection(Collection<Principals> principalsCollection) {
        this.principalsCollection = principalsCollection;
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
        if (!(object instanceof Pictures)) {
            return false;
        }
        Pictures other = (Pictures) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Pictures[ id=" + id + " ]";
    }
    
}
