/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Paidservices;
import DT.Entities.Payments;
import DT.Entities.Principals;
import DT.Entities.Reservations;
import java.util.Date;
import java.util.List;
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
    
    public void createPayPalPayment(Principals principal, Paidservices paidService, String paymentNo) {
        
        createPayPalPayment(principal, paidService, paymentNo, 1);
    }
    
    public void createPayPalPayment(Principals principal, Paidservices paidService, String paymentNo, int amount) {
        
        double price = paidService.getCost();
        price *= amount;
        
        // Create payment object
        Payments payment = new Payments();
        
        payment.setPaidWithPoints(false);
        payment.setAmmount(price);
        payment.setCreatedat(new Date());
        payment.setIspaid(false);
        payment.setPaidserviceid(paidService);
        payment.setPrincipalid(principal);
        payment.setVersion(1);
        payment.setPaymentno(paymentNo);
        payment.setBoughtitems(amount);
        
        create(payment);
    }
    
    public Payments findByPaymentNo(String paymentNo) {
        List<Payments> result = em.createNamedQuery("Payments.findByPaymentno")
                .setParameter("paymentno", paymentNo).getResultList();
        
        if (result.size() > 0)
        {
            return result.get(0);
        }
        else
        {
            return null;
        }
    }
    
    public void completePayment(String paymentNo) {
        
        Payments payment = findByPaymentNo(paymentNo);
        
        if (payment != null)
        {
            payment.setIspaid(true);
            payment.setPayedat(new Date());
            
            edit(payment);
        }
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
    
    public void PayWithPoints(Payments payment, Principals principal) {
        principalsFacade.edit(principal);
        create(payment);
    }
}
