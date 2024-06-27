package entity;

import entity.Feedback;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2024-03-29T20:36:10")
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile SingularAttribute<Event, Date> date;
    public static volatile MapAttribute<Event, Long, Boolean> attendees;
    public static volatile SingularAttribute<Event, String> description;
    public static volatile SingularAttribute<Event, String> photo;
    public static volatile ListAttribute<Event, Feedback> feedbacks;
    public static volatile SingularAttribute<Event, String> location;
    public static volatile SingularAttribute<Event, Long> id;
    public static volatile SingularAttribute<Event, String> title;
    public static volatile SingularAttribute<Event, Date> deadline;

}