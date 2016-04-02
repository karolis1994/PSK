/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Reservations;
import DT.Facades.ReservationFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Henrikas
 */
@ManagedBean(name = "reservationBean")
public class ReservationBean {
    @EJB
    private ReservationFacade reservationFacade;
    private List<Reservations> reservations;
    private Reservations reservation;
    private String reservedFrom;
    private String reservedTo;

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
}
