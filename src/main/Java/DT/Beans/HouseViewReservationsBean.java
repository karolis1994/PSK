package DT.Beans;

import DT.Entities.Houses;
import DT.Entities.Reservations;
import DT.Facades.HouseFacade;
import DT.Facades.ReservationFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Laurynas
 */

@Named
@RequestScoped
public class HouseViewReservationsBean {
    
    @Inject
    ReservationFacade reservationsFacade;
    
    @Inject
    HouseFacade houseFacade;
    
    Houses house;
    
    @PostConstruct
    void init() {
        
        String houseIDString = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        int houseID = Integer.parseInt(houseIDString);
        
        house = houseFacade.find(houseID);
    }
    
    public List<Reservations> getReservations() {
        
        return house.getReservationsList();
    }
}
