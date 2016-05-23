package DT.Beans;

import DT.Entities.Principals;
import facebook4j.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

/**
 * @author Laurynas
 */
@Named("userSessionBean")
@SessionScoped
public class UserSessionBean implements Serializable {

    Principals user;
    User userFB;

    public Principals getUser() {
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
