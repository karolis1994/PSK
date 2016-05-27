/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Payments;
import java.text.DecimalFormat;
import java.util.Map;
import paypalnvp.core.PayPal;
import paypalnvp.fields.Currency;
import paypalnvp.fields.Payment;
import paypalnvp.fields.PaymentAction;
import paypalnvp.fields.PaymentItem;
import paypalnvp.profile.BaseProfile;
import paypalnvp.profile.Profile;
import paypalnvp.request.DoExpressCheckoutPayment;
import paypalnvp.request.GetExpressCheckoutDetails;
import paypalnvp.request.SetExpressCheckout;
/*
 * @author Laurynas
 */
public class PayPalHelper {
    public static Map<String, String> getCheckOutToken(String amount, String description) {
        /* set user - these are your credentials from paypal */
        Profile user = new BaseProfile.Builder("labanorodraugai_api1.gmail.com",
            "TUTKLAXDGPXETKQS").signature("AFcWxV21C7fd0v3bYYYRCpSSRl31AUT.gAv4nER31qMu8bAXBhcXZiDt").build();

        /* create new instance of paypal nvp */
        PayPal pp = new PayPal(user, PayPal.Environment.SANDBOX);

        /* create items (items from a shopping basket) */
        PaymentItem item1 = new PaymentItem();
        item1.setAmount(amount);
        item1.setDescription(description);

        PaymentItem[] items = {item1};

        /* create payment (now you can create payment from the items) */
        Payment payment = new Payment(items);
        payment.setCurrency(Currency.EUR);

        /* create set express checkout - the first paypal request */
        SetExpressCheckout setEC = new SetExpressCheckout(payment,
            "http://localhost:8080/DT.ReservationSystem/logged-in/payments/paypal-complete.xhtml", "http://localhost:8080/DT.ReservationSystem/");

        setEC.setNoShipping(true);
        setEC.setRequireConfirmedShipping(false);
//        setEC.setPaymentAction(PaymentAction.SALE);
        
        /* send request and set response */
        pp.setResponse(setEC);

        /* now you have set express checkout containting */
        /* request and response as well */
        Map<String, String> response = setEC.getNVPResponse();
        return response;
    }
    
    public static Map<String, String> getExpressCheckoutDetails(Payments payment) {
        /* set user - these are your credentials from paypal */
        Profile user = new BaseProfile.Builder("labanorodraugai_api1.gmail.com",
            "TUTKLAXDGPXETKQS").signature("AFcWxV21C7fd0v3bYYYRCpSSRl31AUT.gAv4nER31qMu8bAXBhcXZiDt").build();

        /* create new instance of paypal nvp */
        PayPal pp = new PayPal(user, PayPal.Environment.SANDBOX);

        GetExpressCheckoutDetails getEC = new GetExpressCheckoutDetails(payment.getPaymentno().toString());
                
        pp.setResponse(getEC);

        /* now you have set express checkout containting */
        /* request and response as well */
        Map<String, String> response = getEC.getNVPResponse();
        return response;
    }
    
    public static Map<String, String> doExpressCheckoutPayment(Payments payment, String payerID) {
        
        /* set user - these are your credentials from paypal */
        Profile user = new BaseProfile.Builder("labanorodraugai_api1.gmail.com",
            "TUTKLAXDGPXETKQS").signature("AFcWxV21C7fd0v3bYYYRCpSSRl31AUT.gAv4nER31qMu8bAXBhcXZiDt").build();

        /* create new instance of paypal nvp */
        PayPal pp = new PayPal(user, PayPal.Environment.SANDBOX);
        
        PaymentItem item = new PaymentItem();
        DecimalFormat df = new DecimalFormat("#.00"); 
        String formattedString = df.format(payment.getAmmount()).replace(',', '.');
        item.setAmount(formattedString);
        
        PaymentItem[] items = {item};
        
        Payment paymentPP = new Payment(items);
        paymentPP.setCurrency(Currency.EUR);
        
        String token = payment.getPaymentno();
        
        DoExpressCheckoutPayment doEC = new DoExpressCheckoutPayment(paymentPP, token, PaymentAction.SALE, payerID);
    
        pp.setResponse(doEC);
        
        Map<String, String> response = doEC.getNVPResponse();
        return response;
    } 
}
