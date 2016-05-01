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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.Size;

/**
 *
 * @author Henrikas
 */
@ManagedBean(name = "houseBean")
@RequestScoped
public class HouseBean implements Serializable{

    @Size(min=1,max=255)
    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    @Size(min=1,max=255)
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Size(min=1,max=255)
    private String address;
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    @EJB
    private HouseFacade houseFacade;
    private List<Houses> houses;
    private Houses house; 
    private List<Paidservices> paidServices;
    private double cost;
    
    @ManagedProperty(value="#{param.id}")
    private String paramId;
    public String getParamId() { return paramId; }
    public void setParamId(String paramId) { this.paramId = paramId; }

    @PostConstruct
    public void loadData() {
        if (paramId == null || paramId.isEmpty())
            return;
        
        house = findHouseById(Integer.parseInt(paramId));
        
        if (house == null)
            return;
        
        title = house.getTitle();
        description = house.getDescription();
        address = house.getAddress();
    }
    
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
    
    public Houses findHouseByUrlId() {
        if (paramId != null && !paramId.isEmpty())
            return findHouseById(Integer.parseInt(paramId));
        return null;
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
    
    public String removeHouse() {
        Houses houseToDelete = findHouseByUrlId();
        houseToDelete.setIsdeleted(true);
        houseFacade.edit(houseToDelete);
        
        return "house-view-all.xhtml";
    }
    
    public String updateHouse() {
        Houses houseToUpdate = findHouseByUrlId();
        
        houseToUpdate.setTitle(title);
        houseToUpdate.setDescription(description);
        houseToUpdate.setAddress(address);
        
        houseFacade.edit(houseToUpdate);
        
        return "house-preview.xhtml?id=" + paramId;
    }
}
