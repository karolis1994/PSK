/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="userPageBean")
public class UserPageBean {
    private Principals loggedInPrincipal;
    @EJB
    private PrincipalsFacade principalsFacade;
    
    @PostConstruct
    public void init(){
        //gauname prisijungusio vartotojo objektą
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String loggedInEmail = (String) session.getAttribute("authUserEmail");
        loggedInPrincipal = (Principals) principalsFacade.findByEmail(loggedInEmail).get(0);
    }
    
    public String unregister(){
        loggedInPrincipal.setIsdeleted(Boolean.TRUE);
        principalsFacade.edit(loggedInPrincipal);
        
        //atjungiame vartotoją
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "logged-in//index.xhtml?faces-redirect=true";
    }
    
}
