/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Houses;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Henrikas
 */
public abstract class GenericFacade<T> implements Serializable {
    @PersistenceContext(unitName = "DT_DT.ReservationSystem_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    protected Class<T> entityClass;
    
    public GenericFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public void create(Houses house) {
        em.persist(house);
    }

    public void edit(Houses house) {
        em.merge(house);
    }

    public void remove(Houses house) {
        em.remove(em.merge(house));
    }

    public T find(Object id) {
        T obj = em.find(entityClass, id);
        return obj;
    }

    public List<T> findAll() {
        return em.createQuery("select object(o) from Houses as o").getResultList();
    }

    public List<T> findRange(int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Houses as o");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public int getItemCount() {
        return ((Long) em.createQuery("select count(o) from Houses as o").getSingleResult()).intValue();
    }
}
