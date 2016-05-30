/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Aurimas
 */
@Named
@RequestScoped
public class DisplayMembersBean {

    // Fields ------------------------------------------------------------------
    private Principals selectedPrincipal;  
    private List<Principals> allPrincipals;
   
    @EJB private PrincipalsFacade principalsFacade;

    @Inject private UserSessionBean userSessionBean;
      
    // Methods ------------------------------------------------------------------
    @PostConstruct
    public void init() {
        allPrincipals = principalsFacade.findAllNotDeleted();
    }
    public void navigateToSelected(SelectEvent event) {
        if (true != userSessionBean.getUser().getIsadmin()) {
            return;
        }
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("logged-in/administration/add-points.xhtml?id=" + selectedPrincipal.getId());
        } catch (IOException ex) {
            Logger.getLogger(DisplayMembersBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Getters / setters -------------------------------------------------------
    
    public Principals getSelectedPrincipal() { return selectedPrincipal; }
    public List<Principals> getAllPrincipals() { return allPrincipals; }
    public void setSelectedPrincipal(Principals selectedPrincipal) { this.selectedPrincipal = selectedPrincipal; }
}
