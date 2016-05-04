/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Invitations;
import DT.Entities.Principals;
import DT.Facades.InvitationsFacade;
import DT.Facades.PrincipalsFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="recommendationApproveBean")
@RequestScoped
public class RecommendationApproveBean {
    
    @ManagedProperty(value="#{param.key}")
    private String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    
    public boolean valid;
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private InvitationsFacade invitationFacade;
    private Invitations invitation;
    private Principals principal;
    private Principals loggedInPrincipal;
    
    @PostConstruct
    public void init() {
        //Gauname prisijungusio naudotojo objekta
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String loggedInEmail = (String) session.getAttribute("authUserEmail");
        if(loggedInEmail == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Norint patvirtinti šį prašyma jums reikia prisijungti."));
            valid = false;
            return;
        }
        loggedInPrincipal = (Principals) principalsFacade.findByEmail(loggedInEmail).get(0);
        
        if(key != null)
            valid = approve();
        else
            valid = false;
    }
    
    public boolean approve() {
        try {
            invitation = (Invitations) invitationFacade.findByURLCode(key).get(0);          
        } catch(Exception e) {
            return false;
        }
        //if(invitation.getPrincipals1().equals( prisijunges vartotojas )) {
            principal = invitation.getPrincipals1();
            principal.setIsapproved(true);
            principalsFacade.edit(principal);
            return true;
        //}
        //else
            //return false;
    }
}
