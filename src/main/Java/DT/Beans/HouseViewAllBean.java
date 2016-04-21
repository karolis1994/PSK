package DT.Beans;

import DT.Entities.Houses;
import DT.Facades.HouseFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 * @author Laurynas
 */

@ManagedBean
@RequestScoped
public class HouseViewAllBean {
    
    // Fields-------------------------------------------------------------------
    
    
    private List<Houses> allHouses;
    public List<Houses> getAllHouses() {
        return allHouses;
    }
    
    @EJB
    private HouseFacade houseFacade;
    
    // Methods------------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        allHouses = houseFacade.findAll();
    }
}
