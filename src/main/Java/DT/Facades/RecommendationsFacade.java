/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Invitations;
import DT.Entities.Recommendations;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Karolis
 */
@Stateless
public class RecommendationsFacade extends GenericFacade<Recommendations>{

    public RecommendationsFacade() {
        super(Recommendations.class);
    }
    
    public List findBySender(int senderId) {
        return em.createNamedQuery("Recommendations.findBySenderid").setParameter("senderid", senderId)
                .getResultList();
    }
    
    public List findByURLCode(String urlCode) {
        return em.createNamedQuery("Recommendations.findByUrlcode").setParameter("urlcode", urlCode)
                .getResultList();
    }
    
    public List findByApprovedSender(int receiverId) {
        return findWhere(" o.senderid.id = " + receiverId + " AND o.isactivated = 't'");
    }
}
