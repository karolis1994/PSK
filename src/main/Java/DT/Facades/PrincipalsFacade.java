/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Principals;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Karolis
 */
@Stateless
public class PrincipalsFacade extends GenericFacade<Principals>{

    public PrincipalsFacade() {
        super(Principals.class);
    }

    public Principals findByEmail(String email) {
        List results = em.createNamedQuery("Principals.findByEmail")
                .setParameter("email", email).getResultList();
        return results.isEmpty() ?
                null :(Principals) results.get(0) ;       
    }

    @Override
    public List findAll() {
        return em.createNamedQuery("Principals.findAll").getResultList();
    }
    
    public List findAllApproved() {
        return em.createNamedQuery("Principals.findByIsapproved")
                .setParameter("isapproved", true).getResultList();
    }
    
    public Principals findByFacebookID(String facebookID) {
        List<Principals> result = em.createNamedQuery("Principals.findByFacebookid")
                .setParameter("facebookid", facebookID).getResultList();
        
        if(result.isEmpty())
            return null;
        
        return result.get(0);
    }
}
