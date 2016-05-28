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
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 *
 * @author Henrikas
 */
@Named(value = "houseBean")
@RequestScoped
public class HouseBean implements Serializable{

    private final static String NOT_APPROVED = "Jūsų narystė nepatvirtinta";
    
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
    
    @Min(1)
    private int capacity;
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    @Inject
    private HouseFacade houseFacade;
    private List<Houses> houses;
    private Houses house; 
    private List<Paidservices> paidServices;
    private double cost;
    
    @Inject UserSessionBean user;
    private String canReserveMessage;
    public String getCanReserveMessage() { return canReserveMessage; }
    public void setCanReserveMessage(String canReserveMessage) { this.canReserveMessage = canReserveMessage; }
    
    // param
    private int houseID;
    public int getHouseID() { return houseID; }
    public void setHouseID(int houseID) { this.houseID = houseID; }
    
    public void loadData() {
        house = findHouseById(houseID);
        
        if (house == null)
            return;
        
        title = house.getTitle();
        description = house.getDescription();
        address = house.getAddress();
        capacity = house.getCapacity();
    }
    
    public Houses getHouse() {
        if (house == null) {
            loadData();
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
        return findHouseById(houseID);
    }
    
    public List<Paidservices> getPaidServices() {
        if (paidServices == null) {
            paidServices = getHouse().getPaidservicesList();
        }
        
        return paidServices;
    }

    public void setPaidServices(List<Paidservices> paidServices) {
        this.paidServices = paidServices;
    }
    
    public double getCost() {
        for (Paidservices ps : getPaidServices()) {
            if (ps.getHouseid() != null) {
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
        houseToUpdate.setCapacity(capacity);
        
        houseFacade.edit(houseToUpdate);
        
        return "house-preview.xhtml?id=" + houseID;
    }
    
    public String navigateToReservation() {
        return "house-reservation.xhtml?faces-redirect=true&houseID=" + houseID;
    }
    
    public boolean canReserve() {
        if (user.getUser().getIsapproved() == false) {
            canReserveMessage = NOT_APPROVED;
            return false;
        }
        
        return true;
    }
    
    public static String intToMonthName(int month) {
        switch(month) {
            case 1 :
            return "Sausis";
            case 2 :
            return "Vasaris";
            case 3 :
            return "Kovas";
            case 4 :
            return "Balandis";
            case 5 :
            return "Gegužė";
            case 6 :
            return "Birželis";
            case 7 :
            return "Liepa";
            case 8 :
            return "Rugpjūtis";
            case 9 :
            return "Rugsėjis";
            case 10 :
            return "Spalis";
            case 11 :
            return "Lapkritis";
            case 12 :
            return "Gruodis";
        }
        return "Klaida";
    }
}
