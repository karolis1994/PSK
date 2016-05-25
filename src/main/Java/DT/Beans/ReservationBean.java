/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Extras;
import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Entities.Payments;
import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.HouseFacade;
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
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Henrikas
 */
@Named(value = "reservationBean")
@ViewScoped
public class ReservationBean implements Serializable{
    private final String PAGE_AFTER_RESERVING = "reservations-history?faces-redirect=true";
    private final String MESSAGE_PERIOD_TAKEN = "Pasirinktu laikotarpiu vasarnamis yra užimtas. Pasirinkite kitą laikotarpį";
    private final String MESSAGE_NOT_APPROVED = "Jūs negalite rezervuoti šio vasarnamio. Priežastis: Jūsų narystė nepatvirtinta";
    private final String MESSAGE_INSUFFICIENT_POINTS = "Jūs turite nepakankamai taškų";
    private final String MESSAGE_COMPONENT_CALENDAR = "calendar";
    private final String MESSAGE_COMPONENT_REVIEW = "review";
    
    @Inject private ReservationFacade reservationsFacade;
    @Inject private HouseFacade houseFacade;
    @Inject private PaymentsFacade paymentsFacade;
    @Inject private UserSessionBean userSessionBean;
    
    private Reservations reservation;
    private Date reservedFrom;
    private Date reservedTo;
    private Date selectedDate;
    private Date minExtraDate = new Date();
    private List<Extras> extrasList;
    private Houses house;
    private int houseID;
    private int totalPrice = 0;
    private int housePrice = 0;
    private int totalHousePrice = 0;
    private int numberOfWeeks = 1;
    private boolean extrasTableVisible = false;
    private boolean stepOneVisible = true;
    private boolean stepTwoVisible = false;
    private boolean stepThreeVisible = false;
    private boolean stepFourVisible = false;
    private List<ExtraItem> extraItems = new ArrayList<>();
    private List<ExtraItem> selectedExtraItems = new ArrayList<>();
    
    public static class ExtraItem implements Serializable {
        private Extras extra;
        private Date reservedFrom;
        private Date reservedFromTime;
        private int numberOfHours = 1;

        public Extras getExtra() { return extra; }
        public void setExtra(Extras extra) { this.extra = extra; }

        public Date getReservedFrom() { return reservedFrom; }
        public void setReservedFrom(Date reservedFrom) { this.reservedFrom = reservedFrom; }

        public Date getReservedFromTime() { return reservedFromTime; }
        public void setReservedFromTime(Date reservedFromTime) { this.reservedFromTime = reservedFromTime; }

        public int getNumberOfHours() { return numberOfHours; }
        public void setNumberOfHours(int numberOfHours) { this.numberOfHours = numberOfHours; }
    }
    
    public void init() {
        house = houseFacade.find(houseID);
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
        
        
        if (!findReserved().isEmpty()) {
            FacesContext.getCurrentInstance()
                    .addMessage(MESSAGE_COMPONENT_REVIEW, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, MESSAGE_PERIOD_TAKEN));
            return "";
        }
            
        princ.setPoints(princ.getPoints() - totalPrice);

        Payments payment = new Payments();
        payment.setPrincipalid(princ);
        payment.setPaidserviceid(getRentService());
        payment.setCreatedat(Calendar.getInstance().getTime());
        payment.setPayedat(Calendar.getInstance().getTime());
        payment.setAmmount(totalPrice);
        payment.setIspaid(true);
        payment.setPaidWithPoints(true);

        Reservations res = new Reservations();
        res.setHouseid(house);
        res.setReservedfrom(reservedFrom);
        res.setReservedto(reservedTo);
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
            cal.add(Calendar.HOUR_OF_DAY, ei.numberOfHours);
            Date resToDate = cal.getTime();

            Reservations extraRes = new Reservations();
            extraRes.setHouseid(house);
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
        paymentsFacade.PayWithPoints(payment, princ);
        userSessionBean.setUser(princ);
        
