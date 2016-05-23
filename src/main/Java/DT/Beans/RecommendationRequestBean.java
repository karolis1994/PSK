/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Services.MailSMTP;
import DT.Entities.Principals;
import DT.Entities.Recommendations;
import DT.Entities.Settings;
import DT.Facades.PrincipalsFacade;
import DT.Facades.RecommendationsFacade;
import DT.Facades.SettingsFacade;
import DT.Services.IMail;
import java.io.Serializable;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="recommendationRequestBean")
@ViewScoped
public class RecommendationRequestBean implements Serializable{
    
    // Fields ------------------------------------------------------------------
    
    private static final String SUBJECT = "Prašymas priimti į klubą";
    
    private Principals loggedInPrincipal;
    private Principals inputPrincipal;
    private Settings maxRecommendations;
    
    @EJB
    private SettingsFacade settingsFacade;
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private RecommendationsFacade recommendationsFacade;
    @EJB
    private final IMail mailSMTP = new MailSMTP();
    
    @Inject
    private UserSessionBean userSessionBean;
    
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String inputEmail;
    private int currentlySentRecommendations;  
    
    // Methods -----------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        
        loggedInPrincipal = userSessionBean.getUser();     
              
        maxRecommendations = (Settings) settingsFacade.getSettingByName("MaxRecommendations");
        currentlySentRecommendations = recommendationsFacade.findBySender(loggedInPrincipal.getId()).size();
        
    }
    
    
    //Method to send mail
    public void SendEmails() throws Exception{
        //Check if the logged in user is not already approved
        if(loggedInPrincipal.getIsapproved() == true) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Jums jau yra suteiktas patvirtinto nario statusas."));
            return;
        }
        //Check if recommendations count is not above maximum
        if(currentlySentRecommendations >= Integer.parseInt(maxRecommendations.getSettingvalue())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Daugiau prašymų siūsti nebegalima."));
            return;
        }
        //Check if there is a user registered with input email
        inputPrincipal = new Principals();
        inputPrincipal.setIsapproved(Boolean.FALSE);
        try {
            inputPrincipal = (Principals) principalsFacade.findByEmail(inputEmail);
        } catch(IndexOutOfBoundsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Narys su įvestu email neegzistuoja."));
            return;
        }    
        //Check if the user we're sending the email to is aleady a member
        if(!inputPrincipal.getIsapproved()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Narys su įvestu email nėra patvirtintas narys."));
            return;
        }
        
        //Generating activation key
        String uuid = UUID.randomUUID().toString();
        Recommendations recommendation = new Recommendations();
        
        //Getting the base URL and creating the message to be sent
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
        String message = "Naudotojas vardu " + loggedInPrincipal.getFirstname() + " " + loggedInPrincipal.getLastname() + // Reikalingas prisijungusio vartotojo objektas
                    " prašo jūsų patvirtinimo. Paspauskite nuorodą apačioje jį patvirtinti.\n" +
                    baseURL + "logged-in/recommendation-approve.xhtml?key=" +
                    uuid;          
        int error = -1;
        int i = 0;

        try {
            recommendation.setUrlcode(uuid);
            recommendation.setSenderid(loggedInPrincipal);
            recommendation.setRecieverid(inputPrincipal);
            recommendationsFacade.create(recommendation);                          
            error = mailSMTP.sendMail(inputPrincipal.getEmail(), SUBJECT, message);
        } catch(EJBException e2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Email jau buvo išsiūstas šiam žmogui."));
        } catch(Exception e) {
            error = -1;
        } 
        finally {
            if(error == -1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: ", "Nepavyko išsiūsti email."));
            } else if(error == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Email buvo išsiūstas."));
            }
        }
    }
    
    // Getters / setters -------------------------------------------------------
    
    public String getInputEmail() {
        return inputEmail;
    }
    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }
    
}
