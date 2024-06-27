/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Feedback;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author Syafiq
 */
@Local
public interface FeedbackSessionLocal {
    
    
    public Feedback getFeedback (Long feedbackId) throws NoResultException;

    public void createFeedback(Feedback f, Long eventId);
    
    
}
