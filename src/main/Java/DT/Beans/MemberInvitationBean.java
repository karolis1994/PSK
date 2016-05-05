/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Invitations;
import DT.Entities.InvitationsPK;
import DT.Entities.Principals;
import DT.Facades.InvitationsFacade;
import DT.Facades.PrincipalsFacade;
import DT.Services.IMail;
import DT.Services.MailSMTP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Aurimas
 */
@Named(value = "memberInvitationBean")
@ViewScoped
public class MemberInvitationBean{
    private static final String SUBJECT = "Kvietimas užsiregistruoti į labanoro draugų sistemą";
    private List<Principals> principalsList;
    private List<String> userNameList;  
    private String[] selectedUserNameList;
    private Map<String, Principals> principalsMap;
    private Principals loggedInPrincipal;
    private String receiverEmail;
    private MailSMTP send;
    private MemberInvitationBean memberInvitationBean = new MemberInvitationBean();
    
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private InvitationsFacade invitationsFacade;  
    
    @PostConstruct
    public void init() {
        
        //Gauname prisijungusio naudotojo objekta
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String loggedInEmail = (String) session.getAttribute("authUserEmail");
        loggedInPrincipal = (Principals) principalsFacade.findByEmail(loggedInEmail).get(0);
    }
   
    public void SendInvitation() throws Exception{
        //sugeneruojam aktyvacijos rakta, sukuriam reikiamus laukus
        String uuid = UUID.randomUUID().toString();
        Invitations invitation = new Invitations();
        //test
        String message = "Sveiki, mūsų sistemos vartotojas " + loggedInPrincipal.getFirstname() + " " + loggedInPrincipal.getLastname() +
                         " jus kviečia prisijungti prie Labanoro draugų. Kad tai padarytumėte paspauskite nuorodą apačioje.\n" +
                         "http://localhost:8080/DT.ReservationSystem/user-registration.xhtml?key=" 
                         + uuid; //Reikalingas sugeneruotas linkas.             
        int error = -1;
        int i = 0;

        try {
            invitation.setUrlcode(uuid);
                       
            InvitationsPK inv = new InvitationsPK();
            inv.setSenderid(loggedInPrincipal.getId());        
            memberInvitationBean.setReceiverID(receiverEmail);
            String recipient = memberInvitationBean.getReceiverID();             
            inv.setRecieveremail(recipient);
            invitation = new Invitations(
                    inv, 
                    uuid);
            invitationsFacade.create(invitation); 
                    
            error = send.sendMail(recipient, SUBJECT, message);
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
    
    public List<Principals> getPrincipalsList() { return principalsList; }

    public void setPrincipalsList(List<Principals> userList) { this.principalsList = userList; }
    
    public List<String> getUserNameList() { return userNameList; }

    public void setUserNameList(List<String> userNameList) { this.userNameList = userNameList; }

    public String[] getSelectedUserNameList() { return selectedUserNameList; }

    public void setSelectedUserNameList(String[] selectedUserNameList) { this.selectedUserNameList = selectedUserNameList; }

    public String getReceiverID() { return receiverEmail; }

    public void setReceiverID(String recieverID) { this.receiverEmail = recieverID; }
}
