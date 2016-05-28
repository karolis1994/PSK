package DT.Facades;

import DT.Entities.Extras;
import javax.ejb.Stateless;

/**
 * @author Laurynas
 */
@Stateless
public class ExtrasFacade extends GenericFacade<Extras>{
    
    public ExtrasFacade() {
        super(Extras.class);
    }
}
