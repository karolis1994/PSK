/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Entities.Recommendations;
import DT.Entities.Settings;
import DT.Facades.PrincipalsFacade;
import DT.Facades.RecommendationsFacade;
import DT.Facades.SettingsFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="recommendationApproveBean")
@RequestScoped
public class RecommendationApproveBean {
    
    // Fields ------------------------------------------------------------------
    
    private Recommendations recommendation;
    private Principals principal;
    private Principals loggedInPrincipal;
    private Settings minRecommendations;
      
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private RecommendationsFacade recommendationsFacade;
    @EJB
    private SettingsFacade settingsFacade;
    
    @Inject
    private UserSessionBean userSessionBean;
    
    @ManagedProperty(value="#{param.key}")
    private String key;
    public boolean valid;
    private int currentlyAcceptedRecommendations; 
     
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init() {       
        loggedInPrincipal = userSessionBean.getUser(); 
        
        minRecommendations = (Settings) settingsFacade.getSettingByName("MinRecommendations");           
        if(key != null)
            valid = approve();
        else
            valid = false;
    }
    
    //Method checking if the approval message is correct
    public boolean approve() {
        try {
            recommendation = (Recommendations) recommendationsFacade.findByURLCode(key).get(0);          
        } catch(Exception e) {
            return false;
        }
        
        //Check if logged in user is the one to whom this email was sent
        if(recommendation.getRecieverid().equals(loggedInPrincipal)) {
            //approve recommendation
            recommendation.setIsactivated(Boolean.TRUE);
            recommendationsFacade.edit(recommendation);
            
            //Check if there are enough approvals
            currentlyAcceptedRecommendations = recommendationsFacade
                    .findByApprovedSender(recommendation.getSenderid().getId()).size();      
            int minRecommendationsCount = Integer.parseInt(minRecommendations.getSettingvalue());
            if(currentlyAcceptedRecommendations < minRecommendationsCount) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "", "Prašymas patvirtintas"));
                return true;
            } else {      
                principal = recommendation.getSenderid();
                principal.setIsapproved(true);
                principalsFacade.edit(principal);
                return true;
            }
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Šio prašymo jūs negalite patvirtinti, nes jis nebuvo siūstas jums."));
            return false;
        }
    }
    
    // Getters / setters -------------------------------------------------------
    
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getEmptyString() { 
        return ""; 
    }
}
