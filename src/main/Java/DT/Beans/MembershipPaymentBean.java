package DT.Beans;

import DT.Entities.Paidservices;
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
        
        Principals payer = principalsFacade.find(1);
        Paidservices membership = paidServicesFacade.find(1); // TODO proper way to retreive membership fee.
        
        try {
            paymentsFacade.PayWithPoints(payer, membership);
        } catch (Exception e) {
            
        }
        
        // Create payment
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());

        return "logged-in/index.xhtml";
    }
}
