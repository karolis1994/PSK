/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Settings;
import DT.Facades.SettingsFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="changeRecommendationSettings")
public class ChangeRecommendationSettings {
     
    // Fields ------------------------------------------------------------------
    
    private Settings maxRecommendations;
    private Settings minRecommendations;
    
    @EJB
    private SettingsFacade settingsFacade;
    
    private String maxRecommendationsValue;
    private String minRecommendationsValue;
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        maxRecommendations = (Settings) settingsFacade.getSettingByName("MaxRecommendations");
        minRecommendations = (Settings) settingsFacade.getSettingByName("MinRecommendations");
        
        maxRecommendationsValue = maxRecommendations.getSettingvalue();
        minRecommendationsValue = minRecommendations.getSettingvalue();
    }
    
    //Method to change settings
    public void change() {
        int max;
        int min;
        
        //Check if input is valid
        try {
            max = Integer.parseInt(maxRecommendationsValue);
            min = Integer.parseInt(minRecommendationsValue);
        } catch(NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Įvesta ne skaičiai."));
            return;
        }
        //Check if input numbers positive
        if(max <= 0 || min <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Įvesti skaičiai negali būti mažesni už 0."));
            return;
        }
        //Check if the minimal number is not above maximum
        if(max < min) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Įvestas minimumas negali būti didesnis už maksimumą."));
            return;
        }
        
        //Setting fields and updating database
        maxRecommendations.setSettingvalue(""+max);
        minRecommendations.setSettingvalue(""+min);
        settingsFacade.edit(maxRecommendations);
        settingsFacade.edit(minRecommendations);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Nustatymai pakeisti."));
    }
    
    // Getters / setters -------------------------------------------------------
    
    public String getMaxRecommendationsValue() {
        return maxRecommendationsValue;
    }
    
    public void setMaxRecommendationsValue(String maxRecommendationsValue) {
        this.maxRecommendationsValue = maxRecommendationsValue;
    }
    
    public String getMinRecommendationsValue() {
        return minRecommendationsValue;
    }

    public void setMinRecommendationsValue(String minRecommendationsValue) {
        this.minRecommendationsValue = minRecommendationsValue;
    }
    
}
