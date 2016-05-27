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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
    
    @Inject
    Paidservices membership;
    
    @EJB
    PrincipalsFacade principalsFacade;
    
    @EJB
    PaymentsFacade paymentsFacade;
    
    @EJB
    PaidServicesFacade paidServicesFacade;
    
    @PostConstruct
    public void init() {
        membership = paidServicesFacade.find(1); // TODO proper way to retreive membership fee.
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
    
    public Paidservices getMembership() { return membership; }
    
    public String payMembersFeeWithPoints() {
        
        Principals payer = userSessionBean.getUser();
        
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
    
    public String payMembersFeeWithPayPal() throws IOException {
        
        Paidservices membership = paidServicesFacade.find(1);
        double price = membership.getCost();
        DecimalFormat df = new DecimalFormat("#.00"); 
        Map<String, String> response = PayPalHelper.getCheckOutToken(df.format(price), membership.getTitle());
        
        if ("success".equals(response.get("ACK").toLowerCase()))
        {
            String token = response.get("TOKEN");
            
            if (token != null)
            {
                String paymentUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + token;
                
                paymentsFacade.createPayPalPayment(userSessionBean.getUser(), membership, token);
                
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(paymentUrl);
                
                return paymentUrl;
            }
        }
        
        return "logged-in/index.xhtml";
    }
}
