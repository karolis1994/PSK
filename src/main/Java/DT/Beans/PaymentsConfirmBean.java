package DT.Beans;

import DT.Entities.Payments;
import DT.Entities.Principals;
import DT.Facades.PaymentsFacade;
import DT.Facades.PrincipalsFacade;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * @author Laurynas
 */
@ManagedBean
@RequestScoped
public class PaymentsConfirmBean {
    
    // Fields-------------------------------------------------------------------
    
    private Payments selectedPayment;
    public Payments getSelectedPayment() { return selectedPayment; }
    public void setSelectedPayment(Payments selectedPayment) { this.selectedPayment = selectedPayment; }
    
    private List<Payments> payments;
    public List<Payments> getPayments() { return payments; }
    public void setPayments(List<Payments> payments) { this.payments = payments; }

    @EJB
    private PaymentsFacade paymentsFacade;
    @Inject
    private PrincipalsFacade principalsFacade;
    
    // Methods------------------------------------------------------------------
    
    @PostConstruct
    public void Init() {
        payments = paymentsFacade.findAll();
    }
    
    public void confirmSelected() throws IOException {
        
        Boolean wasPaymentNotApproved = !selectedPayment.getIspaid();
        selectedPayment.setIspaid(true);
        
        if (wasPaymentNotApproved) {
            if (selectedPayment.getPaidserviceid().getId() == 2) {
                completeBuyingPoints(selectedPayment);
            } else if (selectedPayment.getPaidserviceid().getId() == 1) {
                completeMembershipPayment(selectedPayment);
            }
        }
        
        paymentsFacade.edit(selectedPayment);
    }
    
    public void completeBuyingPoints(Payments payment) throws IOException {
        
        int points = payment.getBoughtitems();
        Principals payer = payment.getPrincipalid();
        
        int currentPoints = payer.getPoints();
        payer.setPoints(points + currentPoints);
        
        principalsFacade.edit(payer);
        
    }
    
    public void completeMembershipPayment(Payments payment) throws IOException {
        
        Principals payer = payment.getPrincipalid();
        
        principalsFacade.extendMembership(payer);
        
    }
}
