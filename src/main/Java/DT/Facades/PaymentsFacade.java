/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Entities.Payments;
import DT.Entities.Principals;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Laurynas
 */
@Stateless
public class PaymentsFacade extends GenericFacade<Payments> {
    
    @EJB
    private PrincipalsFacade principalsFacade;
    
    public PaymentsFacade() {
        super(Payments.class);
    }
    
    public void PayWithPoints(Principals principal, Paidservices paidService) throws Exception {
        
        int principalPoints = principal.getPoints();
        int serviceCost = paidService.getCostInPoints();
        
        // Check whether principal has enough points 
        if (principalPoints < serviceCost)
        {
            throw new Exception("Insufficient funds");
        }
        
        // Subract principal's funds
        principalPoints -= serviceCost;
        principal.setPoints(principalPoints);
        principalsFacade.edit(principal);
        
        // Create payment object
        Payments payment = new Payments();
        
        payment.setPaidWithPoints(true);
        payment.setAmmount(serviceCost);
        payment.setCreatedat(new Date());
        payment.setIspaid(true);
        payment.setPaidserviceid(paidService);
        payment.setPayedat(new Date());
        payment.setPrincipalid(principal);
        payment.setVersion(1);
        
        create(payment);
    }
}
