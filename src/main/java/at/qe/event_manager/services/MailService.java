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
import org.springframework.stereotype.Service;
import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.User;

@Service
public class MailService {
	
	private static final String SRC_MAIL_ADDR = "event.manager.g7t0@gmail.com";
	private static final String PASSWORD = "g7t0passwd";
	private static final Properties PROPERTIES = new Properties();
	private static final Session SESSION;
	private static final boolean ENABLED = false;
	
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
	
	private static Message prepareMessage(final String DST_MAIL_ADDR) throws MessagingException {
		Message msg = new MimeMessage(SESSION);
		msg.setFrom(new InternetAddress(DST_MAIL_ADDR));
		return msg;
	}
	
	private static Message buildMessage(User user, String subject, String content) {
		Message msg = null;
		try {
			msg = prepareMessage(user.getEmail());
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart msgBodyPart = new MimeBodyPart();
			msgBodyPart.setText(content);
			multipart.addBodyPart(msgBodyPart);
			msg.setContent(multipart);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
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
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(SRC_MAIL_ADDR));
				if(ENABLED) Transport.send(msg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String generateContentString(User user, String content) {
		StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append(String.format("Hello %s %s!%n%n", user.getFirstName(), user.getLastName()));
		contentBuilder.append(content);
		contentBuilder.append("\n\nYour Event Manager Team");
		return contentBuilder.toString();
	}
	
	public static void sendUserRegisterMessage(User user){
		String subject = "Event Manager: New User Registration";
		String content =  String.format("You just created a User Account with the username \"%s\" on \"Event Manager\" "
				+ "and used this Email for Registration.\n\nIf not, please contact us!", user.getUsername());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendUserDeleteMessage(User user){
		String subject = "Event Manager: Your User has been deleted";
		String content = String.format("Your User Account with the username \"%s\" on \"Event Manager\" has been deleted!\n\n"
				+ "If this was a mistake, please contact us!", user.getUsername());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventCreationMessage(User user, Event event) {
		String subject = "Event Manager: You have been invited to an Event";
		String content = String.format("The user with the username \"%s\" just created a new event \"%s\" on \"Event Manager\" "
				+ "and has invited you to take part in this event.\n"
				+ "You can now vote until \"%s\" when and where you think the event should take place.", event.getCreator().getUsername(),
				event.getName(), event.getPollEndDate());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventEvaluationMessage(User user, Event event) {
		String subject = String.format("Event Manager: Event: \"%s\", WHEN: \"%s\" - \"%s\", WHERE: \"%s\"",
				event.getName(), event.getTimeslot().getStart(), event.getTimeslot().getEnd(), event.getLocation().getName());
		String content = String.format("The voting time for the event \"%s\" on \"Event Manager\" has expired "
				+ "and the polls of all participants have been evaluated.\nThe event will take place from \"%s\" until \"%s\" at \"%s\"."
				, event.getName(), event.getTimeslot().getStart(), event.getTimeslot().getEnd(), event.getLocation().getName());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventCreatorDeletionMessage(User user, Event event) {
		String subject = String.format("Event Manager: Event: \"%s\" has been cancelled", event.getName());
		String content = String.format("");
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventNotEnoughParticipantsMessage(User user, Event event) {
		
	}
	
	public static void sendEventNotEnoughLocationsMessage(User user, Event event) {
		
	}
	
	public static void sendEventLocationDeletionMessage(User user, Event event) {
		
	}
	
	public static void sendEventNoCompatibleTimeslotAvailableMessage(User user, Event event) {
		
	}
}
