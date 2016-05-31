package DT.Facades;

import DT.Entities.Extras;
import DT.Entities.Paidservices;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author Laurynas
 */
@Stateless
public class ExtrasFacade extends GenericFacade<Extras>{
    
    @Inject
    private PaidServicesFacade paidServicesFacade;
    
    public ExtrasFacade() {
        super(Extras.class);
    }
    
    @Override
    public void remove(Extras extra) {
                
        extra.setIsdeleted(true);
        em.merge(extra);
    }
}
