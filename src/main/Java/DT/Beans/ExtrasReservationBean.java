/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Beans.ReservationBean.ExtraItem;
import DT.Entities.Extras;
import DT.Entities.Payments;
import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.PaymentsFacade;
import DT.Facades.ReservationFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;

/**
 *
 * @author Henrikas
 */
@Named
@ViewScoped
public class ExtrasReservationBean implements Serializable {
    private final String PAGE_AFTER_RESERVING = "reservations-history?faces-redirect=true";
    private final String MESSAGE_NOT_APPROVED = "Jūs negalite rezervuoti šio vasarnamio. Priežastis: Jūsų narystė nepatvirtinta";
    private final String MESSAGE_INSUFFICIENT_POINTS = "Jūs turite nepakankamai taškų";
    private final String MESSAGE_OPTILOCK = "Įvyko klaida. Bandykite dar kartą";
    private final String MESSAGE_COMPONENT_REVIEW = "review";
    
    @Inject private UserSessionBean userSessionBean;
    @Inject private PaymentsFacade paymentsFacade;
    @Inject private ReservationFacade reservationFacade;
    
    private int ID;

    private boolean stepOneVisible = true;
    private boolean stepTwoVisible = false;
    private Date minExtraDate;
    
    private Reservations reservation;
    
    private int totalPrice;
    
    private List<ExtraItem> extraItems;
    private List<ExtraItem> selectedExtraItems = new ArrayList<>();
    
    public void init() {
        reservation = getReservation();
        extraItems = getExtraItems();
    }
    
    public String saveReservation() {           
        Principals princ = userSessionBean.getUser();
        
        if (!princ.getIsapproved()) {
            FacesContext.getCurrentInstance()
                    .addMessage(MESSAGE_COMPONENT_REVIEW, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, MESSAGE_NOT_APPROVED));
            return "";
        }
        
