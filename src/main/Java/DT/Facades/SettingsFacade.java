/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Settings;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author Karolis
 */
@Stateless
public class SettingsFacade extends GenericFacade<Settings>{
    
    public SettingsFacade() {
        super(Settings.class);
    }
    
    public Settings getFirstnameFieldSettings() {
        Settings res = new Settings();
        try {
            res = (Settings) em.createNamedQuery("Settings.findBySettingname").setParameter("settingname", "firstnamefield")
                .getSingleResult();
        } catch(NoResultException e1) {
            res.setSettingname("firstnamefield");
            res.setSettingvalue("true");
            em.persist(res);
            res = getFirstnameFieldSettings();
        } catch(NonUniqueResultException e2) {

        }
        return res;
    }
    
    public Settings getLastnameFieldSettings() {
        Settings res = new Settings();
        try {
            res = (Settings) em.createNamedQuery("Settings.findBySettingname").setParameter("settingname", "lastnamefield")
                .getSingleResult();
        } catch(NoResultException e1) {
            res.setSettingname("lastnamefield");
            res.setSettingvalue("true");
            em.persist(res);
            res = getLastnameFieldSettings();
        } catch(NonUniqueResultException e2) {
            
        }
        return res;
    }
    
    public String getFirstnameFieldSetting() {
        Settings res = getFirstnameFieldSettings();
        return res.getSettingvalue();
    }
    
    public String getLastnameFieldSetting() {
        Settings res = getLastnameFieldSettings();
        return res.getSettingvalue();
    }
    
}
