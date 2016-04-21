/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Entities.Principals;
import DT.Entities.Reservationextras;
import DT.Entities.Reservations;
import DT.Facades.HouseFacade;
import DT.Facades.PrincipalsFacade;
import DT.Facades.ReservationFacade;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;

import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Henrikas
 */
@Named(value = "reservationBean")
@ViewScoped
public class ReservationBean implements Serializable{
    private final String DATE_FORMAT = "MM/dd/yy";
    private final String PAGE_AFTER_RESERVING = "index";
    
    @Inject
    HouseBean houseBean;
    
    @Inject
    private PrincipalsFacade principalFacade;
    
    @Inject
    private HouseFacade houseFacade;
    
    @Inject
    private ReservationFacade reservationFacade;
    private Reservations reservation;
    private String reservedFrom;
    private String reservedTo;
    private int numberOfWeeks = 1;
    private Date reservedFromParsed;
    private Date reservedToParsed;
    private List<Extras> extrasList;
    private List<Extras> selectedExtras = new ArrayList<>();
    private double totalPrice = 0.0;
    private double housePrice = 0.0;
    private double totalHousePrice = 0.0;
    private Houses house;
    private boolean extrasTableVisible = false;
    private int houseID;
    private boolean selectedExtrasEmpty = true;

    public void init() {
        house = houseFacade.find(houseID);
    }
 
    
    public String saveReservation() {
        parseDates();
        
        // WARNING: FOR TESTING ONLY
        Principals princ = (Principals) principalFacade.findByEmail("zen@gmail.com").get(0);
        
        Reservations res = new Reservations();
        res.setHouseid(house);
        res.setReservedfrom(reservedFromParsed);
        res.setReservedto(reservedToParsed);
        res.setIscanceled(false);
        res.setPrincipalid(princ);
        
        
        List<Reservationextras> rextrasList = new ArrayList<>();
        for (Extras e : selectedExtras) {
            Reservationextras rextra = new Reservationextras();
            rextra.setExtraid(e);
            rextra.setReservationid(res);
            rextrasList.add(rextra);
        }
        
        res.setReservationextrasList(rextrasList);
        
        reservationFacade.create(res);
        
        return PAGE_AFTER_RESERVING;
    }
    
    public void extraSelectionChanged(Extras extra, boolean isSelected) {        
        if (isSelected && !selectedExtras.contains(extra)) {
            selectedExtras.add(extra);
        }
        else if (!isSelected && selectedExtras.contains(extra)) {
            selectedExtras.remove(extra);
        }
    }
    
    public double calculateTotalPrice() {
        totalPrice = getHousePrice() * numberOfWeeks;
        for(Extras se : selectedExtras) {
            totalPrice += se.getPaidservicesList().get(0).getCost();
        }
        
        return totalPrice;
    }
    
    public void parseDates() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            
            cal.setTime(sdf.parse(reservedFrom));
            reservedFromParsed = cal.getTime();
            
            cal.setTime(sdf.parse(reservedTo));
            reservedToParsed = cal.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(ReservationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reset() {
        house = null;
        numberOfWeeks = 1;
        totalPrice = 0d;
        selectedExtras = new ArrayList<>();
    }
    
    public double getTotalHousePrice() {
        totalHousePrice = housePrice * numberOfWeeks;
        return totalHousePrice;
    }

    public void setTotalHousePrice(double totalHousePrice) {
        this.totalHousePrice = totalHousePrice;
    }
    
    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }
    public void setNumberOfWeeks(int numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }
    
    public double getHousePrice() {
        if (housePrice == 0d) {
            for (Paidservices ps : house.getPaidservicesList()) {
                if (ps.getExtrasid() == null) {
                    housePrice = ps.getCost();
                }
            }
        }
        
        return housePrice;
    }

    public void setHousePrice(double housePrice) {
        this.housePrice = housePrice;
    }

    public Houses getHouse() {
        return house;
    }

    public void setHouse(Houses house) {
        this.house = house;
    }

    public double getTotalPrice() {
        calculateTotalPrice();
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public List<Extras> getExtrasList() {
        if (extrasList == null) {
            extrasList = house.getExtrasList();
        }
        
        return extrasList;
    }

    public List<Extras> getSelectedExtras() {
        return selectedExtras;
    }

    public void setExtrasList(List<Extras> extrasList) {
        this.extrasList = extrasList;
    }

    public void setSelectedExtras(List<Extras> selectedExtras) {
        this.selectedExtras = selectedExtras;
    }

    public Reservations getReservation() {
        if (reservation == null) {
            reservation = new Reservations();
        }
        return reservation;
    }
    
    public String getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(String reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public String getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(String reservedTo) {
        this.reservedTo = reservedTo;
    }
    
    public boolean isExtrasTableVisible() {
        return extrasTableVisible;
    }

    public void setExtrasTableVisible(boolean extrasTableVisible) {
        this.extrasTableVisible = extrasTableVisible;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public boolean isSelectedExtrasEmpty() {
        return (getSelectedExtras().isEmpty());
    }

    public void setSelectedExtrasEmpty(boolean selectedExtrasEmpty) {
        this.selectedExtrasEmpty = selectedExtrasEmpty;
    }
    
    
}
