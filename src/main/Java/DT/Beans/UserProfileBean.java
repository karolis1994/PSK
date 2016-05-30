/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import DT.Facades.SettingsFacade;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Karolis
 */
@Named("userProfileBean")
@RequestScoped
public class UserProfileBean implements Serializable {  
        
    // Fields ------------------------------------------------------------------
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String ERROR = "Klaida: ";
    private final static String WRONG_ID_FORMAT = "ID formatas netinkamas.";
    private final static String NO_USER_WITH_SUCH_ID = "Naudotojas su tokiu ID neegzistuoja.";
    private final static String CLUB_MEMBER = "Klubo narys";
    private final static String CLUB_CANDIDATE = "Klubo kandidatas";
    private final static String CLUB_CANDIDATE_NO_MEMBERSHIP = "Klubo kandidatas (neapmokėta narystė)";
    private final static String CLUB_ADMINISTRATOR = "Klubo administratorius";

    private Principals shownPrincipal;
    @Inject
    private PrincipalsFacade principalsFacade;
    @Inject
    private SettingsFacade settingsFacade;
    
    private String userID;
    private String firstname;
    private String lastname;
    private String birthdate;
    private String memberUntil;
    private String email;
    private String phoneNumber;
    private String address;
    private String about;
    private String memberStatus;
    private byte[] picture;
    
    private boolean aboutField;
    private boolean pictureField;
    
    private SimpleDateFormat sdf;
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init(){
        //Loading settings
        aboutField = "true".equals(settingsFacade.getSettingByName("AboutField").getSettingvalue());
        pictureField = "true".equals(settingsFacade.getSettingByName("PictureField").getSettingvalue());
        
        userID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("userID");
        
        //Choosing the user by userID, if no such member showing an error message
        int principalID;
        try {
            principalID = Integer.parseInt(userID);
            shownPrincipal = principalsFacade.find(principalID);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, WRONG_ID_FORMAT));
            return;
        }
        if(shownPrincipal == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, NO_USER_WITH_SUCH_ID));
            return;
        }
        
        //Setting fields that will be shown
        sdf = new SimpleDateFormat(DATE_FORMAT);   
        chooseMemberStatus();
        firstname = shownPrincipal.getFirstname();
        lastname = shownPrincipal.getLastname();
        birthdate = sdf.format(shownPrincipal.getBirthdate());
        displayPicture();
        
        email = shownPrincipal.getEmail();
        phoneNumber = shownPrincipal.getPhonenumber();
        address = shownPrincipal.getAddress();
        about = shownPrincipal.getAbout();
        if(about == null) {
            about = "";
        }
    }
    
    //Method to choose the member status
    private void chooseMemberStatus() {
        if(shownPrincipal.getIsadmin() != null && shownPrincipal.getIsadmin()) {
            memberStatus = CLUB_ADMINISTRATOR;
            return;
        }
        if(shownPrincipal.getMembershipuntill() != null && new Date().after(shownPrincipal.getMembershipuntill())) {
            memberUntil = sdf.format(shownPrincipal.getMembershipuntill());
            memberStatus = CLUB_MEMBER;
        } else {
            if(!shownPrincipal.getIsapproved())
                memberStatus = CLUB_CANDIDATE;
            else
                memberStatus = CLUB_CANDIDATE_NO_MEMBERSHIP;
        }
    }
    
    //Method to convert byte array into displayable format
    public void displayPicture() {
        if(pictureField) {
            if(shownPrincipal.getPicture() != null) {
                picture = shownPrincipal.getPicture().getImage();
                return;
            }
            picture = new byte[1];
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

    public String getMemberStatus() {
        return memberStatus;
    }

    public byte[] getPicture() {
        return picture;
    }
    
    
    
}
