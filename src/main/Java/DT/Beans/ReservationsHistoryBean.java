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
import java.util.List;
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
    private List<Reservations> reservations;
    private Reservations selectedReservation;
    
    private List<Reservations> findReservations() {
        reservations = reservationFacade.findByPrincipalNotCanceled(getPrincipal());
        
        return reservations;
    }

    public String cancelReservation() {
        selectedReservation.setIscanceled(true);
        reservationFacade.edit(selectedReservation);
        
        return RESERVATIONS_HISTORY_PAGE;
    }
    
    public void preselectReservation(Reservations reservation) {
        selectedReservation = reservation;
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

    public List<Reservations> getReservations() {
        if (reservations == null) {
            findReservations();
        }
        
        return reservations;
    }

    public void setReservations(List<Reservations> reservations) {
        this.reservations = reservations;
    }

    public Reservations getSelectedReservation() {
        return selectedReservation;
    }

    public void setSelectedReservation(Reservations selectedReservation) {
        this.selectedReservation = selectedReservation;
    }
}
