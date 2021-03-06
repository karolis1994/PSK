/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.PrincipalsFacade;
import DT.Facades.ReservationFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Henrikas
 */
@Named
@ViewScoped
public class ReservationsHistoryBean implements Serializable {
    
    private final static String RESERVATIONS_HISTORY_PAGE = "reservations-history.xhtml";
    
    @Inject private PrincipalsFacade principalsFacade;
    @Inject private ReservationFacade reservationFacade;
    @Inject private UserSessionBean userSessionBean;
    
    private Principals principal;
    private Reservations selectedExtraReservation;
    private ReservationItem selectedItem;
    private List<ReservationItem> reservationItems;
    
    public class ReservationItem implements Serializable {
        private Reservations houseReservation;
        private List<Reservations> extraReservations = new ArrayList<>();

        public Reservations getHouseReservation() { return houseReservation; }
        public void setHouseReservation(Reservations houseReservation) { this.houseReservation = houseReservation; }

        public List<Reservations> getExtraReservations() { return extraReservations; }
        public void setExtraReservations(List<Reservations> extraReservations) { this.extraReservations = extraReservations; }   
    }
    
    private List<ReservationItem> findReservations() {
        List<Reservations> houseReservations = reservationFacade.findByPrincipalNotCanceledHouseAndHolidayOnly(getPrincipal());
        List<Reservations> extraReservations = reservationFacade.findByPrincipalNotCanceledExtraOnly(getPrincipal());
        
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
        reservationFacade.cancelReservation(selectedItem.getHouseReservation().getPaymentid(), getPrincipal());
        //userSessionBean.setUser(getPrincipal());
        
        return RESERVATIONS_HISTORY_PAGE;
    }
    
    public boolean canBeCanceled(ReservationItem item) {
        return item.houseReservation.getReservedfrom().after(new Date());
    }
    
    public boolean canOrderExtras(ReservationItem item) {
        Date now = new Date();
        if (item.houseReservation.getHouseid() == null)
            return false;
        
        return (item.houseReservation.getReservedfrom().before(now) && 
                item.houseReservation.getReservedto().after(now) && !
                item.houseReservation.getHouseid().getExtrasList().isEmpty());
    }
    
    public String navigateToExtrasReservation(ReservationItem item) {
        return "extras-reservation.xhtml?faces-redirect=true&id=" + item.getHouseReservation().getId();
    }
    
    public void preselectReservation(ReservationItem item) {
        selectedItem = item;
    }
    
    public void unselectExtraReservation() {
        selectedExtraReservation = null;
    }
    
    public int calculateNumberOfHours() {
        if (selectedExtraReservation == null)
            return 0;
        long diff = selectedExtraReservation.getReservedto().getTime() - selectedExtraReservation.getReservedfrom().getTime();
        return (int) diff / (60 * 60 * 1000);
    }

    public Principals getPrincipal() {
        if (principal == null) {
            principal = userSessionBean.getUser();
        }
        
        return principal;
    }

    public void setPrincipal(Principals principal) {
        this.principal = principal;
    }

    public Reservations getSelectedExtraReservation() {
        return selectedExtraReservation;
    }

    public void setSelectedExtraReservation(Reservations selectedExtraReservation) {
        this.selectedExtraReservation = selectedExtraReservation;
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
