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
@ManagedBean(name="changeApplicationForm")
public class ChangeApplicationForm {
       
    // Fields ------------------------------------------------------------------
    
    private Settings PictureSettings;
    private Settings AboutSettings;
    
    @EJB
    SettingsFacade settingsFacade;
    
    private boolean picture;    
    private boolean about;   
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        //Loading settings 
        AboutSettings = settingsFacade.getSettingByName("AboutField");
        PictureSettings = settingsFacade.getSettingByName("PictureField");
        
        about = "true".equals(AboutSettings.getSettingvalue());
        picture = "true".equals(PictureSettings.getSettingvalue());
        
    }

    //Method to change settings
    public void change() {
        //Setting fields and updating database
        AboutSettings.setSettingvalue(String.valueOf(about)); 
        PictureSettings.setSettingvalue(String.valueOf(picture));                      
        settingsFacade.edit(AboutSettings);
        settingsFacade.edit(PictureSettings);                
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Forma atnaujinta"));
    }

    // Getters / setters -------------------------------------------------------
    
    public boolean isPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture = picture;
    }

    public boolean isAbout() {
        return about;
    }

    public void setAbout(boolean about) {
        this.about = about;
    }  
    
}
