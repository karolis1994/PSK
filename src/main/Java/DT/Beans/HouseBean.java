/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Facades.HouseFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Henrikas
 */
@Named(value = "houseBean")
@RequestScoped
public class HouseBean implements Serializable {

    private final static String NOT_APPROVED = "Jūsų narystė nepatvirtinta";

    @Size(min = 1, max = 255)
    private String title;

    @Inject
    private HouseFacade houseFacade;
    private List<Houses> houses;
    private Houses house;
    private List<Paidservices> paidServices;
    private double cost;
    private int costInPoints;

    @Inject
    UserSessionBean user;
    private String canReserveMessage;

    @Size(min = 1, max = 255)
    private String description;

    @Size(min = 1, max = 255)
    private String address;

    // param
    private int houseID;

    @Min(1)
    private int capacity;
    private List<Extras> extrasList;
    private int availableto;
    private int availablefrom;

    public List<Extras> getExtrasList() {
        return extrasList;
    }

    public void setExtrasList(List<Extras> extrasList) {
        this.extrasList = extrasList;
    }

    public int getAvailablefrom() {
        return availablefrom;
    }

    public void setAvailablefrom(int availablefrom) {
        this.availablefrom = availablefrom;
    }

    public int getAvailableto() {
        return availableto;
    }

    public void setAvailableto(int availableto) {
        this.availableto = availableto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCanReserveMessage() {
        return canReserveMessage;
    }

    public void setCanReserveMessage(String canReserveMessage) {
        this.canReserveMessage = canReserveMessage;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public Houses getHouse() {

        house = houseFacade.find(houseID);
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

    public List<Paidservices> getPaidServices() {
        paidServices = getHouse().getPaidservicesList();

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

    public int getCostInPoints() {
        for (Paidservices ps : getPaidServices()) {
            if (ps.getHouseid() != null) {
                costInPoints = ps.getCostInPoints();
                break;
            }
        }

        return costInPoints;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCostInPoints(int points) {
        this.costInPoints = points;
    }

    @PostConstruct
    void init() {
        String houseIDstring = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (houseIDstring != null) {
            houseID = Integer.parseInt(houseIDstring);
            loadData();
        }
    }

    public void loadData() {
        house = houseFacade.find(houseID);

        if (house == null) {
            return;
        }

        title = house.getTitle();
        description = house.getDescription();
        address = house.getAddress();
        capacity = house.getCapacity();
        extrasList = house.getExtrasList();
        availableto = house.getAvailableto();
        availablefrom = house.getAvailablefrom();
    }

    public String removeHouse() {
        Houses houseToDelete = houseFacade.find(houseID);
        houseToDelete.setIsdeleted(true);
        houseFacade.edit(houseToDelete);

        return "house-view-all.xhtml";
    }

    public String updateHouse() {
        Houses houseToUpdate = houseFacade.find(houseID);

        houseToUpdate.setTitle(title);
        houseToUpdate.setDescription(description);
        houseToUpdate.setAddress(address);
        houseToUpdate.setCapacity(capacity);
        houseToUpdate.setAvailablefrom(availablefrom);
        houseToUpdate.setAvailableto(availableto);
        houseFacade.edit(houseToUpdate);

        return "house-preview.xhtml?faces-redirect=true&id=" + houseID;
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
        switch (month) {
            case 1:
                return "Sausis";
            case 2:
                return "Vasaris";
            case 3:
                return "Kovas";
            case 4:
                return "Balandis";
            case 5:
                return "Gegužė";
            case 6:
                return "Birželis";
            case 7:
                return "Liepa";
            case 8:
                return "Rugpjūtis";
            case 9:
                return "Rugsėjis";
            case 10:
                return "Spalis";
            case 11:
                return "Lapkritis";
            case 12:
                return "Gruodis";
        }
        return "Klaida";
    }

    //this is some next level java bullshit
    public String RedirectToEdit() {
        return "house-edit.xhtml?faces-redirect=true&amp;id=" + houseID;
    }

    public String RedirectAddMoreExtras() {
        return "administration/add-extra.xhtml?faces-redirect=true&amp;house=" + houseID;
    }
}
