/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import DT.Facades.SettingsFacade;
import DT.Services.IPasswordHasher;
import DT.Services.PasswordHasherPBKDF2;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;  
import javax.faces.bean.ManagedBean;    
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Karolis
 */
@ManagedBean(name = "registrationBean")
@ViewScoped
public class RegistrationBean implements Serializable{
    
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String email;
    @Size(min=0,max=20)
    private String firstName;
    @Size(min=0,max=25)
    private String lastName;
    @Size(min=5,max=20)
    private String password;
    private String repeatPassword;
    private boolean firstnameSetting = true;
    private boolean lastnameSetting = true;
    private Principals principal;
      
    @EJB
    private PrincipalsFacade registrationFacade;
    @EJB
    private SettingsFacade settingsFacade;
    @EJB
    private final IPasswordHasher passwordHasher = new PasswordHasherPBKDF2();

    public boolean isFirstnameSetting() {
        return firstnameSetting;
    }

    public void setFirstnameSetting(boolean firstnameSetting) {
        this.firstnameSetting = firstnameSetting;
    }

    public boolean isLastnameSetting() {
        return lastnameSetting;
    }

    public void setLastnameSetting(boolean lastnameSetting) {
        this.lastnameSetting = lastnameSetting;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
    
    @PostConstruct
    public void init(){
        //firstnameSetting = "true".equals(settingsFacade.getSettingByName("firstnamesetting"));
        //lastnameSetting = "true".equals(settingsFacade.getSettingByName("lastnamesetting"));
    }
    
    public String register() {
        if(registrationFacade.findByEmail(email).isEmpty()) {
            principal = new Principals();
            principal.setEmail(email);
            if(firstName != null)
                principal.setFirstname(firstName);   
            else principal.setFirstname("noname");       
            if(lastName != null)
                principal.setLastname(lastName); 
            else principal.setLastname("noname");
            byte[] salt = passwordHasher.createSalt();
            principal.setSalt(salt.toString());
            try {
            principal.setPasswordhash(passwordHasher.createHash(password, salt));
            } catch(Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Klaida: ", "Programos nustatymuose yra klaida. Praneškite sistemos administratoriui."));
                return "";
            }
            principal.setPoints(0);
            principal.setIsadmin(Boolean.FALSE);
            principal.setIsdeleted(Boolean.FALSE);
            principal.setIsapproved(Boolean.FALSE);
            registrationFacade.create(principal);
            return "REGISTRATION_SUCCESFUL";
        } else{
            principal = (Principals) registrationFacade.findByEmail(email).get(0);
            if(principal.getIsdeleted() != null) {
                if (principal.getIsdeleted()) {
                    return "REGISTRATION_UNSUCCESFUL_RELOG";
                }
            }          
        }
        return "REGISTRATION_UNSUCCESFUL";
    }
    
}
