/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Aurimas
 */
@Named(value = "addPoints")
@RequestScoped
public class AddPoints {

     // Fields ------------------------------------------------------------------
    @Inject private PrincipalsFacade principalsFacade;
    private List<Principals> principals;
    private Principals principal; 
    private int points;
      
    private String paramId;
    public String getParamId() { return paramId; }
    public void setParamId(String paramId) { this.paramId = paramId; }

    // Methods ------------------------------------------------------------------
    @PostConstruct
    public void loadData() {
       
        if (paramId == null || paramId.isEmpty())
            return;
        
        //paramId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("principal");
        
        principal = findPrincipalById(Integer.parseInt(paramId));
        
        if (principal == null)
            return;
        
        points = principal.getPoints();
    }
    
    //Method to change member's points
    public void add(int addedPoints){
        principal.setPoints(addedPoints);
    }

    //Method to find principal by id in page's url
    public Principals findPrincipalByUrlId() {
        if (paramId != null && !paramId.isEmpty())
            return findPrincipalById(Integer.parseInt(paramId));
        return null;
    }

    //Method to update principal that has been modified
    public String updatePrincipal() {
        Principals principalToUpdate = findPrincipalByUrlId();
        
        principalToUpdate.setPoints(points);
        principalsFacade.edit(principalToUpdate);
        
        return "logged-in/administration/display-members-points.xhtml";
    }
    //Method to find principal by it's id in database
    private Principals findPrincipalById(int id) {
        principal = principalsFacade.find(id);
        return principal;
    }
    // Getters / setters -------------------------------------------------------
    public int getPoints() { return points; }
    
    public void setPoints(int points) { this.points = points; }

    public List<Principals> getPrincipals() {
        if (principals == null){
            principals = principalsFacade.findAll();
        }
        return principals;
    }

    public void setPrincipals(List<Principals> principals) {
        this.principals = principals;
    }
}
