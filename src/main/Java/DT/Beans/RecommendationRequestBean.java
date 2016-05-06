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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="recommendationRequestBean")
@ViewScoped
public class RecommendationRequestBean implements Serializable{
    
    private static final String SUBJECT = "Prašymas priimti į klubą";
    
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String inputEmail;
    public String getInputEmail() {
        return inputEmail;
    }
    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }
    
    private Principals loggedInPrincipal;
    private Principals inputPrincipal;
    private Settings maxRecommendations;
    private int currentlySentRecommendations;
    
    @EJB
    private SettingsFacade settingsFacade;
    @EJB
    private PrincipalsFacade principalsFacade;
    @EJB
    private RecommendationsFacade recommendationsFacade;
    @EJB
    private final IMail mailSMTP = new MailSMTP();
    
    @PostConstruct
    public void init() {
        
        //Gauname prisijungusio naudotojo objekta
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String loggedInEmail = (String) session.getAttribute("authUserEmail");
        loggedInPrincipal = (Principals) principalsFacade.findByEmail(loggedInEmail).get(0);      
              
        maxRecommendations = (Settings) settingsFacade.getSettingByName("MaxRecommendations");
        currentlySentRecommendations = recommendationsFacade.findBySender(loggedInPrincipal.getId()).size();
        
    }
    
    public void SendEmails() throws Exception{
        //Patikriname ar narys jau nėra patvirtintas
        if(loggedInPrincipal.getIsapproved() == true) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Jums jau yra suteiktas patvirtinto nario statusas."));
            return;
        }
        //Patikriname ar išsiūstų prašymų skaičius neviršija leistinos ribos
        if(currentlySentRecommendations >= Integer.parseInt(maxRecommendations.getSettingvalue())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Daugiau prašymų siūsti nebegalima."));
            return;
        }
        //Patikriname ar yra narys užsiregistravęs su įvestu email
        inputPrincipal = new Principals();
        inputPrincipal.setIsapproved(Boolean.FALSE);
        try {
            inputPrincipal = (Principals) principalsFacade.findByEmail(inputEmail).get(0);
        } catch(IndexOutOfBoundsException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Narys su įvestu email neegzistuoja."));
            return;
        }    
        //Patikriname ar narys kuriam siunčiame yra patvirtintas
        if(!inputPrincipal.getIsapproved()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Narys su įvestu email nėra patvirtintas narys."));
            return;
        }
        
        //sugeneruojam aktyvacijos rakta, sukuriam reikiamus laukus
        String uuid = UUID.randomUUID().toString();
        Recommendations recommendation = new Recommendations();
        
        //Gauname internetio adreso bazinį url, jį pridedam prie žinutės
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
    
}
