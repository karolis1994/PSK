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
    private boolean firstname;
    public boolean isFirstname() {
        return firstname;
    }
    public void setFirstname(boolean firstname) {
        this.firstname = firstname;
    }   
    private boolean lastname;
    public boolean isLastname() {
        return lastname;
    }
    public void setLastname(boolean lastname) {
        this.lastname = lastname;
    }
    private Settings firstnameSettings;
    private Settings lastnameSettings;
    @EJB
    SettingsFacade settingsFacade;
    
    @PostConstruct
    public void init() {
        firstnameSettings = settingsFacade.getFirstnameFieldSettings();
        lastnameSettings = settingsFacade.getLastnameFieldSettings();
        firstname = "true".equals(firstnameSettings.getSettingvalue());
        lastname = "true".equals(lastnameSettings.getSettingvalue());
    }
    
    
    public void change() {
        firstnameSettings.setSettingvalue(String.valueOf(firstname));
        lastnameSettings.setSettingvalue(String.valueOf(lastname));
        settingsFacade.edit(firstnameSettings);
        settingsFacade.edit(lastnameSettings);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Settings updated"));
    }
}
