/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Pictures;
import DT.Entities.Principals;
import DT.Facades.PicturesFacade;
import DT.Facades.PrincipalsFacade;
import DT.Facades.SettingsFacade;
import DT.Services.IPasswordHasher;
import DT.Services.PasswordHasherPBKDF2;
import facebook4j.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Past;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Karolis
 */
@Named("registrationBean")
@RequestScoped
public class RegistrationBean {

    // Fields ------------------------------------------------------------------
    private final static String NO_PASSWORD_NECESSARY = "Slaptažodis Facebook registracijai neprivalomas.";
    private final static String NOT_NECESSARY = "(Neprivaloma)";
    private final static String PASSWORD_LENGTH = "Slaptažodis turi būti 5-20 simbolių ilgio.";
    private final static String PROGRAM_ERROR = "Programos nustatymuose yra klaida. Praneškite sistemos administratoriui.";
    private final static String FB_DATE_FORMAT = "MM/DD/YYYY";
    private final static String HAVE_TO_REACTIVATE = "Naudotojas su tokiu el. paštu yra panaikinęs savo paskyrą.\nNorint ją atstatyti jums reikia prisijungti su savo senu slaptažodžiu.";
    private final static String EMAIL_ALREADY_USED = "Naudotojas su tokiu el. paštu jau egzistuoja.";

    private Principals principal;
    private Pictures picture;

    @Inject
    private PrincipalsFacade principalsFacade;
    @Inject
    private SettingsFacade settingsFacade;
    @Inject
    private UserSessionBean userSessionBean;
    @EJB
    private PicturesFacade picturesFacade;
    @EJB
    private final IPasswordHasher passwordHasher = new PasswordHasherPBKDF2();

    @Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]",
            message = "Neteisingas el. pašto formatas, teisingo pavyzdys: Jonas@gmail.lt")
    private String email;
    private String facebookID;
    private String firstname;
    private String lastname;

    @Size(max = 20) //password min size set dynamicaly(for fb login)
    private String password;
    private String repeatPassword;
    private String address;
    @Past(message = "Gimimo data negali būti didesnė už dabartinę.")
    private Date birthdate;
    @Pattern(regexp = "\\+370\\d{8}|8\\d{8}", 
            message="Neteisingas telefono formatas, teisingo pavyzdys: 862329999 arba +37062329999")
    private String phoneNumber;
    @Size(max = 250, message = "Jūsų prisistatymo limitas 250 simbolių.")
    private String about;
    private UploadedFile uploadedPicture;

    private boolean aboutField = true;
    private boolean pictureField = true;

    private boolean facebookRegistration = false;

    private String passwordInfo = "*";
    // Methods -----------------------------------------------------------------

    @PostConstruct
    public void init() {
        aboutField = "true".equals(settingsFacade.getSettingByName("AboutField").getSettingvalue());
        pictureField = "true".equals(settingsFacade.getSettingByName("PictureField").getSettingvalue());
        //userSessionBean = (UserSessionBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userSessionBean");
        if (userSessionBean != null && userSessionBean.getUserFB() != null) {
            setFacebookRegistration(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, NO_PASSWORD_NECESSARY, ""));
            passwordInfo = NOT_NECESSARY;
            loadFacebookData(userSessionBean.getUserFB());
        }
    }

    //Method to register a new principal
    public String register() {
        if (principalsFacade.findByEmail(email) == null) {
            if ((password.length() < 5 && !facebookRegistration)
                    || (password.length() > 0 && password.length() < 5)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, PASSWORD_LENGTH, "" ));
                return "";
            }

            principal = new Principals();
            principal.setFacebookid(getFacebookID());
            principal.setEmail(email);
            principal.setFirstname(firstname);
            principal.setLastname(lastname);
            if (password.length() >= 5) {
                byte[] salt = passwordHasher.createSalt();
                principal.setSalt(new String(salt));
                try {
                    principal.setPasswordhash(passwordHasher.createHash(password, salt));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, PROGRAM_ERROR, ""));
                    return "";
                }
            }
            principal.setAddress(address);
            principal.setBirthdate(birthdate);
            principal.setPhonenumber(phoneNumber);
            if (about != null) {
                principal.setAbout(about);
            }
            principal.setPoints(0);
            if(pictureField) {
                if(uploadedPicture != null) {
                    //if succesful upload set the pic, otherwise leave null
                    if(picturesFacade.uploadPicture(picture, uploadedPicture))
                        principal.setPicture(picture);
                }
            }
            if (principalsFacade.findAll().isEmpty()) {
                principal.setIsadmin(true);
                principal.setIsapproved(true);
            } else {
                principal.setIsadmin(false);
                principal.setIsapproved(false);
            }

            principal.setIsdeleted(false);

            principalsFacade.create(principal);
            userSessionBean.setUser(principal);
            return "REGISTRATION_SUCCESFUL";
        } else {
            principal = (Principals) principalsFacade.findByEmail(email);
            if (principal.getIsdeleted() != null) {
                if (principal.getIsdeleted()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, HAVE_TO_REACTIVATE, ""));
                    return null;
                }
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, EMAIL_ALREADY_USED, ""));
        return null;
    }

    //Method to register a new principal
    public void loadFacebookData(User userFB) {
        setEmail(userFB.getEmail());
        setFirstname(userFB.getFirstName());
        setLastname(userFB.getLastName());
        setFacebookID(userFB.getId());
        // principal.setAddress(locale.getDisplayCountry() + userFB. );
        SimpleDateFormat formatter = new SimpleDateFormat(FB_DATE_FORMAT);
        try {
            if (userFB.getBirthday() != null) {
                setBirthdate(formatter.parse(userFB.getBirthday()));
            }
        } catch (ParseException e) {
        }

        //principal.setPhonenumber(userFB.);
        if (userFB.getBio() != null) {
            setAbout(userFB.getBio());
        }
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

    public boolean isFacebookRegistration() {
        return facebookRegistration;
    }

    public void setFacebookRegistration(boolean facebookRegistration) {
        this.facebookRegistration = facebookRegistration;
    }

    private String getFacebookID() {
        return this.facebookID;
    }

    private void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getPasswordInfo() {
        return this.passwordInfo;
    }

    public void setPasswordInfo(String passwordInfo) {
        this.passwordInfo = passwordInfo;
    }

    public UploadedFile getUploadedPicture() {
        return uploadedPicture;
    }

    public void setUploadedPicture(UploadedFile uploadedPicture) {
        this.uploadedPicture = uploadedPicture;
    }

}
