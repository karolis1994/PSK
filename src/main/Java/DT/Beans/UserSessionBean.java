package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import facebook4j.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Laurynas
 */
@Named("userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable {

    Principals user;
    User userFB;
    
    @Inject
    PrincipalsFacade principalsFacade;

    public Principals getUser() {
        if (user != null) {
            user = principalsFacade.find(user.getId());
        }
        return user;
    }

    public void setUser(Principals user) {
        this.user = user;
    }

    public User getUserFB() {
        return userFB;
    }

    public void setUserFB(User userFB) {
        this.userFB = userFB;
    }

    @PostConstruct
    public void init() {

    }
}
