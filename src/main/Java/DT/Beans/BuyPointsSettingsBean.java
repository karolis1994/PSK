package DT.Beans;

import DT.Entities.Paidservices;
import DT.Facades.PaidServicesFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Laurynas
 */

@Named("buyPointsSettingsBean")
@RequestScoped
public class BuyPointsSettingsBean {
    
    @Inject
    private Paidservices buyPoints;
    
    @EJB
    private PaidServicesFacade paidServicesFacade;
    
    @PostConstruct
    void init() {
        buyPoints = paidServicesFacade.find(2);
    }
    
    public Paidservices getPoints() { return buyPoints; }
    
    public void saveChanges() {
        paidServicesFacade.edit(buyPoints);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Išsaugota."));
    }
}
