/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Event;
import entity.Feedback;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author Syafiq
 */
@Local
public interface EventSessionLocal {

    public Event getEvent(Long eventId) throws NoResultException;

    public void createEvent(Event e);

    public void updateEvent(Event e) throws NoResultException;

    public void deleteEvent(Long eventId, Long personId) throws NoResultException;

    public void managerCreateEvent(Event e, Long personId);

    public void registerForEvent(Long eventId, Long personId);

    public List<Event> getAllEvents();

    public Map<Long, Boolean> getAllAttendes(Long eventId);

    public List<Event> getRegisteredEvents(Long personId);

    public void markAttendee(Long eventId, Long personId);

    public void markAttendeePresent(Long eventId, Long personId);

    public void markAttendeeAbsent(Long eventId, Long personId);

    public List<Event> searchEventByTitle(String title);

    public void unregister(Long eventId, Long personId);

    public List<Event> getCurrentEvents();

    public List<Event> getPastEvents();

    public List<Event> searchPastEvents(String title);

    public List<Feedback> getFeedbacks(Long eventId);

    public List<Event> getPastRegisteredEvents(Long personId);

    public List<Event> searchPastRegisteredEvents(String title, Long personId);

    public List<Event> searchEventByTitle2(String title, long personId);

    public List<Event> getCurrentEvents2(long personId);
    
}
