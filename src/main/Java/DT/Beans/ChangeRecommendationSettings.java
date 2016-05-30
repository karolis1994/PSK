/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Settings;
import DT.Facades.SettingsFacade;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Karolis
 */
@Named("changeRecommendationSettingsBean")
@RequestScoped
public class ChangeRecommendationSettings {
     
    // Fields ------------------------------------------------------------------
    private final static String INPUT_NOT_NUMBER = "Įvesta ne skaičiai.";
    private final static String INPUT_NUMBER_TOO_LOW = "Įvesti skaičiai negali būti mažesni už 0.";
    private final static String MINIMUM_TOO_HIGH = "Įvestas minimumas negali būti didesnis už maksimumą.";
    private final static String SETTINGS_CHANGED = "Nustatymai pakeisti.";
    
    private Settings maxRecommendations;
    private Settings minRecommendations;
    
    @Inject
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, INPUT_NOT_NUMBER, ""));
            return;
        }
        //Check if input numbers positive
        if(max <= 0 || min <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, INPUT_NUMBER_TOO_LOW, ""));
            return;
        }
        //Check if the minimal number is not above maximum
        if(max < min) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, MINIMUM_TOO_HIGH, ""));
            return;
        }
        
        //Setting fields and updating database
        maxRecommendations.setSettingvalue(""+max);
        minRecommendations.setSettingvalue(""+min);
        settingsFacade.edit(maxRecommendations);
        settingsFacade.edit(minRecommendations);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, SETTINGS_CHANGED, ""));
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
