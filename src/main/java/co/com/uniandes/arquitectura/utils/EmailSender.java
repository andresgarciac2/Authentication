package co.com.uniandes.arquitectura.utils;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EmailSender {

	static final String user = "sube.uniandes@gmail.com";
	static final String pass = "manage2017";
	
	
	public static void main (String [] args){
		sendEmail("jaherlo19@gmail.com", "12345");
	}
    
    public static void sendEmail(String receiver, String passText){
    	Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", true); // added this line
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", true);
        
        Session session = Session.getInstance(props,null);
        MimeMessage message = new MimeMessage(session);

        System.out.println("Port: "+session.getProperty("mail.smtp.port"));
        
     // Create the email addresses involved
        try {
            InternetAddress from = new InternetAddress("username");
            message.setSubject("Recuperación de contraseña");
            message.setFrom(from);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));

            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart("alternative");

            // Create the html part
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlMessage = "Se ha generado la siguiente contraseña temporal en el sistema SUBE: " + passText + ". \n"
            					+ "Por favor actualice su contraseña la próxima vez que ingrese en el sistema.";
            messageBodyPart.setContent(htmlMessage, "text/html");

            // Add html part to multi part
            multipart.addBodyPart(messageBodyPart);

            // Associate multi-part with message
            message.setContent(multipart);

            // Send message
            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", user, pass);
            System.out.println("Transport: "+transport.toString());
            transport.sendMessage(message, message.getAllRecipients());

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
	
}
