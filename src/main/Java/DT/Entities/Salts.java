/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Laurynas
 */
@Entity
@Table(name = "salts")
@NamedQueries({
    @NamedQuery(name = "Salts.findAll", query = "SELECT s FROM Salts s"),
    @NamedQuery(name = "Salts.findById", query = "SELECT s FROM Salts s WHERE s.id = :id"),
    @NamedQuery(name = "Salts.findBySalt", query = "SELECT s FROM Salts s WHERE s.salt = :salt")})
public class Salts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "salt")
    private int salt;

    public Salts() {
    }

    public Salts(Integer id) {
        this.id = id;
    }

    public Salts(Integer id, int salt) {
        this.id = id;
        this.salt = salt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
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
        if (!(object instanceof Salts)) {
            return false;
        }
        Salts other = (Salts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.Salts[ id=" + id + " ]";
    }
    
}
