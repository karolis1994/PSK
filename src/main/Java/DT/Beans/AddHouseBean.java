package DT.Beans;

import DT.Entities.Houses;
import DT.Entities.Paidservices;
import DT.Facades.HouseFacade;
import DT.Facades.PaidServicesFacade;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


/**
 * @author Laurynas
 */

@ManagedBean(name = "addHouseBean")
@RequestScoped
public class AddHouseBean implements Serializable {
    
    // Fields ------------------------------------------------------------------
    
    @Size(min=1,max=255)
    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    @Size(min=1,max=255)
    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Size(min=1,max=255)
    private String address;
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    @Min(1)
    private int capacity;
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    private int availableFrom;
    public int getAvailableFrom() { return availableFrom; }
    public void setAvailableFrom(int availableFrom) { this.availableFrom = availableFrom; }
    
    private int availableTo;
    public int getAvailableTo() { return availableTo; }
    public void setAvailableTo(int availableTo) { this.availableTo = availableTo; }
    
    private Map<String, Integer> months;
    public Map<String, Integer> getMonths() { return months; }
    
    private int costInPoints;
    public int getCostInPoints() { return costInPoints; }
    public void setCostInPoints(int costInPoints) { this.costInPoints = costInPoints; }
    
    private double cost;
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    
    @EJB
    private HouseFacade houseFacade;
    
    @EJB
    private PaidServicesFacade paidServicesFacade;
    
    private Houses house;
    
    // Methods -----------------------------------------------------------------   
    
    @PostConstruct
    void init() {
        months = new LinkedHashMap<>();
        months.put("Sausis", 1);
        months.put("Vasaris", 2);
        months.put("Kovas", 3);
        months.put("Balandis", 4);
        months.put("Gegužė", 5);
        months.put("Birželis", 6);
        months.put("Liepa", 7);
        months.put("Rugpjūtis", 8);
        months.put("Rugsėjis", 9);
        months.put("Spalis", 10);
        months.put("Lapkritis", 11);
        months.put("Gruodis", 12);
    }
    
    // Saves house to the database
    public void save() {
        if (houseFacade.findWhere("o.title = '" + title + "'").isEmpty()) {
            if(house == null) {
                house = new Houses();
                house.setTitle(title);
                house.setDescription(description);
                house.setAddress(address);
                house.setIsclosed(Boolean.FALSE);
                house.setIsdeleted(Boolean.FALSE);
                house.setCapacity(capacity);
                house.setAvailablefrom(availableFrom);
                house.setAvailableto(availableTo);
            }
            
            if (availableFrom > availableTo) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Klaida: ", "Pasirinktas neteisingas naudojimosi laikotarpis."));
                return;
            }
            
            houseFacade.create(house);
            
            Paidservices housePaidService = new Paidservices();
            housePaidService.setCost(cost);
            housePaidService.setCostInPoints(costInPoints);
            housePaidService.setHouseid(house);
            
            paidServicesFacade.create(housePaidService);
            
            List<Paidservices> paidServices = house.getPaidservicesList();
            if (paidServices == null) {
                paidServices = new LinkedList<>();
            }
            
            paidServices.add(housePaidService);
            house.setPaidservicesList(paidServices);
            
            houseFacade.edit(house);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Vasarnamis sukurtas."));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Klaida: ", "Vasarnamis tokiu pavadinimu jau egzistuoja."));
        }
    }
}
