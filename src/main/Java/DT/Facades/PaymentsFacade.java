/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Houses;
import DT.Entities.Payments;
import javax.ejb.Stateless;

/**
 *
 * @author Laurynas
 */
@Stateless
public class PaymentsFacade extends GenericFacade<Payments> {
    
    public PaymentsFacade() {
        super(Payments.class);
    }
    
}
