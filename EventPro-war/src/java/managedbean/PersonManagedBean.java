/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Event;
import entity.Person;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.Part;
import session.PersonSessionBeanLocal;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.persistence.NoResultException;

/**
 *
 * @author Syafiq
 */
@Named(value = "personManagedBean")
@ViewScoped
public class PersonManagedBean implements Serializable {

    //viewscoped allows us to retain id even after navigating to different page with anoter button
    @EJB
    private PersonSessionBeanLocal personSessionLocal;

    @Inject
    private AuthenticationManagedBean authenticationManagedBean;

    //person attributes
    private String name;
    private String username;
    private String password;
    private String email;
    private String phoneNum;
    private String photoLink;
    private List<Person> people;
    //need this to display selected person
    //the input will come from the authenticated.xml page
    private Long personId;
    private Person selectedPerson;

    //private List<Event> managerEvents;
    /**
     * Creates a new instance of PersonManagedBean
     */
    public PersonManagedBean() {
    }

    public void addPerson(ActionEvent evt) {
        Person p = new Person();
        p.setName(name);
        p.setUsername(username);
        p.setPassword(password);
        p.setEmail(email);
        p.setPhoneNum(phoneNum);
        p.setPhotoLink(photoLink);
        personSessionLocal.createPerson(p);
    }

    //accounted for duplicate registration
    public void addPerson2(ActionEvent evt) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Attempt to retrieve the person by username
//            Person personExist = personSessionLocal.getPersonByUsername2(username);
            Person personExistEmail = personSessionLocal.getPersonByEmail(email);

            // If personExist is not null, it means the person already exists
            if (personExistEmail != null) {
                // Person already exists, show error message and redirect to signup page
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username/email already exists", "Username already exists"));
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().getExternalContext().redirect("signup.xhtml?faces-redirect=true");
                // Exit method to avoid creating a new person
                return;
            }

        } catch (IOException e) {
            // Handle IOException
            throw e;
        }

        // Proceed with registration
        Person p = new Person();
        p.setName(name);
        p.setUsername(username);
        p.setPassword(password);
        p.setEmail(email);
        p.setPhoneNum(phoneNum);
        p.setPhotoLink(photoLink);
        personSessionLocal.createPerson(p);
        context.addMessage(null, new FacesMessage("Successfully Registered!", "Sucess!"));
        context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect   
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml?faces-redirect=true");
    }

    public void loadSelectedPerson() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.selectedPerson = personSessionLocal.getPerson(personId);
            name = this.selectedPerson.getName();
            username = this.selectedPerson.getUsername();
            email = this.selectedPerson.getEmail();
            phoneNum = this.selectedPerson.getPhoneNum();
            password = this.selectedPerson.getPassword();
            photoLink = this.selectedPerson.getPhotoLink();

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load user"));
        }

    }

    public Person getPersonById(Long personId) {
        return personSessionLocal.getPerson(personId);
    }

    //picture
    private Part uploadedfile;
    private String filename = "";

    public void upload() throws IOException {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        //get the deployment path
        String UPLOAD_DIRECTORY = ctx.getRealPath("/") + "upload/";
        System.out.println("#UPLOAD_DIRECTORY : " + UPLOAD_DIRECTORY);

        //debug purposes
        setFilename(Paths.get(uploadedfile.getSubmittedFileName()).getFileName().toString());
        System.out.println("filename: " + getFilename());
        //---------------------

        //replace existing file
        Path path = Paths.get(UPLOAD_DIRECTORY + getFilename());
        InputStream bytes = uploadedfile.getInputStream();
        Files.copy(bytes, path, StandardCopyOption.REPLACE_EXISTING);
        //addednew line
        //this.photoLink = getFilename();
        System.out.println("b4 update photo");

        selectedPerson.setPhotoLink("../upload/" + getFilename());
        System.out.println("after update photo");

        personSessionLocal.updatePhoto(selectedPerson);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful Picture!", "Sucess!"));
        context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect 
        FacesContext.getCurrentInstance().getExternalContext().redirect("editperson.xhtml?faces-redirect=true");

    }

    public void updatePerson(ActionEvent evt) {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Update the selectedPerson object with non-empty values
            if (name != null && !name.trim().isEmpty()) {
                selectedPerson.setName(name);
            }
            if (username != null && !username.trim().isEmpty()) {
                selectedPerson.setUsername(username);
            }
            if (password != null && !password.trim().isEmpty()) {
                selectedPerson.setPassword(password);
            }
            if (email != null && !email.trim().isEmpty()) {
                selectedPerson.setEmail(email);
            }
            if (phoneNum != null && !phoneNum.trim().isEmpty()) {
                selectedPerson.setPhoneNum(phoneNum);
            }
//
//            if (!photoLink.isEmpty()) {
//                selectedPerson.setPhotoLink(photoLink);
//            }
            // Call the session bean to update the person
            personSessionLocal.updatePerson(selectedPerson);

            // Redirect to the authenticated page with personId parameter
            context.addMessage(null, new FacesMessage("Profile Editted!", "Sucess!"));
            context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect 
            FacesContext.getCurrentInstance().getExternalContext().redirect("editperson.xhtml?faces-redirect=true");
        } catch (Exception e) {
            // Show error message if an exception occurs
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update person"));
        }
    }

    public void updatePassword() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.selectedPerson = personSessionLocal.getPersonByEmail(email);
            // Validate user
            if (selectedPerson != null && phoneNum.equals(selectedPerson.getPhoneNum()) && email.equals(selectedPerson.getEmail())) {
                selectedPerson.setPassword(password);
                personSessionLocal.updatePerson(selectedPerson);
                context.addMessage(null, new FacesMessage("Password Updated!", "Successfully updated password, redirecting to login.."));
                context.getExternalContext().getFlash().setKeepMessages(true); // Keep messages after redirect
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml?faces-redirect=true");

//                // Introduce a delay before redirecting
//                String redirectScript = "setTimeout(function(){ window.location.href = 'login.xhtml?faces-redirect=true'; }, 1500);";
//                FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add(redirectScript);
            } else {
                // Show error message if phone number or email address doesn't match
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong phone number, re-validate!", "Wrong email or phone number, re-validate!"));
            }
        } catch (Exception e) {
            // Show error message if an exception occurs during password update or redirection
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong phone number", "An error occurred: " + e.getMessage()));
        }
    }

    public List<Event> loadEventsByManager() {
        return personSessionLocal.listEventsByManager(personId);
    }

    public String navigateAfterAdd() {
        // Add any additional logic if needed
        return "login.xhtml?faces-redirect=true";
    }

    //getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    //manual setup using authentication bean
    public void setPersonIdAuth() {
        this.personId = authenticationManagedBean.getUserId();
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public Part getUploadedfile() {
        return uploadedfile;
    }

    public void setUploadedfile(Part uploadedfile) {
        this.uploadedfile = uploadedfile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
