package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Facades.ExtrasFacade;
import DT.Facades.HouseFacade;
import DT.Facades.PaidServicesFacade;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @author Laurynas
 */

@Named("addExtraBean")
@RequestScoped
public class AddExtraBean {
    
    private String title;
    private String description;
    private double cost;
    private int costInPoints;

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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getCostInPoints() {
        return costInPoints;
    }

    public void setCostInPoints(int costInPoints) {
        this.costInPoints = costInPoints;
    }

    public Houses getHouse() {
        return house;
    }

    public void setHouse(Houses house) {
        this.house = house;
    }

    public HouseFacade getHousesFacade() {
        return housesFacade;
    }

    public void setHousesFacade(HouseFacade housesFacade) {
        this.housesFacade = housesFacade;
    }
    
    Houses house;
    
    @Inject
    HouseFacade housesFacade;
    @Inject
    PaidServicesFacade paidServicesFacade;
    @Inject
    ExtrasFacade extrasFacade;
    
    @PostConstruct
    void init() {
        String houseID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("house");
        
        if (houseID == null || houseID.length() <= 0) {
            return;
        }
        
        house = housesFacade.find(Integer.parseInt(houseID));
    }
    
    public void save() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, IOException {
        
        Paidservices paidService = new Paidservices();
        Extras extra = new Extras();
        
        extra.setTitle(title);
        extra.setDescription(description);
        extra.setHouseid(house);
        extrasFacade.create(extra);
        
        paidService.setCost(cost);
        paidService.setCostInPoints(costInPoints);
        paidService.setExtrasid(extra);
        paidServicesFacade.create(paidService);
        
        List<Paidservices> paidServiceExtras = extra.getPaidservicesList();
        if (paidServiceExtras == null) {
            paidServiceExtras = new LinkedList<>();
        }
        paidServiceExtras.add(paidService);
        extra.setPaidservicesList(paidServiceExtras);
        extrasFacade.edit(extra);
        
        List<Extras> extras = house.getExtrasList();
        if (extras == null) {
            extras = new LinkedList<>();
        }
        extras.add(extra);
        house.setExtrasList(extras);
        
        housesFacade.edit(house);
        
        
        
        FacesContext.getCurrentInstance()
            .getExternalContext()
            .redirect("../house-preview.xhtml?id=" + house.getId());
   }
}
