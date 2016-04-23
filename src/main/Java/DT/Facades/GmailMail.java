/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DT.Facades;

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
public class GmailMail implements Serializable{

    private String to;
    private static final String FROM = "psk.labanoras@gmail.com";
    private String message;
    private static final String SUBJECT ="Club membership request";
    private static final String SMTPSERV = "smtp.gmail.com";


    public int sendMail(String to, String message) {
        this.to = to;
        this.message = message;
        try {
            Properties props = System.getProperties();
            // -- Attaching to default Session, or we could start a new one --
            props.put("mail.transport.protocol", "smtp" );
            props.put("mail.smtp.starttls.enable", "true" );
            props.put("mail.smtp.host", SMTPSERV);
            props.put("mail.smtp.auth", "true" );
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress(FROM));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(SUBJECT);
            msg.setText(message);
            // -- Set some other header information --
            msg.setHeader("MyMail", "Mr. XYZ" );
            msg.setSentDate(new Date());
            // -- Send the message --
            Transport.send(msg);
            System.out.println("Message sent to"+to+" OK." );
            return 0;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Exception "+ex);
            return -1;
        }
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username =  "psk.labanoras";           // specify your email id here (sender's email id)
            String password = "labanoras";                                      // specify your password here
            return new PasswordAuthentication(username, password);
        }
    }
    
}