        if (princ.getPoints() < totalPrice) {
            FacesContext.getCurrentInstance()
                    .addMessage(MESSAGE_COMPONENT_REVIEW, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, MESSAGE_INSUFFICIENT_POINTS));
            return "";
        }
            
        princ.setPoints(princ.getPoints() - totalPrice);

        Payments payment = new Payments();
        payment.setPrincipalid(princ);
        payment.setPaidserviceid(reservation.getPaymentid().getPaidserviceid());
        payment.setCreatedat(Calendar.getInstance().getTime());
        payment.setPayedat(Calendar.getInstance().getTime());
        payment.setAmmount(totalPrice);
        payment.setIspaid(true);
        payment.setPaidWithPoints(true);

        Reservations res = new Reservations();
        res.setReservedfrom(reservation.getReservedfrom());
        res.setReservedto(reservation.getReservedto());
        res.setIscanceled(false);
        res.setPrincipalid(princ);
        res.setPaymentid(payment);

        List<Reservations> allReservations = new ArrayList<>();
        for (ExtraItem ei : selectedExtraItems) {
            Date resFromDate = new Date(
                     ei.getReservedFrom().getYear(), ei.getReservedFrom().getMonth(), ei.getReservedFrom().getDay(), 
                     ei.getReservedFromTime().getHours(), ei.getReservedFromTime().getMinutes(), ei.getReservedFromTime().getSeconds());

            Calendar cal = Calendar.getInstance();
            cal.setTime(resFromDate);
            cal.add(Calendar.HOUR_OF_DAY, ei.getNumberOfHours());
            Date resToDate = cal.getTime();

            Reservations extraRes = new Reservations();
            extraRes.setExtraid(ei.getExtra());
            extraRes.setReservedfrom(resFromDate);
            extraRes.setReservedto(resToDate);
            extraRes.setIscanceled(false);
            extraRes.setPrincipalid(princ);
            extraRes.setPaymentid(payment);
            allReservations.add(extraRes);
        }

        allReservations.add(res);
        payment.setReservationsList(allReservations);
        try {
            paymentsFacade.PayWithPoints(payment, princ);
            userSessionBean.setUser(princ);
        } catch(OptimisticLockException ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(MESSAGE_COMPONENT_REVIEW, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, MESSAGE_OPTILOCK));
            return "";
        }
        
        return PAGE_AFTER_RESERVING;
    }
    
    public void extraSelectDate(Extras extra) {
        ExtraItem selectedItem = null;
        for (ExtraItem ei : extraItems) {
            if (ei.getExtra().equals(extra)) {
                selectedItem = ei;
                break;
            }
        }
        
        if (selectedItem != null) {
            if (selectedItem.getReservedFrom() == null) 
                selectedItem.setReservedFrom(getMinExtraDate());
            
            if (selectedItem.getReservedFromTime() == null)
                selectedItem.setReservedFromTime(getMinExtraDate());
            
            if (selectedItem.getNumberOfHours() == 0) 
                selectedItem.setNumberOfHours(1);
        }
        
        if (!selectedExtraItems.contains(selectedItem)) {
            selectedExtraItems.add(selectedItem);
        }
    }
    
    public void clearExtraSelection(Extras extra) {
        for (ExtraItem ei : extraItems) {
            if (ei.getExtra().equals(extra)) {
                ei.setReservedFrom(null);
                ei.setReservedFromTime(null);
                ei.setNumberOfHours(1);
                
                if (selectedExtraItems.contains(ei)) {
                    selectedExtraItems.remove(ei);
                }
                break;
            }
        }
    }
    
    public int calculateTotalPrice() {
        totalPrice = 0;
        for(ExtraItem ei : extraItems) {
            if (ei.getReservedFrom() != null) {
                totalPrice += ei.getExtra().getPaidservicesList().get(0).getCost() * ei.getNumberOfHours();
            }
        }
        
        return totalPrice;
    }
    
    public void continueClicked() {
        stepOneVisible = false;
        stepTwoVisible = true;
    }
    
    public void editClicked() {
        stepOneVisible = true;
        stepTwoVisible = false;
    } 
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isStepOneVisible() {
        return stepOneVisible;
    }

    public void setStepOneVisible(boolean stepOneVisible) {
        this.stepOneVisible = stepOneVisible;
    }

    public boolean isStepTwoVisible() {
        return stepTwoVisible;
    }

    public void setStepTwoVisible(boolean stepTwoVisible) {
        this.stepTwoVisible = stepTwoVisible;
    }

    public Date getMinExtraDate() {
        if (minExtraDate == null) {
           
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.setTimeZone(TimeZone.getTimeZone("Europe/Vilnius"));
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            
            minExtraDate = cal.getTime();
            
            if (reservation.getReservedfrom().after(minExtraDate))
                minExtraDate = reservation.getReservedfrom();
        }
        
        return minExtraDate;
    }

    public void setMinExtraDate(Date minExtraDate) {
        this.minExtraDate = minExtraDate;
    }
    
    public Reservations getReservation() {
        if (reservation == null) {
            reservation = reservationFacade.find(ID);
        }
        
        return reservation;
    }

    public void setReservation(Reservations reservation) {
        this.reservation = reservation;
    }

    public int getTotalPrice() {
        calculateTotalPrice();
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public List<ExtraItem> getExtraItems() {        
        if (extraItems == null) {
            extraItems = new ArrayList<>();
            List<Extras> extras = reservation.getHouseid().getExtrasList();
            for (Extras e : extras) {
                ExtraItem ei = new ExtraItem();
                ei.setExtra(e);
                extraItems.add(ei);
            }
        }
        
        return extraItems;
    }

    public void setExtraItems(List<ExtraItem> extraItems) {
        this.extraItems = extraItems;
    }

    public List<ExtraItem> getSelectedExtraItems() {
        return selectedExtraItems;
    }

    public void setSelectedExtraItems(List<ExtraItem> selectedExtraItems) {
        this.selectedExtraItems = selectedExtraItems;
    }
    
}
