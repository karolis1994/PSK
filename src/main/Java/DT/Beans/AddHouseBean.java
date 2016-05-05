package DT.Beans;

import DT.Entities.Houses;
import DT.Facades.HouseFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Size;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;

/**
 * @author Laurynas
 */

@ManagedBean(name = "addHouseBean")
@RequestScoped
public class AddHouseBean implements Serializable {
    
    // Fields ------------------------------------------------------------------
    
    @Size(min=1,max=255)
    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    @Size(min=1,max=255)
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Size(min=1,max=255)
    private String address;
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    @EJB
    private HouseFacade houseFacade;
    
    private Houses house;
    
    // Methods -----------------------------------------------------------------   
    
    // Saves house to the database
    public void save() {
        if (houseFacade.findWhere("o.title = '" + title + "'").isEmpty()) {
            if(house == null) {
                house = new Houses();
                house.setTitle(title);
                house.setDescription(description);
                house.setAddress(address);
                house.setIsclosed(Boolean.FALSE);
                house.setIsdeleted(Boolean.FALSE);
                
            }
            houseFacade.create(house);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Vasarnamis sukurtas."));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Klaida: ", "Vasarnamis tokiu pavadinimu jau egzistuoja."));
        }
    }
}
