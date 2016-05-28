/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Entities.Reservations;
import DT.Facades.PrincipalsFacade;
import DT.Facades.ReservationFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Karolis
 */
@Named("usersViewAllBean")
@ViewScoped
public class UsersViewAllBean implements Serializable {
    
    // Fields-------------------------------------------------------------------
    
    private Principals selectedPrincipal;
    public Principals getSelectedPrincipal() { return selectedPrincipal; }
    public void setSelectedPrincipal(Principals selectedPrincipal) { this.selectedPrincipal = selectedPrincipal; }
        
    private List<Principals> allPrincipals;
    public List<Principals> getAllPrincipals() { return allPrincipals; }
    
    private List<Principals> filteredPrincipals;
    public List<Principals> getFilteredPrincipals() { return filteredPrincipals; }
    public void setFilteredHouses(List<Principals> filteredPrincipals) { this.filteredPrincipals = filteredPrincipals;}
    
    private Date dateFromFilter;
    public Date getDateFromFilter() { return dateFromFilter; }
    public void setDateFromFilter(Date dateFromFilter) { this.dateFromFilter = dateFromFilter; }
    
    private Date dateToFilter;
    public Date getDateToFilter() { return dateToFilter; }
    public void setDateToFilter(Date dateToFilter) { this.dateToFilter = dateToFilter; }
    
    @Inject
    private ReservationFacade reservationFacade;
    @Inject 
    private PrincipalsFacade principalsFacade;    
    
    // Methods------------------------------------------------------------------
    @PostConstruct
    public void init() {
        allPrincipals = principalsFacade.findAllNotDeleted();
        filteredPrincipals = allPrincipals;
    }
    
    public void navigateToSelected(SelectEvent event) {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("logged-in/user-profile.xhtml?userID=" + selectedPrincipal.getId());
        } catch (IOException ex) {
            Logger.getLogger(UsersViewAllBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Principals> filter() {
        filteredPrincipals = new ArrayList<>(allPrincipals);
        if (dateFromFilter != null && dateToFilter != null) {
            filterByDate();
        }
        return filteredPrincipals;
    }
    
    public List<Principals> filterByDate() {
        boolean has;
        List<Reservations> reservations = reservationFacade.findByDatesCoveringNotCanceledHouseOnly(dateFromFilter, dateToFilter);
        if(!reservations.isEmpty()) {
            Iterator<Principals> iter = filteredPrincipals.iterator();
            while (iter.hasNext()) {
                Principals h = iter.next();
                has = false;
                for (Reservations r : reservations) {
                    if (r.getPrincipalid().equals(h)) {
                        has = true;
                    } 
                }
                if(!has)
                    iter.remove();
            }
            return filteredPrincipals;
        } else {
            filteredPrincipals.clear();
            return filteredPrincipals;
        }   
    }
    
    public void resetDatesFilter() {
        dateFromFilter = null;
        dateToFilter = null;
        filter();
    }
    
    public void resetAllFilters() {
        dateFromFilter = null;
        dateToFilter = null;
        filteredPrincipals = allPrincipals;
    }
    
    
}
