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
 * @param <T>
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
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        Query query = em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o");
        return query.getResultList();
    }

    public List<T> findRange(int maxResults, int firstResult) {
        Query q = em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public int getItemCount() {
        return ((Long) em.createQuery("SELECT count(o) FROM " + entityClass.getSimpleName() + " o").getSingleResult()).intValue();
    }
}
