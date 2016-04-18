/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Principals;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Karolis
 */
@Stateless
public class PrincipalsFacade extends GenericFacade<Principals>{

    public PrincipalsFacade() {
        super(Principals.class);
    }

    public List findByEmail(String email) {
        return em.createNamedQuery("Principals.findByEmail")
                .setParameter("email", email).getResultList();       
    }

    @Override
    public List findAll() {
        return em.createNamedQuery("Principals.findAll").getResultList();
    }
    
    public List findAllApproved() {
        return em.createNamedQuery("Principals.findByIsapproved")
                .setParameter("isapproved", true).getResultList();
    }
    
}
