package DT.Beans;

import DT.Entities.Payments;
import DT.Entities.Principals;
import DT.Facades.PaidServicesFacade;
import DT.Facades.PaymentsFacade;
import DT.Facades.PrincipalsFacade;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @author Laurynas
 */

@ManagedBean(name = "membersFeeBean")
@RequestScoped
public class MembershipPaymentBean {
    
    @EJB
    PrincipalsFacade principalsFacade;
    
    @EJB
    PaymentsFacade paymentsFacade;
    
    @EJB
    PaidServicesFacade paidServicesFacade;
    
    public String getNextPaymentDate() {
        
        
        
        return "";
    }
    
    public String payMembersFeeWithPoints() {
        
        // Pay
        Principals payer = principalsFacade.find(1);
        
        int payerPoints = payer.getPoints();
        payerPoints -= 10;
        payer.setPoints(payerPoints);
        
        principalsFacade.edit(payer);
        
        // Create payment
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        
        Payments payment = new Payments();
        
        payment.setPrincipalid(payer);
        payment.setAmmount(0);
        payment.setCreatedat(new Date());
        payment.setIspaid(false);
        calendar.add(Calendar.YEAR, 1);
        payment.setPaidserviceid(paidServicesFacade.find(1));
               
        paymentsFacade.create(payment);
        
        return "logged-in/index.xhtml";
    }
}
