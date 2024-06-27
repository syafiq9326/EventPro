/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Event;
import entity.Feedback;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.faces.event.ActionEvent;
import session.EventSessionLocal;
import javax.faces.application.FacesMessage;


/**
 *
 * @author Syafiq
 */
@Named(value = "eventManagedBean")
@ViewScoped
public class EventManagedBean implements Serializable {

    //viewscoped allows us to retain id even after navigating to different page with anoter button
    @EJB
    private EventSessionLocal eventSessionLocal;

    private String title;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Temporal(TemporalType.DATE)
    private Date deadline;

    private String location;
    private String description;

    private Long eventId;

    private Event selectedEvent;

    private String searchType = "TITLE";
    private String searchString;

    private List<Event> events;

    private List<String> allFields = Arrays.asList("TITLE", "date", "deadline", "location");

//    private PieChartModel pieChartModel;
//
//    private DonutChartModel donutModel;

    private Map<String, Integer> chartData;

    private String jsonChartData;

    private String donutData;

    public EventManagedBean() {
    }

    public void addEvent(ActionEvent evt) {
        Event e = new Event();
        e.setTitle(title);
        e.setDate(date);
        e.setDeadline(deadline);
        e.setLocation(location);
        e.setDescription(description);
        eventSessionLocal.createEvent(e);
    }

