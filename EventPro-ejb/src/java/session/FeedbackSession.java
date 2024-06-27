/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Event;
import entity.Feedback;
import entity.Person;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Syafiq
 */
@Stateless
public class FeedbackSession implements FeedbackSessionLocal {
    
    
    @PersistenceContext
    private EntityManager em;


    @Override
    public Feedback getFeedback(Long feedbackId) throws NoResultException {
        Feedback f = em.find(Feedback.class, feedbackId);
        if (f != null) {
            return f;
        } else {
            throw new NoResultException("Not found");
        }
    }

    @Override
    public void createFeedback(Feedback f, Long eventId) {
        Event e = em.find(Event.class, eventId);
        e.getFeedbacks().add(f);
        em.persist(f);
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
