/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="recommendationRequestBean")
@ViewScoped
public class RecommendationRequestBean implements Serializable{
    
    private List<Principals> principalsList;
    
    @EJB
    private PrincipalsFacade principalsFacade;
    
    private List<String> userNameList;
    
    private String[] selectedUserNameList;
    private Map<String, Principals> principalsMap;
    
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
    
    public void SendEmails() {
        /*for(String name : selectedUserNameList) {
            Principals p = principalsMap.get(name);
            // siusti emaila = p.getEmail();
        }*/
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
