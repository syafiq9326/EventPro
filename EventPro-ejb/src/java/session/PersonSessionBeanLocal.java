/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Event;
import entity.Person;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 *
 * @author Syafiq
 */
@Local
public interface PersonSessionBeanLocal {

    public Person getPerson(Long personId) throws NoResultException;

    public void createPerson(Person p);

    public void updatePerson(Person p) throws NoResultException;

    public void deleteCustomer(Long personId) throws NoResultException;

    public Person getPersonByUsername(String username);

    public Person login(String username, String password);

    public List<Event> listEventsByManager(Long personId);

    public void updatePhoto(Person p);

    public Person getPersonByUsername2(String username);

    public Person getPersonByEmail(String email);

    public Person login2(String username, String password);

}
