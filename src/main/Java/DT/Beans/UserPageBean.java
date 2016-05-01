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

/**
 *
 * @author Karolis
 */
@ManagedBean(name="userPageBean")
public class UserPageBean {
    private Principals principal;
    @EJB
    private PrincipalsFacade principalsFacade;
    
    @PostConstruct
    public void init(){
        principal = new Principals(); // reikia prisijungusio vartotojo objekto
    }
    
    public void unregister(){
        principal.setIsdeleted(Boolean.TRUE);
        principalsFacade.edit(principal);
        //reikia iškarto atjungti vartotoją
    }
    
}
