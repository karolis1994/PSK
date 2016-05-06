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
    
    private String maxRecommendationsValue;
    public String getMaxRecommendationsValue() {
        return maxRecommendationsValue;
    }
    public void setMaxRecommendationsValue(String maxRecommendationsValue) {
        this.maxRecommendationsValue = maxRecommendationsValue;
    }

    private String minRecommendationsValue;
    public String getMinRecommendationsValue() {
        return minRecommendationsValue;
    }

    public void setMinRecommendationsValue(String minRecommendationsValue) {
        this.minRecommendationsValue = minRecommendationsValue;
    }
     
    private Settings maxRecommendations;
    private Settings minRecommendations;
    
    @EJB
    private SettingsFacade settingsFacade;
    
    @PostConstruct
    public void init() {
        maxRecommendations = (Settings) settingsFacade.getSettingByName("MaxRecommendations");
        minRecommendations = (Settings) settingsFacade.getSettingByName("MinRecommendations");
        
        maxRecommendationsValue = maxRecommendations.getSettingvalue();
        minRecommendationsValue = minRecommendations.getSettingvalue();
    }
    
    public void change() {
        int max;
        int min;
        //Patikriname ar įvesta skaičiai
        try {
            max = Integer.parseInt(maxRecommendationsValue);
            min = Integer.parseInt(minRecommendationsValue);
        } catch(NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Įvesta ne skaičiai."));
            return;
        }
        //Patikriname ar įvesta teigiami skaičiai
        if(max <= 0 || min <= 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Įvesti skaičiai negali būti mažesni už 0."));
            return;
        }
        //Patikriname ar reikalaujamas minimalus skaičius patvirtinimų neviršija maksimaliai galimų išsiūsti žinučių
        if(max < min) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Įvestas minimumas negali būti didesnis už maksimumą."));
            return;
        }
        
        maxRecommendations.setSettingvalue(""+max);
        minRecommendations.setSettingvalue(""+min);
        settingsFacade.edit(maxRecommendations);
        settingsFacade.edit(minRecommendations);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Nustatymai pakeisti."));
    }
    
}
