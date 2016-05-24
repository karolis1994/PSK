/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.PrincipalsFacade;
import DT.Facades.ReservationFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Henrikas
 */
@Named(value = "reservationsHistoryBean")
@ViewScoped
public class ReservationsHistoryBean implements Serializable {
    
    private final static String RESERVATIONS_HISTORY_PAGE = "reservations-history.xhtml";
    
    @Inject 
    private PrincipalsFacade principalsFacade;
    
    @Inject
    private ReservationFacade reservationFacade;
    
    private Principals principal;   
    private ReservationItem selectedItem;
    private List<ReservationItem> reservationItems;
    
    public class ReservationItem implements Serializable {
        private Reservations houseReservation;
        private List<Reservations> extraReservations;

        public Reservations getHouseReservation() { return houseReservation; }
        public void setHouseReservation(Reservations houseReservation) { this.houseReservation = houseReservation; }

        public List<Reservations> getExtraReservations() { return extraReservations; }
        public void setExtraReservations(List<Reservations> extraReservations) { this.extraReservations = extraReservations; }
        
    }
    
    private List<ReservationItem> findReservations() {
        List<Reservations> houseReservations = reservationFacade.findByPrincipalNotCanceledExtraIdNull(getPrincipal());
        List<Reservations> extraReservations = reservationFacade.findByPrincipalNotCanceledExtraIdNotNull(getPrincipal());
        
        reservationItems = new ArrayList<>();
        
        for (Reservations hr : houseReservations) {
            List<Reservations> reservedExtras = new ArrayList<>();
            
            for (Reservations er : extraReservations) {
                if (hr.getPaymentid().equals(er.getPaymentid())) {
                    reservedExtras.add(er);
                }
            }
            
            ReservationItem newItem = new ReservationItem();
            newItem.setHouseReservation(hr);
            newItem.setExtraReservations(reservedExtras);
            reservationItems.add(newItem);
        }
        
        return reservationItems;
    }

    public String cancelReservation() {      
        reservationFacade.setCanceledByPaymendId(selectedItem.getHouseReservation().getPaymentid(), true);
        
        return RESERVATIONS_HISTORY_PAGE;
    }
    
    public void preselectReservation(ReservationItem item) {
        selectedItem = item;
    }
    
    public PrincipalsFacade getPrincipalsFacade() {
        return principalsFacade;
    }

    public void setPrincipalsFacade(PrincipalsFacade principalsFacade) {
        this.principalsFacade = principalsFacade;
    }

    public ReservationFacade getReservationFacade() {
        return reservationFacade;
    }

    public void setReservationFacade(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    public Principals getPrincipal() {
        if (principal == null) {
            // WARNING: for testing only
            principal = principalsFacade.find(1);
        }
        
        return principal;
    }

    public void setPrincipal(Principals principal) {
        this.principal = principal;
    }

    public ReservationItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ReservationItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<ReservationItem> getReservationItems() {
        if (reservationItems == null) {
            findReservations();
        }
        
        return reservationItems;
    }

    public void setReservationItems(List<ReservationItem> reservationItems) {
        this.reservationItems = reservationItems;
    }
    
    
}
