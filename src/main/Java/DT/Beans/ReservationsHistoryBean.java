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
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Henrikas
 */
@Named(value = "reservationsHistoryBean")
@RequestScoped
public class ReservationsHistoryBean implements Serializable {
    @Inject 
    private PrincipalsFacade principalsFacade;
    
    @Inject
    private ReservationFacade reservationFacade;
    
    private Principals principal;
    private List<Reservations> reservations;
    private Reservations selectedReservation;
    
    private List<Reservations> findReservations() {
        reservations = reservationFacade.findByPrincipal(getPrincipal());
        return reservations;
    }

    public void navigateToSelected(SelectEvent event) {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    //.redirect("reservation-preview.xhtml?id=" + selectedReservation.getId());
                    .redirect("index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(HouseViewAllBean.class.getName()).log(Level.SEVERE, null, ex);
        }
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
