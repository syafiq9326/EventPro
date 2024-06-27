/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Feedback;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import session.FeedbackSessionLocal;

/**
 *
 * @author Syafiq
 */
@Named(value = "feedbackManagedBean")
@ViewScoped
public class FeedbackManagedBean implements Serializable {

    @EJB
    private FeedbackSessionLocal feedbackSessionLocal;
    private Long feedbackId;
    private String description;
    private Double rating;
    private Feedback selectedFeedback;

    /**
     * Creates a new instance of FeedbackManagedBean
     */
    public FeedbackManagedBean() {
    }

    public void addFeedback(ActionEvent evt, Long eventId) {
        FacesContext context = FacesContext.getCurrentInstance();
        Feedback f = new Feedback();
        f.setDescription(description);
        f.setRating(rating);
        try {
            feedbackSessionLocal.createFeedback(f, eventId);
            context.addMessage(null, new FacesMessage("Added!", "Sucess!"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("feedback.xhtml?faces-redirect=true&eventId=" + eventId);
        } catch (Exception exception) {
            //show with an error icon 
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update customer"));
        }
    }

    public void loadSelectedFeedback() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.selectedFeedback = feedbackSessionLocal.getFeedback(feedbackId);
            description = this.selectedFeedback.getDescription();
            rating = this.selectedFeedback.getRating();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load feedback"));
        }

    }

    //getter and setter
    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    
    
    
       

}
