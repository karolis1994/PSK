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
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Karolis
 */
@Named("recommendationApproveBean")
public class RecommendationApproveBean {
    
    // Fields ------------------------------------------------------------------
    private final static String REQUEST_APPROVED = "Prašymas patvirtintas";
    private final static String REQUEST_NOT_SENT_TO_YOU = "Šio prašymo jūs negalite patvirtinti, nes jis nebuvo siųstas jums.";
    
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
    
    private String key;
    public boolean isRecommendationValid;
    private int currentlyAcceptedRecommendations; 
     
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init() {       
        loggedInPrincipal = userSessionBean.getUser(); 
        
        minRecommendations = (Settings) settingsFacade.getSettingByName("MinRecommendations");           
        if(key != null)
            isRecommendationValid = approve();
        else
            isRecommendationValid = false;
        
        key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");
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
                        FacesMessage.SEVERITY_INFO, "", REQUEST_APPROVED));
                return true;
            } else {      
                principal = recommendation.getSenderid();
                principal.setIsapproved(true);
                principalsFacade.edit(principal);
                return true;
            }
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", REQUEST_NOT_SENT_TO_YOU));
            return false;
        }
    }
    
    // Getters / setters -------------------------------------------------------
    
    public boolean isIsRecommendationValid() {
        return isRecommendationValid;
    }
    public void setIsRecommendationValid(boolean isRecommendationValid) {
        this.isRecommendationValid = isRecommendationValid;
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
