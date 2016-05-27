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
import javax.inject.Inject;
import javax.persistence.Query;

/**
 *
 * @author Henrikas
 */
@Stateless
public class ReservationFacade extends GenericFacade<Reservations> {
    
    @Inject PrincipalsFacade principalsFacade;
    
    public ReservationFacade() {
        super(Reservations.class);
    }
    
    /**
     * finds reservations made by given principal
     * @param principal
     * @return 
     */
    public List<Reservations> findByPrincipal(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    /**
     * finds non-canceled reservations made by given principal
     * @param principal
     * @return 
     */
    public List<Reservations> findByPrincipalNotCanceled(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p AND o.iscanceled=false");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    /**
     * finds reservations during holidays and house reservations made by given principal
     * @param principal
     * @return 
     */
    public List<Reservations> findByPrincipalNotCanceledHouseAndHolidayOnly(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p AND o.iscanceled=false "
                + "AND o.extraid IS NULL");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    /**
     * finds non-canceled extras reservations made by given principal   
     * @param principal
     * @return 
     */
    public List<Reservations> findByPrincipalNotCanceledExtraOnly(Principals principal) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE o.principalid=:p AND o.iscanceled=false "
                + "AND o.extraid IS NOT NULL");
        query.setParameter("p", principal);
        return query.getResultList();
    }
    
    /**
     * finds non-canceled reservations that intersect with given time period 
     * @param from 
     * @param to
     * @return 
     */
    public List<Reservations> findByDatesCoveringNotCanceledHouseOnly(Date from, Date to) {
        Query query = em.createQuery(""
                + "SELECT o "
                + "FROM " + entityClass.getSimpleName() + " o "
                + "WHERE ((o.reservedfrom >= :from AND o.reservedfrom <= :to) OR (o.reservedto >= :from AND o.reservedto <= :to)) "
                + "AND o.iscanceled=false "
                + "AND o.extraid IS NULL "
                + "AND o.houseid IS NOT NULL");
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
    
    public void cancelReservation(Payments payment, Principals principal) {
        if (payment.getPaidWithPoints()) {
            int principalPoints = principal.getPoints() - (int) payment.getAmmount();
            principal.setPoints(principalPoints);
            principalsFacade.edit(principal);
        }    
        
        setCanceledByPaymendId(payment, true);
    }
}
