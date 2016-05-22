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
    
    private String payPalCompleteText;
    public String getPayPalCompleteText() { return payPalCompleteText; }
    
    @PostConstruct
    void init() {
        paymentNo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token");
        payment = paymentsFacade.findByPaymentNo(paymentNo);
    
        Map<String, String> response = PayPalHelper.getExpressCheckoutDetails(payment);
        
        payerID = response.get("PAYERID");
        
        String ack = response.get("ACK");
        if (!ack.equals("Success") || payerID == null) {
            payPalCompleteText = "Įvyko klaida tvirtinant PayPal mokėjimą.";
            
            paymentsFacade.remove(payment);
            return;
        }
        
        // Preparing text
        payPalCompleteText = "";
        String firstName = response.get("FIRSTNAME");
        String lastName = response.get("LASTNAME");
        
        if (firstName != null && lastName != null) {
            payPalCompleteText = "Mokėtojas: " + firstName + " " + lastName;
            
            String email = response.get("EMAIL");
            if (email != null) {
                payPalCompleteText = payPalCompleteText + " (" + email + ")";
            }
            
            payPalCompleteText = payPalCompleteText + "\n";
        }
        
        String paymentFor = response.get("L_DESC0");
        if (paymentFor != null) {
            payPalCompleteText = payPalCompleteText + "Mokėjimas už: " + paymentFor + "\n";
        }
        
        String price = response.get("AMT");
        String currencyCode = response.get("CURRENCYCODE");
        if (price != null && currencyCode != null) {
            payPalCompleteText = payPalCompleteText + "Kaina: " + price + " " + currencyCode + "\n";
        }
    }
    
    public void confirmPayment() {
        payment = paymentsFacade.findByPaymentNo(paymentNo);
        Map<String, String> response = PayPalHelper.doExpressCheckoutPayment(payment, payerID);
        return;
    }
}
