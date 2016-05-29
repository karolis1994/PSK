/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Pictures;
import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.PicturesFacade;
import DT.Facades.PrincipalsFacade;
import DT.Facades.ReservationFacade;
import DT.Facades.SettingsFacade;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.Size;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Karolis
 */
@Named("userProfileChangeBean")
@RequestScoped
public class UserProfileChangeBean{
    
    // Fields ------------------------------------------------------------------
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String PROFILE_UPDATED = "Jūsų profilis atnaujintas.";
    private final static String MEMBERSHIP_EXPIRED = "Pasibaigusi narystė";
    private final static String NOT_A_MEMBER = "Ne narys";
    private final static String ERROR = "Klaida: ";
    private final static String PICTURE_ERROR = "Jūsų paveikslėlis nebuvo atnaujintas.";
    
    private Principals loggedInPrincipal;
    private List<Reservations> reservations;
    private Pictures picture;
    
    @Inject
    private SettingsFacade settingsFacade;
    @Inject
    private PrincipalsFacade principalsFacade;
    @Inject
    private ReservationFacade reservationsFacade;
    @EJB
    private PicturesFacade picturesFacade;
    @Inject
    private UserSessionBean userSessionBean;

    private String firstname;
    private String lastname;
    @Past
    private Date birthdate;
    private String memberUntil;
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]",
            message = "Neteisingas formatas, teisingo pavyzdys: Jonas@gmail.lt")
    private String email;
    @Pattern(regexp="\\+370\\d{8}|8\\d{8}", message="Neteisingas formatas, teisingo pavyzdys: 862329999 arba +37062329999")
    private String phoneNumber;
    private String address;
    private String about;  
    private String points;
    private UploadedFile uploadedPicture;
    
    private boolean aboutField;
    private boolean pictureField;
    
    private SimpleDateFormat sdf;
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init(){
        //Load settings
        aboutField = "true".equals(settingsFacade.getSettingByName("AboutField").getSettingvalue());
        pictureField = "true".equals(settingsFacade.getSettingByName("PictureField").getSettingvalue());

        loggedInPrincipal = userSessionBean.getUser();
        
        sdf = new SimpleDateFormat(DATE_FORMAT);
        
        firstname = loggedInPrincipal.getFirstname();
        lastname = loggedInPrincipal.getLastname();
        birthdate = loggedInPrincipal.getBirthdate();
        
        if(loggedInPrincipal.getMembershipuntill() != null && new Date().after(loggedInPrincipal.getMembershipuntill()))
            memberUntil = MEMBERSHIP_EXPIRED;
        else if(loggedInPrincipal.getMembershipuntill() == null)
            memberUntil = NOT_A_MEMBER;  
        else
            memberUntil = sdf.format(loggedInPrincipal.getMembershipuntill());
            
        email = loggedInPrincipal.getEmail();
        phoneNumber = loggedInPrincipal.getPhonenumber();
        address = loggedInPrincipal.getAddress();
        about = loggedInPrincipal.getAbout();
        if(about == null) {
            about = "";
        }
        points = loggedInPrincipal.getPoints().toString();
    }
    
    //Method to unregister user
    public String unregister(){
        //Change principal status and cancel member subscription
        loggedInPrincipal.setIsdeleted(Boolean.TRUE);
        loggedInPrincipal.setMembershipuntill(null);
        /*
        pictureFacade.remove(loggedInPrincipal.getPicture());
        */
        
        //Cancel all reservations made by the user
        reservations = reservationsFacade.findByPrincipalNotCanceled(loggedInPrincipal);
        if(!reservations.isEmpty()) {
            for(Reservations r : reservations) {
                r.setIscanceled(true);
            }
        }
        
        //Update database
        principalsFacade.remove(loggedInPrincipal);
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
        //Create a new picture
        picture = new Pictures();
        Pictures temp = null;
        if(uploadedPicture != null) {
            //If the picture creation failed skip picture setting
            if(picturesFacade.uploadPicture(picture, uploadedPicture)) {
                //If the user already has a picture, copy it to delete it later
                if(loggedInPrincipal.getPicture() != null && loggedInPrincipal.getPicture().getId() != 1)
                    temp = loggedInPrincipal.getPicture();
                loggedInPrincipal.setPicture(picture);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, PICTURE_ERROR));
            }
        }
        principalsFacade.edit(loggedInPrincipal);
        if(temp != null)
            picturesFacade.remove(temp);
        userSessionBean.setUser(loggedInPrincipal);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", PROFILE_UPDATED));
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

    public UploadedFile getUploadedPicture() {
        return uploadedPicture;
    }

    public void setUploadedPicture(UploadedFile uploadedPicture) {
        this.uploadedPicture = uploadedPicture;
    }
    
    
    
}
