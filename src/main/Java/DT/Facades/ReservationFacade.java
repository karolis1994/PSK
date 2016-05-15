/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Houses;
import DT.Entities.Principals;
import DT.Entities.Reservations;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author Henrikas
 */
@Stateless
public class ReservationFacade extends GenericFacade<Reservations> {
    
    public ReservationFacade() {
        super(Reservations.class);
    }
    
    public List<Reservations> findByPrincipal(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    public List<Reservations> findByPrincipalNotCanceled(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p AND o.iscanceled=false");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    public List<Reservations> findByDatesCoveredNotCanceled(Date from, Date to) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE ((o.reservedfrom <= :from AND o.reservedto >= :from) OR (o.reservedfrom <= :to AND o.reservedto >= :to)) "
                + "AND o.iscanceled=false");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }
    
    public List<Reservations> findByDatesCoveredNotCanceledExtraIdNull(Date from, Date to) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE ((o.reservedfrom <= :from AND o.reservedto >= :from) OR (o.reservedfrom <= :to AND o.reservedto >= :to)) "
                + "AND o.iscanceled=false"
                + "AND o.extraid=null");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }
}
