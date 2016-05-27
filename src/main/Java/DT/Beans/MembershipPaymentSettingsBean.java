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

@Named("membershipPaymentSettingsBean")
@RequestScoped
public class MembershipPaymentSettingsBean {
    
    @Inject
    private Paidservices membership;
    
    @EJB
    private PaidServicesFacade paidServicesFacade;
    
    @PostConstruct
    void init() {
        membership = paidServicesFacade.find(1);
    }
    
    public Paidservices getMembership() { return membership; }
    
    public void saveChanges() {
        paidServicesFacade.edit(membership);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Išsaugota."));
    }
}
