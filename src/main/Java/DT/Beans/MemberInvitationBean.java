/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Invitations;
import DT.Entities.Principals;
import DT.Facades.InvitationsFacade;
import DT.Facades.PrincipalsFacade;
import DT.Services.IMail;
import DT.Services.MailSMTP;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;    
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Aurimas
 */
@ManagedBean(name = "memberInvitationBean")
@ViewScoped
public class MemberInvitationBean{
    private static final String SUBJECT = "Kvietimas užsiregistruoti į labanoro draugų sistemą";
   
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String inputEmail;
    private Principals loggedInPrincipal;
  //  private MemberInvitationBean memberInvitationBean = new MemberInvitationBean();
    
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private InvitationsFacade invitationsFacade;  
    @EJB
    private final IMail mailSMTP = new MailSMTP();
     
    @PostConstruct
    public void init() {
        
        //Gauname prisijungusio naudotojo objekta
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String loggedInEmail = (String) session.getAttribute("authUserEmail");
        loggedInPrincipal = (Principals) principalsFacade.findByEmail(loggedInEmail);
    }
   
    public void SendInvitation() throws Exception{
        //sugeneruojam aktyvacijos rakta, sukuriam reikiamus laukus
        String uuid = UUID.randomUUID().toString();
        Invitations invitation = new Invitations();
        //test
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String message = "Sveiki, mūsų sistemos vartotojas " + loggedInPrincipal.getFirstname() + " " + loggedInPrincipal.getLastname() +
                         " jus kviečia prisijungti prie Labanoro draugų. Kad tai padarytumėte paspauskite nuorodą apačioje.\n" +
                          baseURL +"user-registration.xhtml?key=" 
                         + uuid; //Reikalingas sugeneruotas linkas.             
        int error = -1;
        int i = 0;

        try {
            invitation.setUrlcode(uuid);         
            invitation.setSenderid(loggedInPrincipal);  
            invitation.setIsactivated(Boolean.FALSE);
          //  memberInvitationBean.setInputEmail(inputEmail);
            invitationsFacade.create(invitation); 
                    
            error = mailSMTP.sendMail(inputEmail , SUBJECT, message);
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
    public String getInputEmail() {
        return inputEmail;
    }
    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }
}
