package DT.Beans;

import DT.Entities.Settings;
import DT.Facades.SettingsFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Size;

/**
 *
 * @author donatas
 */
@ManagedBean(name = "userSettingsBean")
@RequestScoped
public class UserSettingsBean {

//    public class NumberTooSmallException extends Exception {
//
//        public NumberTooSmallException() {
//            super();
//        }
//    }
    @EJB
    SettingsFacade settingsFacade;

    private Integer maxUserAmmount;

    public Integer getMaxUserAmmount() {
        return maxUserAmmount;
    }

    public void setMaxUserAmmount(Integer value) {

        maxUserAmmount = value;
    }

    @PostConstruct
    public void init() {
        Settings setting = settingsFacade.getSettingByName("MaxUserAmmount");
        maxUserAmmount = Integer.parseInt(setting.getSettingvalue());

        setting = settingsFacade.getSettingByName("MembershipCost");

    }

    public UserSettingsBean() {

    }

    public void save() {
//        try {
//            validateInteger(maxUserAmmount);
//        } catch (NumberTooSmallException e) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Klaida: ", "Permažas maksimalių vartotojų skaičius"));
//            return;
//        }

        Settings setting = settingsFacade.getSettingByName("MaxUserAmmount");
        setting.setSettingvalue(maxUserAmmount.toString());
        settingsFacade.edit(setting);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Išsaugota."));

    }

//    private void validateInteger(Integer value) throws NumberTooSmallException {
//        if (value < 1) {
//            throw new NumberTooSmallException();
//        }
//    }
}
