/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import DT.Facades.SettingsFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="userProfileBean")
@ViewScoped
public class UserProfileBean {  
        
    private Principals loggedInPrincipal;
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB 
    private SettingsFacade settingsFacade;
    
    private String firstname;
    private String lastname;
    private String birthdate;
    private String email;
    private String phoneNumber;
    private String address;
    private String about;    
    
    private boolean aboutField;
    private boolean pictureField;
    
    @PostConstruct
    public void init(){
        //užkrauname nustatymus
        aboutField = "true".equals(settingsFacade.getSettingByName("AboutField").getSettingvalue());
        //pictureField = "true".equals(settingsFacade.getSettingByName("PictureField").getSettingvalue());
        
        //gauname prisijungusio vartotojo objektą
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String loggedInEmail = (String) session.getAttribute("authUserEmail");
        loggedInPrincipal = (Principals) principalsFacade.findByEmail(loggedInEmail).get(0);
        
        firstname = loggedInPrincipal.getFirstname();
        lastname = loggedInPrincipal.getLastname();
        birthdate = loggedInPrincipal.getBirthdate();
        email = loggedInPrincipal.getEmail();
        phoneNumber = loggedInPrincipal.getPhonenumber();
        address = loggedInPrincipal.getAddress();
        about = loggedInPrincipal.getAbout();
        if(about == null) {
            about = "";
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public boolean isAboutField() {
        return aboutField;
    }

    public void setAboutField(boolean aboutField) {
        this.aboutField = aboutField;
    }

    public boolean isPictureField() {
        return pictureField;
    }

    public void setPictureField(boolean pictureField) {
        this.pictureField = pictureField;
    }
    
    
    
}
