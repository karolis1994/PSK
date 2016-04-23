/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Facades.GmailMail;
import DT.Entities.Invitations;
import DT.Entities.Principals;
import DT.Facades.InvitationsFacade;
import DT.Facades.PrincipalsFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="recommendationRequestBean")
@ViewScoped
public class RecommendationRequestBean implements Serializable{
    
    private List<Principals> principalsList;
    private List<String> userNameList;  
    private String[] selectedUserNameList;
    private Map<String, Principals> principalsMap;
    
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private InvitationsFacade invitationsFacade;
    @EJB
    private GmailMail mailFacade;
    
    
    @PostConstruct
    public void init() {
        
        principalsList = new ArrayList<Principals>();
        principalsList = principalsFacade.findAllApproved();
        userNameList = new ArrayList<String>();
        principalsMap = new HashMap<String, Principals>();
        
        int i = 0;
        for(Principals principal : principalsList) {
            userNameList.add(principal.getFirstname() + " " + principal.getLastname());
            principalsMap.put(userNameList.get(i), principal);
            i++;
        }    
    }
    
    //mail - psk.labanoras@gmail.com pw - labanoras
    public void SendEmails() {
        String uuid = UUID.randomUUID().toString();
        Invitations invitation = new Invitations();
        String message;
        
        int error = -1;
        invitation.setUrlcode(uuid);
        
        //Siunciame kiekvienam pasirinktam vartotojui email'a
        for(String name : selectedUserNameList) {
            Principals p = principalsMap.get(name);
            invitation.setPrincipals(p); // Reikalingas prisijungusio vartotojo objektas
            invitation.setPrincipals1(p);
            invitationsFacade.create(invitation);
            message = "User with the name: " + p.getFirstname() + " " + p.getLastname() + // Reikalingas prisijungusio vartotojo objektas
                    " is asking for your approval. Click the link below to approve.\n" +
                    ""; //Reikalingas sugeneruotas linkas.
            error = mailFacade.sendMail(p.getEmail(), message);
            if(error != 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Failed to send email."));
                break;
            }
        }
        if(error == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Email sent."));
        }
    }
    
    public List<Principals> getPrincipalsList() {
        return principalsList;
    }

    public void setPrincipalsList(List<Principals> userList) {
        this.principalsList = userList;
    }
    
    public List<String> getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }

    public String[] getSelectedUserNameList() {
        return selectedUserNameList;
    }

    public void setSelectedUserNameList(String[] selectedUserNameList) {
        this.selectedUserNameList = selectedUserNameList;
    }
    
}
