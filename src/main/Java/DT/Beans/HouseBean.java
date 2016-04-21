/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Facades.HouseFacade;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Henrikas
 */
@Named(value = "houseBean")
@ViewScoped
public class HouseBean implements Serializable{

    @Inject
    private HouseFacade houseFacade;
    private List<Houses> houses;
    private Houses house; 
    private List<Paidservices> paidServices;
    private double cost;
    
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
    
    public List<Paidservices> getPaidServices() {
        if (paidServices == null) {
            paidServices = house.getPaidservicesList();
        }
        
        return paidServices;
    }

    public void setPaidServices(List<Paidservices> paidServices) {
        this.paidServices = paidServices;
    }
    
    public double getCost() {
        for (Paidservices ps : getPaidServices()) {
            if (ps.getExtrasid() == null) {
                cost = ps.getCost();
                break;
            }
        }
        
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
 
}
