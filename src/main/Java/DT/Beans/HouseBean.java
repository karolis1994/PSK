/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Houses;
import DT.Facades.HouseFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Henrikas
 */
@ManagedBean(name = "houseBean")
public class HouseBean {

    @EJB
    private HouseFacade houseFacade;
    private List<Houses> houses;
    private Houses house; 
    private List<Extras> extras;
 
    public Houses getHouse() {
        if (house == null) {
            house = new Houses();
        }
 
        return house;
    }
 
    public void setHouse(Houses house) {
        this.house = house;
    }
 
    public void resetHouse() {
        house = new Houses();
    }
    
    public List<Houses> getAllHouses() {
        if (houses == null) {
            loadHouses();
        }
 
        return houses;
    }
 
    private void loadHouses() {
        houses = houseFacade.findAll();
        System.out.println("Extra 1: " + houses.get(0).getExtrasList().get(0).getTitle());
    }
}
