/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Services;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/**
 *
 * @author Karolis
 */
@Stateless
public class MailSMTP implements IMail, Serializable{

    // Fields ------------------------------------------------------------------
    
    private static final String FROM = "psk.labanoras@gmail.com";
    private static final String SMTPSERV = "smtp.gmail.com";

    // Methods -----------------------------------------------------------------
    
    public int sendMail(String recipient, String subject, String message) {
        try {
            Properties props = System.getProperties();
            //Attaching to default Session, or we could start a new one
            props.put("mail.transport.protocol", "smtp" );
            props.put("mail.smtp.starttls.enable", "true" );
            props.put("mail.smtp.host", SMTPSERV);
            props.put("mail.smtp.auth", "true" );
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            //Create a new message
            Message msg = new MimeMessage(session);
            //Set the FROM and TO fields
            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));
            msg.setSubject(subject);
            msg.setText(message);
            //Set some other header information
            msg.setHeader("Labanoro Draugai mail", "Klubas Labanoro Draugai" );
            msg.setSentDate(new Date());
            //Send the message
            Transport.send(msg);
            return 0;
        }
        catch (Exception ex)
        {
            return -1;
        }
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username =  "psk.labanoras";
            String password = "labanoras";
            return new PasswordAuthentication(username, password);
        }
    }
    
}
