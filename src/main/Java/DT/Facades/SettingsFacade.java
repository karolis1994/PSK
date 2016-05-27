/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Settings;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Karolis
 */
@Stateless
public class SettingsFacade extends GenericFacade<Settings> {

    public SettingsFacade() {
        super(Settings.class);
    }

    public Settings getSettingByName(String Name) {
        List settingList = em.createNamedQuery("Settings.findBySettingname")
                .setParameter("settingname", Name).getResultList();
        if (settingList.isEmpty()) {
            return null;
        }
        return (Settings) settingList.get(0);
    }

}
