/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Event;
import entity.Feedback;
import entity.Person;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Syafiq
 */
@Stateless
public class EventSession implements EventSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Event getEvent(Long eventId) throws NoResultException {
        Event event = em.find(Event.class, eventId);
        if (event != null) {
            return event;
        } else {
            throw new NoResultException("Not found");
        }
    }

    @Override
    public void createEvent(Event e) {
        em.persist(e);
    }

    @Override
    public void managerCreateEvent(Event e, Long personId) {
        Person p = em.find(Person.class, personId);
        p.getManagerEvents().add(e);
        em.persist(e);
    }

    @Override
    public void updateEvent(Event e) throws NoResultException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void markAttendee(Long eventId, Long personId) {
        Event e = em.find(Event.class, eventId);
        Map<Long, Boolean> attendees = e.getAttendees();
        if (attendees.containsKey(personId)) {
            boolean currentStatus = attendees.get(personId); // Get the current status
            attendees.put(personId, !currentStatus); // Invert the status
            em.merge(e); // Persist the changes to the database
        }
    }

    @Override
    public void markAttendeePresent(Long eventId, Long personId) {
        Event e = em.find(Event.class, eventId);
        Map<Long, Boolean> attendees = e.getAttendees();
        if (attendees.containsKey(personId)) {
            attendees.put(personId, Boolean.TRUE); // Update attendance status to true (present)
            em.merge(e); // Persist the changes to the database
        }

    }

    @Override
    public void markAttendeeAbsent(Long eventId, Long personId) {
        Event e = em.find(Event.class, eventId);
        Map<Long, Boolean> attendees = e.getAttendees();
        if (attendees.containsKey(personId)) {
            attendees.put(personId, Boolean.FALSE); // Update attendance status to true (present)
            em.merge(e); // Persist the changes to the database
        }
    }

    @Override
    public void deleteEvent(Long eventId, Long personId) throws NoResultException {
        Event e = em.find(Event.class, eventId);
        //remove all attendes from the list
        e.getAttendees().clear();
        //remove from the manager's event list
        Person manager = em.find(Person.class, personId);
        if (manager.getManagerEvents() != null) {
            manager.getManagerEvents().remove(e);
        }

        em.remove(e);
    }

    @Override
    public List<Event> getAllEvents() {
        Query query = em.createQuery("SELECT e FROM Event e");
        return query.getResultList();
    }

    //events before deadline 
    @Override
    public List<Event> getCurrentEvents() {
        Date currentDate = new Date();
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.deadline >= :currentDate");
        query.setParameter("currentDate", currentDate);
        System.out.println(currentDate);
        return query.getResultList();
    }
