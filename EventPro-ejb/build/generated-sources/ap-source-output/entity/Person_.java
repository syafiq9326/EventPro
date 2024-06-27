package entity;

import entity.Event;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-03-29T20:36:10")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, String> password;
    public static volatile ListAttribute<Person, Event> managerEvents;
    public static volatile SingularAttribute<Person, String> name;
    public static volatile SingularAttribute<Person, String> phoneNum;
    public static volatile SingularAttribute<Person, Long> id;
    public static volatile SingularAttribute<Person, String> photoLink;
    public static volatile SingularAttribute<Person, String> email;
    public static volatile SingularAttribute<Person, String> username;

}