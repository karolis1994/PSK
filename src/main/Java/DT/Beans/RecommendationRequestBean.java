/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Karolis
 */
@ManagedBean(name="recommendationRequestBean")
@RequestScoped
public class RecommendationRequestBean {
    
    private List<Principals> principalsList;
    
    private PrincipalsFacade principalsFacade;
    
    private String[] userNameList;
    
    private String[] selectedUserNameList;
    private Map<String, Principals> principalsMap;
    
    @PostConstruct
    public void init() {
        principalsList = principalsFacade.findAllApproved();
        
        int i = 0;
        for(Principals principal : principalsList) {
            userNameList[i] = principal.getFirstname() + " " + principal.getLastname();
            principalsMap.put(userNameList[i], principal);
            i++;
        }
        
    }
    
    public void SendEmails() {
        for(String name : selectedUserNameList) {
            Principals p = principalsMap.get(name);
            // siusti emaila = p.getEmail();
        }
    }
    
    public List<Principals> getPrincipalsList() {
        return principalsList;
    }

    public void setPrincipalsList(List<Principals> userList) {
        this.principalsList = userList;
    }
    
    public String[] getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(String[] userNameList) {
        this.userNameList = userNameList;
    }

    public String[] getSelectedUserNameList() {
        return selectedUserNameList;
    }

    public void setSelectedUserNameList(String[] selectedUserNameList) {
        this.selectedUserNameList = selectedUserNameList;
    }
    
}