//

    @Override
    public List<Event> getCurrentEvents2(long personId) {
        Date currentDate = new Date();
//        Query query = em.createQuery("SELECT e FROM Event e WHERE e.deadline >= :currentDate AND :personId NOT MEMBER OF e.attendees.keySet()");
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.deadline >= :currentDate");
        query.setParameter("currentDate", currentDate);
        List<Event> eventsBeforeDeadline = query.getResultList();

        //only get events where user have not registered yet and before deadline of course
        List<Event> filteredEvents = eventsBeforeDeadline.stream()
                .filter(event -> !event.getAttendees().containsKey(personId))
                .collect(Collectors.toList());

        return filteredEvents;
    }

    //events whose deadline are over already
    @Override
    public List<Event> getPastEvents() {
        Date currentDate = new Date();
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.deadline < :currentDate");
        query.setParameter("currentDate", currentDate);
        return query.getResultList();
    }

    //registered events whose date are over already
    @Override
    //for attendes, initially all attendes assumed to not have attended yet until marked as present
    public void registerForEvent(Long eventId, Long personId) {
        Event e = em.find(Event.class, eventId);
        e.getAttendees().put(personId, false);
        em.merge(e);
    }

    @Override
    public void unregister(Long eventId, Long personId) {
        Event e = em.find(Event.class, eventId);
        e.getAttendees().remove(personId);
        em.merge(e);
    }

    @Override
    //get the map out
    public Map<Long, Boolean> getAllAttendes(Long eventId) {
        Event e = em.find(Event.class, eventId);
        return e.getAttendees();
    }

    @Override
    public List<Event> getRegisteredEvents(Long personId) {
        List<Event> registeredEvents = new ArrayList<>();
        for (Event event : getAllEvents()) {
            // Check if the event contains the person ID in its attendees map
            if (event.getAttendees().containsKey(personId)) {
                // Add the event to the list of registered events
                registeredEvents.add(event);
            }
        }

        return registeredEvents;
    }

    @Override
    public List<Event> getPastRegisteredEvents(Long personId) {
        Date currentDate = new Date();
        List<Event> pastregisteredEvents = new ArrayList<>();
        for (Event event : getAllEvents()) {
            // Check if the event contains the person ID in its attendees map
            if (event.getAttendees().containsKey(personId) && event.getDate().before(currentDate)) {
                // Add the event to the list of registered events
                pastregisteredEvents.add(event);
            }
        }

        return pastregisteredEvents;
    }

    @Override
    public List<Event> searchEventByTitle(String title) {
        Date currentDate = new Date();
        if (title == null) {
            // If the title is null or empty, return all events
            return getCurrentEvents();
        } else {
            // Otherwise, perform a partial search using the LIKE operator
            Query query = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.title) LIKE :searchTitle AND e.deadline >= :currentDate");
            query.setParameter("searchTitle", "%" + title.toLowerCase() + "%");
            query.setParameter("currentDate", currentDate);
            List<Event> events = query.getResultList();
            System.out.println(title);

            return events;
        }
    }

    @Override
    public List<Event> searchEventByTitle2(String title, long personId) {
        Date currentDate = new Date();
        if (title == null) {
            // If the title is null or empty, return all events
            return getCurrentEvents2(personId);
        } else {
            // Otherwise, perform a partial search using the LIKE operator
            Query query = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.title) LIKE :searchTitle AND e.deadline >= :currentDate");
            query.setParameter("searchTitle", "%" + title.toLowerCase() + "%");
            query.setParameter("currentDate", currentDate);
            List<Event> events = query.getResultList();

            //only get events where user have not registered yet and before deadline of course
            List<Event> filteredEvents = events.stream()
                    .filter(event -> !event.getAttendees().containsKey(personId))
                    .collect(Collectors.toList());

            System.out.println(title);

            return filteredEvents;
        }
    }

    @Override
    public List<Event> searchPastEvents(String title) {
        Date currentDate = new Date();
        if (title == null) {
            // If the title is null or empty, return all events
            System.out.println("hi");
            return getPastEvents();
        } else {
            // Otherwise, perform a partial search using the LIKE operator
            Query query = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.title) LIKE :searchTitle AND e.deadline < :currentDate");
            query.setParameter("searchTitle", "%" + title.toLowerCase() + "%");
            query.setParameter("currentDate", currentDate);
            List<Event> events = query.getResultList();

            return events;
        }
    }

    @Override
    public List<Event> searchPastRegisteredEvents(String title, Long personId) {
        Date currentDate = new Date();
        if (title == null) {
            // If the title is null or empty, return all events
            System.out.println("hi");
            return getPastRegisteredEvents(personId);
        } else {
            // Otherwise, perform a partial search using the LIKE operator
            Query query = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.title) LIKE :searchTitle AND e.date < :currentDate");
            query.setParameter("searchTitle", "%" + title.toLowerCase() + "%");
            query.setParameter("currentDate", currentDate);
            List<Event> events = query.getResultList();

            return events;
        }
    }

    @Override
    public List<Feedback> getFeedbacks(Long eventId) {
        Event e = em.find(Event.class, eventId);
        return e.getFeedbacks();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
