/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import DT.Facades.SettingsFacade;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Karolis
 */
@Named("userProfileBean")
@RequestScoped
public class UserProfileBean {  
        
    // Fields ------------------------------------------------------------------

    private Principals shownPrincipal;
    @Inject
    private PrincipalsFacade principalsFacade;
    @Inject
    private SettingsFacade settingsFacade;
    
    @ManagedProperty(value="#{param.userID}")
    private String userID;
    private String firstname;
    private String lastname;
    private String birthdate;
    private String memberUntil;
    private String email;
    private String phoneNumber;
    private String address;
    private String about;    
    
    private boolean aboutField;
    private boolean pictureField;
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init(){
        //Loading settings
        aboutField = "true".equals(settingsFacade.getSettingByName("AboutField").getSettingvalue());
        //pictureField = "true".equals(settingsFacade.getSettingByName("PictureField").getSettingvalue());
        
        //Choosing the user by the managedproperty key, if no such member throwing an error message
        int principalID;
        try {
            principalID = Integer.parseInt(userID);
            shownPrincipal = principalsFacade.find(principalID);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Klaida: ", "ID formatas netinkamas."));
            return;
        }
        if(shownPrincipal == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Klaida: ", "Naudotojas su tokiu ID neegzistuoja."));
            return;
        }
        
        //Setting to be shown fields
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
        firstname = shownPrincipal.getFirstname();
        lastname = shownPrincipal.getLastname();
        birthdate = sdf.format(shownPrincipal.getBirthdate());
        if(shownPrincipal.getMembershipuntill() == null) {
            memberUntil = "Ne narys";
        } else {
            memberUntil = sdf.format(shownPrincipal.getMembershipuntill());
        }
        email = shownPrincipal.getEmail();
        phoneNumber = shownPrincipal.getPhonenumber();
        address = shownPrincipal.getAddress();
        about = shownPrincipal.getAbout();
        if(about == null) {
            about = "";
        }
    }
    
    // Getters / setters -------------------------------------------------------
    
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getAbout() {
        return about;
    }

    public boolean isAboutField() {
        return aboutField;
    }

    public boolean isPictureField() {
        return pictureField;
    }  

    public String getMemberUntil() {
        return memberUntil;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public String getEmptyString() { 
        return ""; 
    }
    
}
