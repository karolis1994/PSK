/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Principals;
import java.util.Calendar;
import java.util.Date;
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
    
    public void extendMembership(Principals principal) {
        // Extend membership
        Date membershipUntill = principal.getMembershipuntill();
        
        if (membershipUntill == null || membershipUntill.before(new Date())) 
        {
            Calendar todayPlusOneYear = Calendar.getInstance();
            todayPlusOneYear.add(Calendar.YEAR, 1);
            membershipUntill = todayPlusOneYear.getTime();
        }
        else
        {
            Calendar newMembershipUntill = Calendar.getInstance();
            newMembershipUntill.setTime(membershipUntill);
            newMembershipUntill.add(Calendar.YEAR, 1);
            membershipUntill = newMembershipUntill.getTime();
        }
        
        principal.setMembershipuntill(membershipUntill);
        edit(principal);
    }
}
