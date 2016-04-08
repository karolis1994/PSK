/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Paidservices;
import DT.Entities.Reservations;
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
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Henrikas
 */
@ManagedBean(name = "reservationBean")
@SessionScoped
public class ReservationBean implements Serializable{
    private final String DATE_FORMAT = "dd/mm/yy";
    
    @EJB
    private ReservationFacade reservationFacade;
    private List<Reservations> reservations;
    private Reservations reservation;
    private String reservedFrom;
    private String reservedTo;
    private Date reservedFromParsed;
    private Date reservedToParsed;
    private List<Extras> extrasList;
    private List<Extras> selectedExtras = new ArrayList<>();
    private List<Paidservices> paidServices;
    private List<Paidservices> selectedPaidServices = new ArrayList<>();
    private double totalPrice = 0.0;
    private double housePrice = 0.0;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public List<Extras> getExtrasList() {
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
    
    public void extraSelectionChanged(Extras extra, boolean isSelected) {
        if (isSelected && !selectedExtras.contains(extra)) {
            selectedExtras.add(extra);
        }
        else if (!isSelected && selectedExtras.contains(extra)) {
            selectedExtras.remove(extra);
        }
    }
    
    public double calculateTotalPrice() {
        totalPrice = housePrice;
        for(Paidservices sps : selectedPaidServices) {
            totalPrice += sps.getCost();
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
}
