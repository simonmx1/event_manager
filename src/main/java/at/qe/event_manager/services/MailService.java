package at.qe.event_manager.services;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import at.qe.event_manager.model.User;

public class MailService {
	
	private static final String SRC_MAIL_ADDR = "event.manager.g7t0@gmail.com";
	private static final String PASSWORD = "g7t0passwd";
	private static final Properties PROPERTIES = new Properties();
	private static final Session SESSION;
	
	private MailService() {}
	
	static {
		PROPERTIES.put("mail.smtp.auth", "true");
		PROPERTIES.put("mail.smtp.starttls.enable", "true");
		PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
		PROPERTIES.put("mail.smtp.port", "587");
		
		SESSION = Session.getInstance(PROPERTIES, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SRC_MAIL_ADDR, PASSWORD);
			}
		});
	}
	
	public static Message prepareMessage(String DST_MAIL_ADDR) throws MessagingException {
		Message msg = new MimeMessage(SESSION);
		msg.setFrom(new InternetAddress(DST_MAIL_ADDR));
		return msg;
	}
	
	private static void sendMessage(Message msg) {
		new ThreadMailService(msg).start();
	}
	
	private static class ThreadMailService extends Thread{
		Message msg;
		
		public ThreadMailService(Message msg) {
			this.msg = msg;
		}
		
		@Override
		public void run() {
			try {
				// We set our own email address here, so that we don't
				// send any emails to third-party mail addresses.
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress("event.manager.g7t0@gmail.com"));
				Transport.send(msg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void sendUserRegisterMessage(User user){
		Message msg = null;
		try {
			msg = prepareMessage(user.getEmail());
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject("Event Manager: New User Registration");
			
			Multipart multipart = new MimeMultipart();
			BodyPart msgBodyPart = new MimeBodyPart();
			msgBodyPart.setText("Hello " + user.getFirstName() + " " + user.getLastName() +
					"!\n\n" + "You just created a User Account with the username \"" + user.getUsername()
							+ "\" on \"Event Manager\" and used this Email for Registration!\n\n"
							+ "If not, please contact us!\n\nYour Event Manager Team!");
			multipart.addBodyPart(msgBodyPart);
			msg.setContent(multipart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sendMessage(msg);
	}
	
	public static void sendUserDeleteMessage(User user){
		Message msg = null;
		try {
			msg = prepareMessage(user.getEmail());
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject("Event Manager: Your User has been deleted");
			
			Multipart multipart = new MimeMultipart();
			BodyPart msgBodyPart = new MimeBodyPart();
			msgBodyPart.setText("Hello " + user.getFirstName() + " " + user.getLastName()
							+ "!\n\n" + "Your User Account with the username \"" + user.getUsername()
							+ "\" on \"Event Manager\" has been deleted!\n\n"
							+ "If this was a mistake, please contact us!\n\nYour Event Manager Team!");
			multipart.addBodyPart(msgBodyPart);
			msg.setContent(multipart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sendMessage(msg);
	}
}
