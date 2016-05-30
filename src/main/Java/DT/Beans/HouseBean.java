/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Entities.Settings;
import DT.Facades.ExtrasFacade;
import DT.Facades.HouseFacade;
import DT.Facades.PaidServicesFacade;
import DT.Facades.SettingsFacade;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    private final static String NOT_SEASON = "Rezervacijos nevyksta (ne sezonas)";
    private final static String NOT_APPROVED = "Jūsų narystė nepatvirtinta";
    private final static String NOT_MEMBER = "Jūs nesate susimokėjęs metinio narystės mokesčio";
    private final static String GROUP_TOO_LOW = "Jūsų rezervavimo pirmumo grupė yra per žema";

    @Size(min = 1, max = 255)
    private String title;

    @Inject
    private HouseFacade houseFacade;
    @Inject
    private PaidServicesFacade paidServicesFacade;
    private List<Houses> houses;
    private Houses house;
    private List<Paidservices> paidServices;
    private double cost;
    private int costInPoints;

    @Inject
    UserSessionBean user;
    private String canReserveMessage;
    
    @Inject
    private SeasonSettingsBean seasonSettingsBean;

    @Size(min = 1, max = 255)
    private String description;
    
    @Size(min = 0, max = 255)
    private String faceBookImageURL;
    @Size(min = 1, max = 255)
    private String address;

    // param
    private int houseID;

    @Min(1)
    private int capacity;
    private List<Extras> extrasList;
    private int availableto;
    private int availablefrom;
    
    @Min(1)
    private int priceInPoints;
    public int getPriceInPoints() { return priceInPoints; }
    public void setPriceInPoints(int price) { this.priceInPoints = price; }

    @Inject
    ExtrasFacade extrasFacade;
    
    public List<Extras> getExtrasList() {
        
        List<Extras> list = new LinkedList<>();
        
        if (extrasList == null) {
            return list;
        }
        
        for(Extras e : extrasList) {
            if (e.getIsdeleted() == null || e.getIsdeleted() == false) {
                list.add(e);
            }
        }
        
        return list;
        
    }
    
    public Boolean hasExtras() {
        List list = getExtrasList();
        return list != null && list.size() > 0;
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
    
    public String getFaceBookImageURL() {
        return faceBookImageURL;
    }

    public void setFaceBookImageURL(String faceBookImageURL) {
        this.faceBookImageURL = faceBookImageURL;
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
        faceBookImageURL = house.getFaceBookImageURL();
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
        houseToUpdate.setFaceBookImageURL(faceBookImageURL);
        houseFacade.edit(houseToUpdate);
        
        Paidservices paidService = houseToUpdate.getPaidservicesList().get(0);
        paidService.setCostInPoints(costInPoints);
        paidServicesFacade.edit(paidService);

        return "house-preview.xhtml?faces-redirect=true&id=" + houseID;
    }

    public String navigateToReservation() {
        return "house-reservation.xhtml?faces-redirect=true&houseID=" + houseID;
    }
    
    public String remove(Extras extra) {
        
        extrasFacade.remove(extra);
        
        return "";
    }

    public boolean canReserve() {
        if (user.getUser().getIsadmin() == true) {
            return true;
        }
        
        Date seasonStartDate = getSeasonStartDate();
        Date seasonEndDate = getSeasonEndDate();
        if (
                seasonStartDate == null || 
                seasonEndDate == null ||
                seasonStartDate.after(new Date()) ||
                seasonEndDate.before(new Date())
            ) 
        {
            canReserveMessage = NOT_SEASON;
            return false;
        }
        
        if (user.getUser().getIsapproved() == false) {
            canReserveMessage = NOT_APPROVED;
            return false;
        }
        
        if (user.getUser().getMembershipuntill() == null || user.getUser().getMembershipuntill().before(new Date())) {
            canReserveMessage = NOT_MEMBER;
            return false;
        }
        
        Date reservateDateByGroup = calculateReservationDateByGroup();
        if (reservateDateByGroup == null || calculateReservationDateByGroup().after(new Date())) {
            canReserveMessage = GROUP_TOO_LOW;
            return false;
        }

        return true;
    }
    
    private Date calculateReservationDateByGroup() {
        Integer groupNo = user.getUser().getGroupno();
        if (groupNo == null || groupNo == 0) 
            return null;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(getSeasonStartDate());
        cal.add(Calendar.WEEK_OF_YEAR, groupNo-1);  
        
        return cal.getTime();
    }
    
    private Date getSeasonStartDate() {
        return seasonSettingsBean.getStartDate();
    }
    
    private Date getSeasonEndDate() {
        return seasonSettingsBean.getEndDate();
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
