/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Reservations;
import DT.Facades.ReservationFacade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
public class ReservationBean {
    @EJB
    private ReservationFacade reservationFacade;
    private List<Reservations> reservations;
    private Reservations reservation;
    private String reservedFrom;
    private String reservedTo;
    private List<Extras> extrasList;
    private List<Extras> selectedExtras = new ArrayList<>();
    private double totalPrice = 0.0;

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
}
