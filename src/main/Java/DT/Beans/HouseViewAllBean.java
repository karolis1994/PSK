package DT.Beans;

import DT.Entities.Houses;
import DT.Entities.Reservations;
import DT.Facades.HouseFacade;
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
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 * @author Laurynas
 */

@Named
@ViewScoped
public class HouseViewAllBean implements Serializable {
    
    // Fields-------------------------------------------------------------------
    
    private Houses selectedHouse;
    public Houses getSelectedHouse() { return selectedHouse; }
    public void setSelectedHouse(Houses selectedHouse) { this.selectedHouse = selectedHouse; }
        
    private List<Houses> allHouses;
    public List<Houses> getAllHouses() { return allHouses; }
    
    private List<Houses> filteredHouses;
    public List<Houses> getFilteredHouses() { return filteredHouses; }
    public void setFilteredHouses(List<Houses> filteredHouses) { this.filteredHouses = filteredHouses;}
    
    private Date dateFromFilter;
    public Date getDateFromFilter() { return dateFromFilter; }
    public void setDateFromFilter(Date dateFromFilter) { this.dateFromFilter = dateFromFilter; }
    
    private Date dateToFilter;
    public Date getDateToFilter() { return dateToFilter; }
    public void setDateToFilter(Date dateToFilter) { this.dateToFilter = dateToFilter; }
    
    private int capacityFilter;
    public int getCapacityFilter() { return capacityFilter; }
    public void setCapacityFilter(int capacityFilter) { this.capacityFilter = capacityFilter; }
    
    @EJB
    private HouseFacade houseFacade;
    @EJB
    private ReservationFacade reservationFacade;
    
    // Methods------------------------------------------------------------------
    
    @PostConstruct
    public void init() {
        allHouses = houseFacade.findAllNotDeleted();
        filteredHouses = allHouses;
    }
    
    public void navigateToSelected(SelectEvent event) {
        try {
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("logged-in/house-preview.xhtml?id=" + selectedHouse.getId());
        } catch (IOException ex) {
            Logger.getLogger(HouseViewAllBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Houses> filter() {
        filteredHouses = new ArrayList<>(allHouses);
        
        if (capacityFilter != 0) {
            filterByCapacity();
        }
        
        if (dateFromFilter != null && dateToFilter != null) {
            filterByDate();
        }
        
        return filteredHouses;
    }
    
    public List<Houses> filterByDate() {       
        List<Reservations> reservations = reservationFacade.findByDatesCoveringNotCanceledExtraIdNull(dateFromFilter, dateToFilter);
        
        Iterator<Houses> iter = filteredHouses.iterator();
        while (iter.hasNext()) {
            Houses h = iter.next();

            for (Reservations r : reservations) {
                if (r.getHouseid().equals(h)) {
                    iter.remove();
                    break;
                } 
            }
        }
        return filteredHouses;
    }
    
    public List<Houses> filterByCapacity() {
        Iterator<Houses> iter = filteredHouses.iterator();
        while (iter.hasNext()) {
            Houses h = iter.next();

            if (h.getCapacity() != capacityFilter) {
                iter.remove();
            }
        }
        return filteredHouses;
    }
    
    public void resetCapacityFilter() {
        capacityFilter = 0;
        filter();
    }
    
    public void resetDatesFilter() {
        dateFromFilter = null;
        dateToFilter = null;
        filter();
    }
    
    public void resetAllFilters() {
        capacityFilter = 0;
        dateFromFilter = null;
        dateToFilter = null;
        filteredHouses = allHouses;
    }
}
