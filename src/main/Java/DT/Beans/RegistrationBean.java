/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Beans;

import DT.Entities.Principals;
import DT.Facades.PrincipalsFacade;
import java.io.Serializable; 
import javax.ejb.EJB;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
  
import javax.faces.bean.ManagedBean;  
import javax.faces.bean.RequestScoped;  
import javax.naming.Context;  
import javax.naming.InitialContext;  
import javax.naming.NamingException;  
import javax.sql.DataSource;


/**
 *
 * @author Karolis
 */
@ManagedBean(name = "registrationBean")
@RequestScoped
public class RegistrationBean implements Serializable{
    
    @Pattern(regexp="[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
    private String email;
    @Size(min=2,max=5)
    private String firstName;
    @Size(min=2,max=5)
    private String lastName;     
    private String password;
    private String repeatPassword;
    
    Principals principal;
    
    @EJB
    PrincipalsFacade registrationFacade;
    
    DataSource ds;
    
    public RegistrationBean() {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("jdbc\\:postgresql\\://localhost\\:5432/PSK");
        } catch(NamingException e) {
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
    
    public String register() {
        if(registrationFacade.findByEmail(email).isEmpty()) {
            if(principal == null) {
                principal = new Principals(1, email, firstName, lastName, Integer.toString(password.hashCode()));
            } else {
                registrationFacade.create(principal);
            }
            return "registrationSuccesful";
        } else {
            return "registrationUnsuccesful";
        }
    }  
    
}
