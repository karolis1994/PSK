/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.PrincipalsFacade;
import DT.Facades.ReservationFacade;
import DT.Facades.SettingsFacade;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Karolis
 */
@Named("userProfileChangeBean")
@ViewScoped
public class UserProfileChangeBean {
    
    // Fields ------------------------------------------------------------------
    private Principals loggedInPrincipal;
    private List<Reservations> reservations;
    
    @Inject
    private SettingsFacade settingsFacade;
    @Inject
    private PrincipalsFacade principalsFacade;
    @Inject
    private ReservationFacade reservationsFacade;
    
    @Inject
    private UserSessionBean userSessionBean;
    
    private String firstname;
    private String lastname;
    @Past
    private Date birthdate;
    private String memberUntil;
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String email;
    @Pattern(regexp="\\+370\\d{8}|8\\d{8}")
    private String phoneNumber;
    private String address;
    private String about;  
    private String points;
    
    private boolean aboutField;
    private boolean pictureField;
    
    private SimpleDateFormat sdf;
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init(){
        loggedInPrincipal = userSessionBean.getUser();
        
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        firstname = loggedInPrincipal.getFirstname();
        lastname = loggedInPrincipal.getLastname();
        birthdate = loggedInPrincipal.getBirthdate();  
        if(loggedInPrincipal.getMembershipuntill() == null) {
            memberUntil = "Ne narys";
        } else {
            memberUntil = sdf.format(loggedInPrincipal.getMembershipuntill());
        }
        email = loggedInPrincipal.getEmail();
        phoneNumber = loggedInPrincipal.getPhonenumber();
        address = loggedInPrincipal.getAddress();
        about = loggedInPrincipal.getAbout();
        if(about == null) {
            about = "";
        }
        points = loggedInPrincipal.getPoints().toString();
        aboutField = "true".equals(settingsFacade.getSettingByName("AboutField").getSettingvalue());
        pictureField = "true".equals(settingsFacade.getSettingByName("PictureField").getSettingvalue());
    }
    
    //Method to unregister user
    public String unregister(){
        //Change principal status and cancel member subscription
        loggedInPrincipal.setIsdeleted(Boolean.TRUE);
        loggedInPrincipal.setMembershipuntill(null);
        
        //Cancel all reservations made by the user
        reservations = reservationsFacade.findByPrincipalNotCanceled(loggedInPrincipal);
        if(!reservations.isEmpty()) {
            for(Reservations r : reservations) {
                r.setIscanceled(true);
            }
        }
        
        //Update database
        principalsFacade.edit(loggedInPrincipal);
        if(!reservations.isEmpty()) {
            for(Reservations r : reservations) {
                reservationsFacade.edit(r);
            } 
        }
        
        //Disconnect the user
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "LOGOUT";
    }
    
    //Method to update the changed fields
    public void update() {
        loggedInPrincipal.setFirstname(firstname);
        loggedInPrincipal.setLastname(lastname);
        loggedInPrincipal.setBirthdate(birthdate);
        loggedInPrincipal.setAddress(address);
        loggedInPrincipal.setPhonenumber(phoneNumber);
        loggedInPrincipal.setEmail(email);
        if(about != null) {
            loggedInPrincipal.setAbout(about);
        }
        
        principalsFacade.edit(loggedInPrincipal);
        userSessionBean.setUser(loggedInPrincipal);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Jūsų profilis atnaujintas."));
    }

    // Getters / setters -------------------------------------------------------

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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getMemberUntil() {
        return memberUntil;
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

    public String getPoints() {
        return points;
    }

    public boolean isAboutField() {
        return aboutField;
    }

    public boolean isPictureField() {
        return pictureField;
    }  
    
}
