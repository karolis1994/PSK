package DT.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "otherservices")
@NamedQueries({
    @NamedQuery(name = "OtherService.findAll", query = "SELECT o FROM OtherService o"),
    @NamedQuery(name = "OtherService.findById", query = "SELECT o FROM OtherService o WHERE o.id = :id"),
    @NamedQuery(name = "OtherService.findByTitle", query = "SELECT o FROM OtherService o WHERE o.title = :title"),
    @NamedQuery(name = "OtherService.findByDescription", query = "SELECT o FROM OtherService o WHERE o.description = :description"),
    @NamedQuery(name = "OtherService.findByIsdeleted", query = "SELECT o FROM OtherService o WHERE o.isdeleted = :isdeleted")})
public class OtherService implements Serializable {

    @OneToMany(mappedBy = "otherserviceid")
    private List<Paidservices> paidservicesList;

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

    public OtherService() {
    }

    public OtherService(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtherService)) {
            return false;
        }
        OtherService other = (OtherService) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DT.Entities.OtherService[ id=" + id + " ]";
    }

    public List<Paidservices> getPaidservicesList() {
        return paidservicesList;
    }

    public void setPaidservicesList(List<Paidservices> paidservicesList) {
        this.paidservicesList = paidservicesList;
    }
    
}
