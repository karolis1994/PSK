/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Reservations;
import javax.ejb.Stateless;

/**
 *
 * @author Henrikas
 */
@Stateless
public class ReservationFacade extends GenericFacade<Reservations> {
    
    public ReservationFacade() {
        super(Reservations.class);
    }
}
