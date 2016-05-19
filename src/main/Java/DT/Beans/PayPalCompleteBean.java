package DT.Beans;

import DT.Entities.Payments;
import DT.Facades.PaymentsFacade;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * @author Laurynas
 */

@Named("payPalCompleteBean")
@ConversationScoped
public class PayPalCompleteBean implements Serializable {
    
    @EJB
    PaymentsFacade paymentsFacade;
    
    private String paymentNo;
    public String getPaymentNo() { return paymentNo; }
    public void setPaymentNo(String paymentNo) { this.paymentNo = paymentNo; }
    
    private Payments payment;
    public Payments getPayment() { return payment; }
    
    private String payerID;
    public String getPayerID() { return payerID; }
    public void setPayerID(String payerID) { this.payerID = payerID; }
    
    @PostConstruct
    void init() {
        String paymentNo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token");
        payerID = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("PayerID");
        payment = paymentsFacade.findByPaymentNo(paymentNo);
    
        Map<String, String> response = PayPalHelper.getExpressCheckoutDetails(payment);
    }
    
    public void confirmPayment() {
        payment = paymentsFacade.findByPaymentNo(paymentNo);
        
        int i = 0;
        
        if (i > 1)
        {
            i++;
        }
        i--;
    }
}
