/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

import DT.Entities.Invitations;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Karolis
 */
@Stateless
public class InvitationsFacade extends GenericFacade<Invitations> {
    
    public InvitationsFacade() {
        super(Invitations.class);
    }
    
    public List findBySender(int senderId) {
        return em.createNamedQuery("Invitations.findBySenderid").setParameter("senderid", senderId)
                .getResultList();
    }
    
    public List findByURLCode(String urlCode) {
        return em.createNamedQuery("Invitations.findByUrlcode").setParameter("urlcode", urlCode)
                .getResultList();
    }
    
}
