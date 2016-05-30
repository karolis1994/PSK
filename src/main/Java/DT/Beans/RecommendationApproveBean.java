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
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Karolis
 */
@Named("recommendationApproveBean")
public class RecommendationApproveBean {
    
    // Fields ------------------------------------------------------------------
    private final static String REQUEST_APPROVED = "Prašymas patvirtintas.";
    private final static String REQUEST_NOT_SENT_TO_YOU = "Šio prašymo jūs negalite patvirtinti, nes jis nebuvo siųstas jums.";
    private final static String INVALID_KEY = "Toks raktas neegzistuoja.";
    private final static String PROGRAM_ERROR = "Programos nustatymuose yra klaida. Praneškite sistemos administratoriui.";
    private final static String ERROR = "Klaida:";
    
    private Recommendations recommendation;
    private Principals principal;
    private Principals loggedInPrincipal;
    private Settings minRecommendations;
      
    @Inject
    private PrincipalsFacade principalsFacade;
    @Inject
    private RecommendationsFacade recommendationsFacade;
    @Inject
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
        key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("key");
        if(key != null)
            isRecommendationValid = approve();
        else
            isRecommendationValid = false;
    }
    
    //Method checking if the approval message is correct
    public boolean approve() {
        recommendation = recommendationsFacade.findByURLCode(key);          
        if(recommendation == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "", INVALID_KEY));
            return false;
        }

        //Check if logged in user is the one to whom this email was sent
        if(recommendation.getRecieverid().equals(loggedInPrincipal)) {
            //approve recommendation
            recommendation.setIsactivated(Boolean.TRUE);
            recommendationsFacade.edit(recommendation);
            return checkIfApproved();
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_WARN, "", REQUEST_NOT_SENT_TO_YOU));
            return false;
        }
    }
    
    //Method to check if there are enough approvals
    public boolean checkIfApproved() {
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
            int groupNumber = 1;
            try {
                groupNumber = Integer.parseInt(settingsFacade.getSettingByName("GroupNumber").getSettingvalue());
            } catch(NumberFormatException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, ERROR, PROGRAM_ERROR));
            }
            principal.setGroupno(groupNumber);
            principalsFacade.edit(principal);
            return true;
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
