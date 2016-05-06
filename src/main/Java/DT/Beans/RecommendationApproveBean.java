/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Invitations;
import DT.Entities.Principals;
import DT.Entities.Recommendations;
import DT.Entities.Settings;
import DT.Facades.InvitationsFacade;
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
    
    private Recommendations recommendation;
    private Principals principal;
    private Principals loggedInPrincipal;
    private Settings minRecommendations;
    
    private int currentlyAcceptedRecommendations;
    
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private RecommendationsFacade recommendationsFacade;
    @EJB
    private SettingsFacade settingsFacade;
    
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

        minRecommendations = (Settings) settingsFacade.getSettingByName("MinRecommendations");
              
        if(key != null)
            valid = approve();
        else
            valid = false;
    }
    
    public boolean approve() {
        try {
            recommendation = (Recommendations) recommendationsFacade.findByURLCode(key).get(0);          
        } catch(Exception e) {
            return false;
        }
        /*if(recommendation.getIsactivated()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Jūs jau esate patvirtinęs šį prašymą."));
            return true;
        }*/
        
        //Patikriname ar prisijungęs vartotojas yra tas, kuriam buvo siūstas prašymas
        if(recommendation.getRecieverid().equals(loggedInPrincipal)) {
            //patvirtiname rekomendaciją
            recommendation.setIsactivated(Boolean.TRUE);
            recommendationsFacade.edit(recommendation);
            
            //Patikriname ar kol kas patvirtintų pakvietimų skaičius tenkina reikiamą ribą
            currentlyAcceptedRecommendations = recommendationsFacade
                    .findByApprovedSender(recommendation.getSenderid().getId()).size();      
            int minRecommendationsCount = Integer.parseInt(minRecommendations.getSettingvalue());
            System.out.println("test   "+currentlyAcceptedRecommendations+" " + minRecommendationsCount);
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
}
