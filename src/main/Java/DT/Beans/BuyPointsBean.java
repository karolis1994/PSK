package DT.Beans;

import DT.Entities.Paidservices;
import DT.Entities.Principals;
import DT.Facades.PaidServicesFacade;
import DT.Facades.PaymentsFacade;
import DT.Facades.PrincipalsFacade;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Laurynas
 */

@Named("buyPointsBean")
@ViewScoped
public class BuyPointsBean implements Serializable {
    
    public Integer getCurrentPoints() { return userSessionBean.getUser().getPoints(); }
    
    private double pointPrice;
    public double getPointPrice() { return pointPrice; }
    
    private Integer pointsToBuy;
    public Integer getPointsToBuy() { return pointsToBuy; }
    public void setPointsToBuy(Integer points) { this.pointsToBuy = points; }
    
    @Inject
    private PrincipalsFacade principalsFacade;
    
    @Inject
    private PaidServicesFacade paidServicesFacade;
    
    @Inject
    private UserSessionBean userSessionBean;
    
    @Inject
    private PaymentsFacade paymentsFacade;
    
    @PostConstruct
    void init() {
        
        Paidservices buyPoints = paidServicesFacade.find(2);
        pointPrice = buyPoints.getCost();

    }
    
    public void buyPoints() throws IOException {
        
        Paidservices buyPoints = paidServicesFacade.find(2);
        double price = buyPoints.getCost();
        price *= pointsToBuy;
        
        DecimalFormat df = new DecimalFormat("#.00"); 
        String formattedPrice = df.format(price).replace(',', '.');
        
        Map<String, String> response = PayPalHelper.getCheckOutToken(formattedPrice, buyPoints.getTitle());
        
        if ("success".equals(response.get("ACK").toLowerCase()))
        {
            String token = response.get("TOKEN");
            
            if (token != null)
            {
                String paymentUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + token;
                
                paymentsFacade.createPayPalPayment(userSessionBean.getUser(), buyPoints, token, pointsToBuy);
                
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                externalContext.redirect(paymentUrl);
            }
        }
    }
}
