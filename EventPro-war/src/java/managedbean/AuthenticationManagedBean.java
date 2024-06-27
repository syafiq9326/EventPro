/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Person;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import session.PersonSessionBeanLocal;

/**
 *
 * @author Syafiq
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private PersonSessionBeanLocal personSessionLocal;
    //will get filled up by inputs in login.xhtml
    private String username = null;
    private String password = null;
    private Long personId = (long) -1;

    private PersonManagedBean personManagedBean;

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {

    }

    public String login(String username, String password) {
        Person p = personSessionLocal.getPersonByUsername(username);
        Person loggedInPerson = personSessionLocal.login(username, password);
        if (p != null && loggedInPerson != null) {
            personId = loggedInPerson.getId();
            //return "/authenticated/authenticated.xhtml?faces-redirect=true";
            return "/authenticated/homepage.xhtml?faces-redirect=true";
        } else {
            //log in fail
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Password", "Wrong Password/Username"));
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "login.xhtml";
        }
    }

    //account for username no exist
    public String login2(String username, String password) {
        try {
            Person p = personSessionLocal.getPersonByUsername(username);
            Person loggedInPerson = personSessionLocal.login(username, password);
            if (p != null && loggedInPerson != null) {
                personId = loggedInPerson.getId();
                //return "/authenticated/authenticated.xhtml?faces-redirect=true";
                return "/authenticated/homepage.xhtml?faces-redirect=true";
            } else {
                //log in fail
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Password", "Wrong Password/Username"));
//            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                return "login.xhtml";
            }
            //username don't exist
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Wrong Username", "Wrong Password/Username"));
            return "login.xhtml";
        }
    }

    //redirect debug
    public String redirectToRegister() {
        return "signup.xhtml?faces-redirect=true";
    }

    public String logout() {
        username = null;
        password = null;
        personId = (long) -1;
        System.out.println("loggin out");
        return "/login.xhtml?faces-redirect=true";
    }

    //getter setter
    public Long getUserId() {
        return personId;
    }

    public void setUserId(Long personId) {
        this.personId = personId;
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

}
