/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package session;

import entity.Event;
import entity.Person;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Syafiq
 */
@Singleton
@LocalBean
@Startup
public class DataInit {

    @PersistenceContext(unitName = "EventPro-ejbPU")
    private EntityManager em;

    @EJB
    private EventSessionLocal eventSessionLocal;

    @EJB
    private PersonSessionBeanLocal personSessionLocal;

    @PostConstruct
    public void postConstruct() {
//        String name, String username, String email, String password, 
//            String phoneNum, String photoLink
        if (em.find(Person.class, 1l) == null) {
            Person person1 = new Person("Syafiq", "syafiq", "syafiq@gmail.com", "password", "93267920", "https://media.licdn.com/dms/image/D5603AQGuJzOhd0aCHQ/profile-displayphoto-shrink_400_400/0/1706714448089?e=1714608000&v=beta&t=Wjr_yiAkmuhpxTSGEq9ryThy_4K4k-3b_-oqVQyiUxE");
            Person person2 = new Person("Justin", "justin", "justin@gmail.com", "password", "999", "https://media.licdn.com/dms/image/D5603AQGM2nk3PaHAvQ/profile-displayphoto-shrink_800_800/0/1709124353093?e=1714608000&v=beta&t=qDB6bTK3GzCJrjHjjYtp6jqQsC49JWuR_t34AbgysnM");
            Person person3 = new Person("Norman", "norman", "norman@gmail.com", "password", "999", "https://media.licdn.com/dms/image/D4D03AQHYSiZH2n6R6w/profile-displayphoto-shrink_800_800/0/1684771607015?e=1714608000&v=beta&t=cGJyJp1_0OfC-8nHhnFAFpAUQOf05ROjgkVEkhH-7pI");
            Person person4 = new Person("Yiphern", "yiphern", "yiphern@gmail.com", "password", "999", "https://media.licdn.com/dms/image/D5603AQGb8GfSGOtCww/profile-displayphoto-shrink_400_400/0/1679716369177?e=2147483647&v=beta&t=KJjxlGBtcGe12Ljpdyxz2TcgP6G7HW7F6Fu_TUbKlYM");
            personSessionLocal.createPerson(person1);
            personSessionLocal.createPerson(person2);
            personSessionLocal.createPerson(person3);
            personSessionLocal.createPerson(person4);
        }

    }

}
