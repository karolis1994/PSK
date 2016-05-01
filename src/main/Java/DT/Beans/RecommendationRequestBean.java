/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Facades.GmailMail;
import DT.Entities.Invitations;
import DT.Entities.InvitationsPK;
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
import javax.ejb.EJBException;
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
        userNameList = new ArrayList<String>();
        principalsMap = new HashMap<String, Principals>();
        
        principalsList = principalsFacade.findAllApproved();
        int i = 0;
        for(Principals principal : principalsList) {
            userNameList.add(principal.getFirstname() + " " + principal.getLastname());
            principalsMap.put(userNameList.get(i), principal);
            i++;
        }    
    }
    
    public void SendEmails() throws Exception{
        /*if(p.getIsApproved == true) { //reikia prisiregistravusio vartotojo duomenu.
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "You are an approved member already"));
            return "";
        }*/
        
        //sugeneruojam aktyvacijos rakta, sukuriam reikiamus laukus
        String uuid = UUID.randomUUID().toString();
        Invitations invitation = new Invitations();
        //test
        Principals user = principalsFacade.find(1); //reikalingas prisijungusio vartotojo objektas  
        String message = "User with the name: " + user.getFirstname() + " " + user.getLastname() + // Reikalingas prisijungusio vartotojo objektas
                    " is asking for your approval. Click the link below to approve.\n" +
                    "http://localhost:8080/DT.ReservationSystem/recommendation-approve.xhtml?key=" 
                    + uuid; //Reikalingas sugeneruotas linkas.             
        int error = -1;
        int i = 0;

        try {
            invitation.setUrlcode(uuid);
            String[] recipients = new String[selectedUserNameList.length];
            InvitationsPK inv = new InvitationsPK();
            inv.setSenderid(user.getId());
                      
            for(String name : selectedUserNameList) {
                Principals p = principalsMap.get(name);
                recipients[i] = p.getEmail();               
                inv.setRecieverid(p.getId());
                invitation = new Invitations(inv, uuid);
                invitationsFacade.create(invitation);                          
            }
            error = mailFacade.sendMail(recipients, message);
        } catch(EJBException e2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "You have already sent an email to one of the marked people"));
        } catch(Exception e) {
            error = -1;
        } 
        finally {
            if(error == -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Failed to send email."));
            } else if(error == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Emails have been sent."));
            }
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
