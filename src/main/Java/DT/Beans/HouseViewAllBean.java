package DT.Beans;

import DT.Entities.Houses;
import DT.Facades.HouseFacade;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 * @author Laurynas
 */

@ManagedBean
@RequestScoped
public class HouseViewAllBean {
    
    // Fields-------------------------------------------------------------------
    
    private Houses selectedHouse;
    public Houses getSelectedHouse() { return selectedHouse; }
    public void setSelectedHouse(Houses selectedHouse) { this.selectedHouse = selectedHouse; }
        
    private List<Houses> allHouses;
    public List<Houses> getAllHouses() { return allHouses; }
    
    @EJB
    private HouseFacade houseFacade;
    
    // Methods------------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        allHouses = houseFacade.findAllNotDeleted();
    }
    
    public void navigateToSelected(SelectEvent event) {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("house-preview.xhtml?id=" + selectedHouse.getId());
        } catch (IOException ex) {
            Logger.getLogger(HouseViewAllBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
