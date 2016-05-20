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
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;  
import javax.faces.bean.ManagedBean;    
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Past;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Karolis
 */
@ManagedBean(name = "registrationBean")
@ViewScoped
public class RegistrationBean implements Serializable{
    
    // Fields ------------------------------------------------------------------
    
    private Principals principal;
      
    @EJB
    private PrincipalsFacade registrationFacade;
    @EJB
    private SettingsFacade settingsFacade;
    @EJB
    private final IPasswordHasher passwordHasher = new PasswordHasherPBKDF2();
    
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String email;
    @Size(min=0,max=20)
    private String firstname;
    @Size(min=0,max=25)
    private String lastname;
    @Size(min=5,max=20)
    private String password;
    private String repeatPassword;
    private String address;
    @Past
    private Date birthdate;
    @Pattern(regexp="\\+370\\d{8}|8\\d{8}")
    private String phoneNumber;
    @Size(max=250)
    private String about;
    private UploadedFile picture;
    
    private boolean aboutField = true;
    private boolean pictureField = true;      
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init(){
        aboutField = "true".equals(settingsFacade.getSettingByName("AboutField").getSettingvalue());
        pictureField = "true".equals(settingsFacade.getSettingByName("PictureField").getSettingvalue());
    }
    
    //Method to register a new principal
    public String register() {
        if(registrationFacade.findByEmail(email).isEmpty()) {
            principal = new Principals();
            principal.setEmail(email);
            principal.setFirstname(firstname);
            principal.setLastname(lastname);
            byte[] salt = passwordHasher.createSalt();
            principal.setSalt(salt.toString());
            try {
                principal.setPasswordhash(passwordHasher.createHash(password, salt));
            } catch(Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Klaida: ", "Programos nustatymuose yra klaida. Praneškite sistemos administratoriui."));
                return "";
            }
            principal.setAddress(address);
            principal.setBirthdate(birthdate);
            principal.setPhonenumber(phoneNumber);
            if(about != null) {
                principal.setAbout(about);
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

    // Getters / setters -------------------------------------------------------
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
