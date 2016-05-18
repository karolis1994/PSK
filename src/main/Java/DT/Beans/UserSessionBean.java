package DT.Beans;

import DT.Entities.Principals;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * @author Laurynas
 */

@Named("userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable {
    
    Principals user;
    public Principals getUser() { return user; }
    public void setUser(Principals user) { this.user = user; }
    
    @PostConstruct
    public void init() {
        
    }
}
