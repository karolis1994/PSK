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
public class PrincipalsFacade {
    
    @PersistenceContext(unitName = "DT_DT.ReservationSystem_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public PrincipalsFacade() {
    }
    
    public void create(Principals principal) {
        em.persist(principal);
    }

    public void edit(Principals principal) {
        em.merge(principal);
    }

    public void remove(Principals principal) {
        em.remove(em.merge(principal));
    }

    public List findByEmail(String email) {
        return em.createNamedQuery("Principals.findByEmail")
                .setParameter("email", email).getResultList();       
    }

    public List findAll() {
        return em.createNamedQuery("Principals.findAll").getResultList();
    }
    
    public List findAllApproved() {
        return em.createNamedQuery("Principals.findByIsapproved")
                .setParameter("isapproved", true).getResultList();
    }
    
}
