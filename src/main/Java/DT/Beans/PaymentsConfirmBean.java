package DT.Beans;

import DT.Entities.Payments;
import DT.Facades.PaymentsFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

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
    
    // Methods------------------------------------------------------------------
    
    @PostConstruct
    public void Init() {
        payments = paymentsFacade.findAll();
    }
    
    public void confirmSelected() {
        
        selectedPayment.setIspaid(true);
        paymentsFacade.edit(selectedPayment);
    }
}
