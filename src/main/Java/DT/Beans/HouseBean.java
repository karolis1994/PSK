/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Houses;
import DT.Facades.HouseFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Henrikas
 */
@ManagedBean(name = "houseBean")
@SessionScoped
public class HouseBean implements Serializable{

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
    
    public List<Houses> getHouses() {
        if (houses == null) {
            houses = houseFacade.findAll();
        }
        
        return houses;
    }
    
    public void setHouses(List<Houses> houses) {
        this.houses = houses;
    }
    
    public Houses findHouseById(Integer id) {
        house = houseFacade.find(id);
        return house;
    }
    
    public List<Extras> getExtras() {
        return extras;
    }

    public void setExtras(List<Extras> extras) {
        this.extras = extras;
    }
}