    public void managerMakeEvent(ActionEvent evt, Long personId) {
        FacesContext context = FacesContext.getCurrentInstance();
        Event e = new Event();
        e.setTitle(title);
        e.setDate(date);
        e.setDeadline(deadline);
        e.setLocation(location);
        e.setDescription(description);

        try {
            eventSessionLocal.managerCreateEvent(e, personId);
            context.addMessage(null, new FacesMessage("Successfully created event!", "Sucess!"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("eventmanager.xhtml?faces-redirect=true");
        } catch (Exception exception) {
            //show with an error icon 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update customer"));
        }
    }

    public void managerMakeEvent2(ActionEvent evt, Long personId) {
        FacesContext context = FacesContext.getCurrentInstance();
        Date currentDate = new Date();

        //means before current date and deadline so not possible create event
        //deadline should be before event
        
        if (deadline.compareTo(date) >0) {
            context.addMessage(null, new FacesMessage("Error:Deadline must before", "Error!"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
        } 
        else if (date.compareTo(currentDate) < 0 || deadline.compareTo(currentDate) < 0 ) {
            context.addMessage(null, new FacesMessage("Error:Expired date", "Error!"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
        } else {
            Event e = new Event();
            e.setTitle(title);
            e.setDate(date);
            e.setDeadline(deadline);
            e.setLocation(location);
            e.setDescription(description);

            try {
                eventSessionLocal.managerCreateEvent(e, personId);
                context.addMessage(null, new FacesMessage("Successfully created event!", "Sucess!"));
                context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
                FacesContext.getCurrentInstance().getExternalContext().redirect("eventmanager.xhtml?faces-redirect=true");
            } catch (Exception exception) {
                //show with an error icon 
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update customer"));
            }
        }

    }

    public List<Event> getAllEvents() {
        return eventSessionLocal.getAllEvents();
    }

    public List<Event> getCurrentEvents() {
        return eventSessionLocal.getCurrentEvents();
    }

    public void registerForEvent(Long eId, Long personId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            eventSessionLocal.registerForEvent(eId, personId);
            context.addMessage(null, new FacesMessage("Successfully Registered!", "Sucess!"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("registeredeventslist.xhtml?faces-redirect=true");
        } catch (Exception exception) {
            //show with an error icon 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to remove from registration"));
        }
    }

    public void unregister(Long eId, Long personId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            eventSessionLocal.unregister(eId, personId);
            context.addMessage(null, new FacesMessage("Success unregistering", "Welcome to your registered events"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("registeredeventslist.xhtml?faces-redirect=true");
        } catch (Exception exception) {
            //show with an error icon 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to remove from registration"));
        }
    }

    public List<Event> getRegisteredEvents(Long personId) {
        return eventSessionLocal.getRegisteredEvents(personId);
    }
//

    public Map<Long, Boolean> getAllAttendees() {
        return eventSessionLocal.getAllAttendes(eventId);
    }

    public void loadSelectedEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.selectedEvent = eventSessionLocal.getEvent(eventId);
            title = this.selectedEvent.getTitle();
            //getAllAttendees();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load event"));
        }

    }

    //sessionId refers to the user that is logged in to help us with redirection later
    //cancel last on this , we no longer even using sessionId, use auth bean instead!
    public void markAttendeePresent(Long selectedEventId, Long personId, Long sessionId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //System.out.println("selectedEventId");
            eventSessionLocal.markAttendee(selectedEventId, personId);
            context.addMessage(null, new FacesMessage("Attendance Updated", "Successfully mark present"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect            
            FacesContext.getCurrentInstance().getExternalContext().redirect("eventdetails.xhtml?faces-redirect=true&eventId=" + selectedEventId);
        } catch (Exception e) {
            //show with an error icon 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to mark as present"));
            return;
        }
    }

    //addded a get false 
    public List<Map.Entry<Long, Boolean>> getFalseAttendees() {
        List<Map.Entry<Long, Boolean>> falseAttendees = new ArrayList<>();
        Map<Long, Boolean> allAttendees = getAllAttendees();
        for (Map.Entry<Long, Boolean> entry : allAttendees.entrySet()) {
            if (!entry.getValue()) {
                falseAttendees.add(entry);
            }
        }
        return falseAttendees;
    }

    //addded a get true 
    public List<Map.Entry<Long, Boolean>> getTrueAttendees() {
        List<Map.Entry<Long, Boolean>> trueAttendees = new ArrayList<>();
        Map<Long, Boolean> allAttendees = getAllAttendees();
        for (Map.Entry<Long, Boolean> entry : allAttendees.entrySet()) {
            if (entry.getValue()) {
                trueAttendees.add(entry);
            }
        }
        return trueAttendees;
    }

    public void redirectToFeedback(Long selectedEventId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //eventSessionLocal.markAttendeeAbsent(selectedEventId, personId);
            //context.addMessage(null, new FacesMessage("Success", "Successfully mark absent"));
            FacesContext.getCurrentInstance().getExternalContext().redirect("feedback.xhtml?faces-redirect=true&eventId=" + selectedEventId);

        } catch (Exception e) {
            //show with an error icon 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to mark as absent"));
            return;
        }

    }

    public List<Feedback> loadFeedbacks() {
        return eventSessionLocal.getFeedbacks(eventId);
    }

    public List<Event> searchEventByTitle() {
        //searchString will always be updated accordingly
        return eventSessionLocal.searchEventByTitle(searchString);
    }

    public List<Event> searchEventByTitle2(Long personId) {
        //searchString will always be updated accordingly
        return eventSessionLocal.searchEventByTitle2(searchString, personId);
    }

    public List<Event> searchPastEvents() {
        //searchString will always be updated accordingly
        return eventSessionLocal.searchPastEvents(searchString);
    }

    public List<Event> searchPastRegisteredEvents(Long personId) {
        //searchString will always be updated accordingly
        return eventSessionLocal.searchPastRegisteredEvents(searchString, personId);
    }

    public void deleteEvent(Long eventId, Long personId) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            eventSessionLocal.deleteEvent(eventId, personId);

            context.addMessage(null, new FacesMessage("Successfully Deleted!", "Sucess!"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect            

            FacesContext.getCurrentInstance().getExternalContext().redirect("eventmanager.xhtml?faces-redirect=true");

        } catch (Exception e) {
            //show with an error icon 
            System.out.println("error");
            System.out.println(personId);

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to delete"));
            return;
        }
    }

//    public void createPieModel() {
//        PieChartModel pieModel = new PieChartModel();
//
//        Map<Long, Boolean> attendanceMap = eventSessionLocal.getAllAttendes(eventId);
//        int attendedCount = 0;
//        int registeredCount = attendanceMap.size();
//        // Counting attended and registered users
//        for (Boolean attended : attendanceMap.values()) {
//            if (attended) {
//                attendedCount++;
//            }
//        }
//
//        int absentCount = registeredCount - attendedCount;
//
//        pieModel.set("Attended", attendedCount);
//        pieModel.set("Not Attended", absentCount);
//        pieModel.setTitle("Attendance Statistics");
//        pieModel.setLegendPosition("w");
//        pieModel.setShowDataLabels(true); // Show data labels on the chart
//        //pieModel.setSeriesColors("008000, FF0000"); // green for attended, red for not attended
//        pieChartModel = pieModel; // Update the pie chart model
//    }

    public double percentageAttend() {
        Map<Long, Boolean> attendanceMap = eventSessionLocal.getAllAttendes(eventId);
        int attendedCount = 0;
        int registeredCount = attendanceMap.size();
        // Counting attended and registered users
        for (Boolean attended : attendanceMap.values()) {
            if (attended) {
                attendedCount++;
            }
        }

        if (attendedCount > 0) {
            double percentage = (attendedCount / registeredCount) * 100;
//            DecimalFormat df = new DecimalFormat("#.##");
            return (double) attendedCount / registeredCount * 100;
        } else {
            return 0;
        }

    }

    public int attendedCount() {
        Map<Long, Boolean> attendanceMap = eventSessionLocal.getAllAttendes(eventId);
        int attendedCount = 0;
        // Counting attended and registered users
        for (Boolean attended : attendanceMap.values()) {
            if (attended) {
                attendedCount++;
            }
        }
        return attendedCount;
    }

    public int eventSize() {
        Map<Long, Boolean> attendanceMap = eventSessionLocal.getAllAttendes(eventId);
        return attendanceMap.size();
    }
//
//    // Getter for pieChartModel
//    public PieChartModel getPieChartModel() {
//        if (pieChartModel == null) {
//            // Initialize the pie chart model with sample data when accessed for the first time
//            createPieModel(); // Call the method to generate the pie chart model
//        }
//        return pieChartModel;
//    }
//
//    // Setter for pieChartModel
//    public void setPieChartModel(PieChartModel pieChartModel) {
//        this.pieChartModel = pieChartModel;
//    }

    //chart.js version
    public void createChartData() {
        Map<String, Integer> chartData = new HashMap<>();

        Map<Long, Boolean> attendanceMap = eventSessionLocal.getAllAttendes(eventId);
        int attendedCount = 0;
        int registeredCount = attendanceMap.size();
        // Counting attended and registered users
        for (Boolean attended : attendanceMap.values()) {
            if (attended) {
                attendedCount++;
            }
        }

        int absentCount = registeredCount - attendedCount;

        chartData.put("Attended", attendedCount);
        chartData.put("Not Attended", absentCount);

        // Set the chart data in your session bean
        this.chartData = chartData;
    }

// Getter for chartData
    public Map<String, Integer> getChartData() {
        if (chartData == null) {
            // Initialize the chart data with sample data when accessed for the first time
            createChartData(); // Call the method to generate the chart data
        }
        return chartData;

    }

    public String getJsonChartData() {
        if (jsonChartData == null) {
            createChartData();
//            "Attended: + 3 + Not Attended +2) 
            setJsonChartData("{\"Attended\":" + chartData.get("Attended") + ", \"Not Attended\":" + chartData.get("Not Attended") + "}");
        }
        return jsonChartData;
    }

    public void setJsonChartData(String jsonChartData) {
        this.jsonChartData = jsonChartData;
    }

    public Double averageRating() {
        double qty = selectedEvent.getFeedbacks().size();
        double sumRating = 0;
        for (Feedback f : selectedEvent.getFeedbacks()) {
            sumRating += f.getRating();
        }

        if (sumRating > 0) {
            double averageRating = sumRating / qty;
            DecimalFormat df = new DecimalFormat("#.##");
            averageRating = Double.parseDouble(df.format(averageRating));
            return averageRating;
        } else {
            return 0.0;
        }

    }

    public String getDonutData() {
        double averageRating = averageRating();
        double remaining = 5 - averageRating; // maximum rating is 5
        this.donutData = String.format("[%.2f, %.2f]", averageRating, remaining);
        return donutData;
    }

    public void setDonutData(String donutData) {
        this.donutData = donutData;
    }
//
//    public void createDonutModel() {
//
//        double averageRating = averageRating();
//        double remaining = 5 - averageRating; // maximum rating is 5
//        donutModel = new DonutChartModel();
//        Map<String, Number> circle1 = new HashMap<>();
//        circle1.put("Average Rating", averageRating);
//        circle1.put("Remaining", remaining);
//
//        // Show data labels
//        //donutModel.setShowDataLabels(true);
//        donutModel.addCircle(circle1);
//
//        // Add annotation for displaying the rating in the center
//        donutModel.setTitle("Rating: " + String.format("%.1f", averageRating) + "/5");
//
//    }
//
//    public DonutChartModel getDonutModel() {
//        if (donutModel == null) {
//            createDonutModel();
//
//        }
//        return donutModel;
//    }
//
//    public void setDonutModel(DonutChartModel donutModel) {
//        this.donutModel = donutModel;
//    }

    //---- getter setters ------
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<String> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<String> allFields) {
        this.allFields = allFields;
    }

}
