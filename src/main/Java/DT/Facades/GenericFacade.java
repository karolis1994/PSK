/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

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
    protected EntityManager em;

    protected Class<T> entityClass;
    
    public GenericFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public void create(T entity) {
        em.persist(entity);
    }

    public void edit(T entity) {
        em.merge(entity);
    }

    public void remove(T entity) {
        em.remove(em.merge(entity));
    }

    public T find(Object id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        Query query = em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o");
        return query.getResultList();
    }
    
    public List<T> findAllNotDeleted() {
        
        String queryString = String.format(
            "SELECT o FROM %s o WHERE o.isdeleted = FALSE", // constant sql, not a subject to injection
            entityClass.getSimpleName());
        Query query = em.createQuery(queryString);
        
        return query.getResultList();
    }

    public List<T> findRange(int maxResults, int firstResult) {
        Query q = em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o");
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
        
    public List<T> findByName(String name) {
        return em.createNamedQuery("Houses.findByTitle").setParameter("title", name).getResultList();
    }

    public int getItemCount() {
        return ((Long) em.createQuery("SELECT count(o) FROM " + entityClass.getSimpleName() + " o").getSingleResult()).intValue();
    }
}
