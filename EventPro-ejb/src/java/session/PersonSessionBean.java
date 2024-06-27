/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package session;

import entity.Event;
import entity.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class PersonSessionBean implements PersonSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @Override
    public Person getPerson(Long personId) throws NoResultException {
        Person person = em.find(Person.class, personId);
        if (person != null) {
            return person;
        } else {
            throw new NoResultException("Not found");
        }
    }

    @Override
    public Person getPersonByUsername(String username) {
        Query query = em.createQuery("SELECT p FROM Person p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        return (Person) query.getSingleResult();
    }

    @Override
    public Person getPersonByEmail(String email) {
        Query query = em.createQuery("SELECT p FROM Person p WHERE p.email = :inEmail");
        query.setParameter("inEmail", email);
        try {
            return (Person) query.getSingleResult();
        } catch (NoResultException e) {
            // Log or handle the exception as needed
            System.out.println("null person");
            return null;
        }
    }

    @Override
    public void createPerson(Person p) {
        em.persist(p);
    }

    @Override
    public void updatePerson(Person p) throws NoResultException {
        Person oldP = getPerson(p.getId());
        oldP.setName(p.getName());
        oldP.setUsername(p.getUsername());
        oldP.setPassword(p.getPassword());
        oldP.setEmail(p.getEmail());
        oldP.setPhoneNum(p.getPhoneNum());
//        oldP.setPhotoLink(p.getPhotoLink());
    }

    @Override
    public void updatePhoto(Person p) {
        Person oldP = getPerson(p.getId());
        oldP.setPhotoLink(p.getPhotoLink());
    }

    @Override
    public void deleteCustomer(Long personId) throws NoResultException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Person getPersonByUsername2(String username) {
        Query query = em.createQuery("SELECT p FROM Person p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        try {
            return (Person) query.getSingleResult();
        } catch (NoResultException e) {
            // Log or handle the exception as needed
            System.out.println("null person");
            return null;
        }
    }

    @Override
    public Person login2(String username, String password) {
        Person p = getPersonByUsername2(username);
        if (p == null) {
            return null;
        }
        else if (p.getPassword().equals(password)) {
            return p;
        } else {
            return null;
        }
    }

    @Override
    public Person login(String username, String password) {
        Person p = getPersonByUsername2(username);
        if (p.getPassword().equals(password)) {
            return p;
        } else {
            return null;
        }
    }

    @Override
    public List<Event> listEventsByManager(Long personId) {
        Query query = em.createQuery("SELECT e FROM Person p JOIN p.managerEvents e WHERE p.id = :personId");
        query.setParameter("personId", personId);
        return query.getResultList();
    }

}
