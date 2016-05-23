/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Payments;
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
    
    public List<Reservations> findByPrincipalNotCanceledExtraIdNull(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p AND o.iscanceled=false "
                + "AND o.extraid IS NULL");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    public List<Reservations> findByPrincipalNotCanceledExtraIdNotNull(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p AND o.iscanceled=false "
                + "AND o.extraid IS NOT NULL");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    public List<Reservations> findByDatesCoveredNotCanceledExtraIdNull(Date from, Date to) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE ((o.reservedfrom <= :from AND o.reservedto >= :from) OR (o.reservedfrom <= :to AND o.reservedto >= :to)) "
                + "AND o.iscanceled=false "
                + "AND o.extraid IS NULL");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }
    
    public List<Reservations> findByDatesCoveringNotCanceledExtraIdNull(Date from, Date to) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE ((o.reservedfrom >= :from AND o.reservedfrom <= :to) OR (o.reservedto >= :from AND o.reservedto <= :to)) "
                + "AND o.iscanceled=false "
                + "AND o.extraid IS NULL");
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }
    
    public int setCanceledByPaymendId(Payments paymenntid, boolean state) {
        Query query = em.createQuery(""
                + "UPDATE  " + entityClass.getSimpleName() + " o "
                + "SET o.iscanceled =:state "
                + "WHERE o.paymentid =:paymentid");
        query.setParameter("state", state);
        query.setParameter("paymentid", paymenntid);
        return query.executeUpdate();
    }
}
