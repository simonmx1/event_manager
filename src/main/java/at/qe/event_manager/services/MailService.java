package at.qe.event_manager.services;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;
import at.qe.event_manager.model.Event;
import at.qe.event_manager.model.Location;
import at.qe.event_manager.model.User;

/**
 * This class is part of the event manager project which was programmed during the
 * "PS Software Architecture" course in the winter semester 2021/2022 at the University of Innsbruck.
 * 
 * @author Matthias Komar
 * @author Manuel Reichegger
 * @author Simon Muscatello
 * @author Stefan Wagner
 * 
 * Service for sending mails to users
 */
@Service
public class MailService {

	private static final Logger LOGGER = Logger.getLogger(MailService.class.getName());
	private static final String SRC_MAIL_ADDR = "event.manager.g7t0@gmail.com";
	private static final String PASSWORD = "g7t0passwd";
	private static final Properties PROPERTIES = new Properties();
	private static final Session SESSION;
	private static boolean enabled = true;
	
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
	
	
	/**
	 * A possibility to deactivate the MailService while testing or developing
	 */
	public static void disable() {
		enabled = false;
	}
	
	/**
	 * Create a mail message and set the session context and the source mail address
	 *
	 * @return the prepared message
	 */
	private static Message prepareMessage() throws MessagingException {
		Message msg = new MimeMessage(SESSION);
		msg.setFrom(new InternetAddress(SRC_MAIL_ADDR));
		return msg;
	}
	
	/**
	 * A helper method to avoid having to define the same content of a mail over and over again.
	 *
	 * @param user with firstname and lastname to which the mail is addressed 
	 * @param content of the mail message
	 * 
	 * @return the whole content string of the message with greeting and farewell
	 */
	private static String generateContentString(User user, String content) {
		return String.format("Hello %s %s!%n%n", user.getFirstName(), user.getLastName()) +
				content +
				"\n\nYour Event Manager Team";
	}
	
	/**
	 * Build everything together
	 * In this method, the mail is assembled as specified by {@link javax.mail}
	 *
	 * @param user with destination mail address
	 * @param subject of the mail message
	 * @param content of the mail message
	 *
	 * @return message ready to be sent
	 */
	private static Message buildMessage(User user, String subject, String content) {
		Message msg = null;
		try {
			msg = prepareMessage();
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			msg.setSubject(subject);
			Multipart multipart = new MimeMultipart();
			BodyPart msgBodyPart = new MimeBodyPart();
			msgBodyPart.setText(content);
			multipart.addBodyPart(msgBodyPart);
			msg.setContent(multipart);
		} catch (MessagingException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * Send the message by starting a new thread of {@link ThreadMailService}
	 * so that the backend continues to run
	 *
	 * @param msg the message which is sent
	 */
	private static void sendMessage(Message msg) {
		new ThreadMailService(msg).start();
	}
	
	/**
	 * Inner class of {@link MailService} which starts a simple thread
	 * in order to make mail sending multithreaded
	 */
	private static class ThreadMailService extends Thread{
		private Message msg;
		
		private Integer numberOfTries = 0;
		
		public ThreadMailService(Message msg) {
			this.msg = msg;
		}
		
		/**
		 * Start the thread which sends the mail message
		 */
		@Override
		public void run() {
			try {
				if(enabled) {
					numberOfTries++;
					Transport.send(msg);
				}
			} catch (SendFailedException e) {
				catchSendErrorAndPotentiallyTryAgain(e);
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
		
		/**
		 * If the sending of mails is rejected due to too many server accesses
		 * then this method catches the errors and tries again in 5 seconds.
		 * After 5 unsuccessful attempts to send the mail message, sending is aborted.
		 * 
		 * @param e the exception which lead to the call of this method
		 */
		private void catchSendErrorAndPotentiallyTryAgain(SendFailedException e) {
			try {
				if(numberOfTries < 5) {
					Thread.sleep(5000);
					run();
				}
				else {
					LOGGER.log(Level.WARNING, e.getMessage());
				}
			} catch (InterruptedException e1) {
				LOGGER.log(Level.SEVERE, e1.getMessage());
			}
		}
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
	
	public static void sendEventDeletionMessage(User user, Event event) {
		String subject = String.format("Event Manager: Event: \"%s\" has been cancelled", event.getName());
		String content = String.format("The event \"%s\" has just been deleted on \"Event Manager\" and will therefore not take place.\n"
				+ "If this was a mistake, please contact us!", event.getName());
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
		String content = String.format("The creator \"%s %s\" with the username \"%s\" of the event \"%s\" has been deleted on \"Event Manager\".\n"
				+ "Therefore the event \"%s\" has also been deleted.", event.getCreator().getFirstName(), event.getCreator().getLastName(),
				event.getCreator().getUsername(), event.getName(), event.getName());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventNotEnoughParticipantsMessage(User user, Event event, User deletedUser) {
		String subject = String.format("Event Manager: Event: \"%s\" has been cancelled", event.getName());
		String content = String.format("The participant \"%s %s\" with the username \"%s\" of the event \"%s\" has been deleted on \"Event Manager\".\n"
				+ "You are now the only participant in this event. For this reason it will be deleted.", deletedUser.getFirstName(), deletedUser.getLastName(),
				deletedUser.getUsername(), event.getName());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventNotEnoughLocationsMessage(User user, Event event) {
		String subject = String.format("Event Manager: Event: \"%s\" has been cancelled", event.getName());
		String content = String.format("All locations that were available for selection in the voting of the event \"%s\" on \"Event Manager\" "
				+ "have been deleted. For this reason, this event cannot take place and therefore it will be deleted.", event.getName());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventLocationDeletionMessage(User user, Event event, Location location) {
		String subject = String.format("Event Manager: Event: \"%s\" has been cancelled", event.getName());
		String content = String.format("Unfortunately, the location with the name \"%s\" selected in the voting of the event \"%s\" "
				+ "has been deleted on \"Event Manager\". For this reason, this event cannot take place and therefore it will be deleted.",
				location.getName(), event.getName());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
	
	public static void sendEventNoCompatibleTimeslotAvailableMessage(User user, Event event) {
		String subject = String.format("Event Manager: Event: \"%s\" has been cancelled", event.getName());
		String content = String.format("Unfortunately, no timeslots were found in the voting of the event \"%s\" on \"Event Manager\", in which all "
				+ "participants of the event can also participate. For this reason, this event cannot take place and therefore it will be deleted.",
				event.getName());
		sendMessage(buildMessage(user, subject, generateContentString(user, content)));
	}
}
