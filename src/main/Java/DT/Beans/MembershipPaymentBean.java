package DT.Beans;

import DT.Entities.Paidservices;
import DT.Entities.Principals;
import DT.Facades.PaidServicesFacade;
import DT.Facades.PaymentsFacade;
import DT.Facades.PrincipalsFacade;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Laurynas
 */

@Named("membershipPaymentBean")
@ConversationScoped
public class MembershipPaymentBean implements Serializable {
    
    @Inject
    UserSessionBean userSessionBean;
    
    @EJB
    PrincipalsFacade principalsFacade;
    
    @EJB
    PaymentsFacade paymentsFacade;
    
    @EJB
    PaidServicesFacade paidServicesFacade;
    
    @PostConstruct
    public void init() {
    }
    
    public String getMembershipValidUntill() {
        
        Date membershipUntill = userSessionBean.getUser().getMembershipuntill();
        
        if (membershipUntill == null) {
            return "Narystė negaliojanti.";
        }
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        if (membershipUntill.before(new Date())) {
            return "Jūsų narystė baigėsi: " + df.format(membershipUntill) + ".";
        }
        
        return "Jūsų narystė galioji iki: " + df.format(membershipUntill) + ".";
    }
    
    public String getNextPaymentDate() throws MalformedURLException, IOException {
        
//        PayPal pp = new PayPal(profile, PayPal.Environment.LIVE)
        
        
        
        return "";
    }
    
    public String payMembersFeeWithPoints() {
        
        Principals payer = userSessionBean.getUser();
        Paidservices membership = paidServicesFacade.find(1); // TODO proper way to retreive membership fee.
        
        try {
            paymentsFacade.PayWithPoints(payer, membership);
        } catch (Exception e) {
            
        }
        
        // Extend membership
        Date membershipUntill = payer.getMembershipuntill();
        
        if (membershipUntill == null || membershipUntill.before(new Date())) 
        {
            Calendar todayPlusOneYear = Calendar.getInstance();
            todayPlusOneYear.add(Calendar.YEAR, 1);
            membershipUntill = todayPlusOneYear.getTime();
        }
        else
        {
            Calendar newMembershipUntill = Calendar.getInstance();
            newMembershipUntill.setTime(membershipUntill);
            newMembershipUntill.add(Calendar.YEAR, 1);
            membershipUntill = newMembershipUntill.getTime();
        }
        
        payer.setMembershipuntill(membershipUntill);
        principalsFacade.edit(payer);
        
        return "logged-in/index.xhtml";
    }
}