        return PAGE_AFTER_RESERVING;
    }
    
    public List<Reservations> findReserved() {
        List<Reservations> reservations = reservationsFacade.findByDatesCoveringNotCanceledExtraIdNull(reservedFrom, reservedTo);
        return reservations;
    }
    
    private int calculateTotalPrice() {
        totalPrice = getHousePrice() * numberOfWeeks;
        for(ExtraItem ei : extraItems) {
            if (ei.getReservedFrom() != null) {
                totalPrice += ei.getExtra().getPaidservicesList().get(0).getCost() * ei.getNumberOfHours();
            }
        }
        
        return totalPrice;
    }
    
    public void selectDate(SelectEvent evt) {        
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(selectedDate);
        fromCal.setTimeZone(TimeZone.getTimeZone("Europe/Vilnius"));
        fromCal.setFirstDayOfWeek(Calendar.MONDAY);
        fromCal.set(Calendar.DAY_OF_WEEK, fromCal.getFirstDayOfWeek());
        
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(selectedDate);
        toCal.setTimeZone(TimeZone.getTimeZone("Europe/Vilnius"));
        toCal.setFirstDayOfWeek(Calendar.MONDAY);
        toCal.set(Calendar.DAY_OF_WEEK, toCal.getFirstDayOfWeek());
        toCal.add(Calendar.DAY_OF_MONTH, 6 + (numberOfWeeks - 1)*7);
        
        reservedFrom = fromCal.getTime();
        reservedTo = toCal.getTime();

        minExtraDate = new Date();
        if (reservedFrom.after(minExtraDate))
            minExtraDate = reservedFrom;
        
        if (!findReserved().isEmpty()) {
            FacesContext.getCurrentInstance()
                    .addMessage(MESSAGE_COMPONENT_CALENDAR, new FacesMessage(FacesMessage.SEVERITY_WARN, null, MESSAGE_PERIOD_TAKEN));
            stepThreeVisible = false;
        } 
        else {
            stepTwoVisible = true;
            stepThreeVisible = true;
        }
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
    
    public void continueClicked() {
        stepOneVisible = false;
        stepTwoVisible = false;
        stepThreeVisible = false;
        stepFourVisible = true;
    }
    
    public void editClicked() {
        stepOneVisible = true;
        stepTwoVisible = true;
        stepThreeVisible = true;
        stepFourVisible = false;
    } 
    
    public void reset() {
        house = null;
        numberOfWeeks = 1;
        totalPrice = 0;
        selectedExtraItems = new ArrayList<>();
    }
    
    private Paidservices getRentService() {
        for (Paidservices ps : house.getPaidservicesList()) {
            if (ps.getHouseid() != null) {
                return ps;
            }
        }
        return null;
    }
    
    public int getTotalHousePrice() {
        totalHousePrice = housePrice * numberOfWeeks;
        return totalHousePrice;
    }

    public void setTotalHousePrice(int totalHousePrice) {
        this.totalHousePrice = totalHousePrice;
    }
    
    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }
    public void setNumberOfWeeks(int numberOfWeeks) {      
        this.numberOfWeeks = numberOfWeeks;
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(reservedFrom);
        toCal.add(Calendar.DAY_OF_MONTH, 6 + (this.numberOfWeeks - 1)*7);
        reservedTo = toCal.getTime();
        
        if (!findReserved().isEmpty()) {
            FacesContext.getCurrentInstance()
                    .addMessage(MESSAGE_COMPONENT_CALENDAR, new FacesMessage(FacesMessage.SEVERITY_WARN, null, MESSAGE_PERIOD_TAKEN));
            stepThreeVisible = false;
        } 
        else {
            stepThreeVisible = true;
        }
    }
    
    public int getHousePrice() {
        if (housePrice == 0) {
            for (Paidservices ps : house.getPaidservicesList()) {
                if (ps.getHouseid() != null) {
                    housePrice = ps.getCostInPoints();
                    break;
                }
            }
        }
        return housePrice;
    }

    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }

    public Houses getHouse() {
        return house;
    }

    public void setHouse(Houses house) {
        this.house = house;
    }

    public int getTotalPrice() {
        calculateTotalPrice();
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public List<Extras> getExtrasList() {
        if (extrasList == null) {
            extrasList = house.getExtrasList();
        }
        
        return extrasList;
    }

    public void setExtrasList(List<Extras> extrasList) {
        this.extrasList = extrasList;
    }

    public Reservations getReservation() {
        if (reservation == null) {
            reservation = new Reservations();
        }
        return reservation;
    }
    
    public boolean isExtrasTableVisible() {
        return extrasTableVisible;
    }

    public void setExtrasTableVisible(boolean extrasTableVisible) {
        this.extrasTableVisible = extrasTableVisible;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
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

    public boolean isStepThreeVisible() {
        return stepThreeVisible;
    }

    public void setStepThreeVisible(boolean stepThreeVisible) {
        this.stepThreeVisible = stepThreeVisible;
    }
    
    public boolean isStepFourVisible() {
        return stepFourVisible;
    }

    public void setStepFourVisible(boolean stepFourVisible) {
        this.stepFourVisible = stepFourVisible;
    }

    public Date getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(Date reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public Date getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(Date reservedTo) {
        this.reservedTo = reservedTo;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }   

    public Date getMinExtraDate() {
        return minExtraDate;
    }

    public void setMinExtraDate(Date minExtraDate) {
        this.minExtraDate = minExtraDate;
    }
    
    public List<ExtraItem> getExtraItems() {
        if (extrasList == null) {
            List<Extras> extras = getExtrasList();
            for (Extras e : extras) {
                ExtraItem ei = new ExtraItem();
                ei.extra = e;
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
