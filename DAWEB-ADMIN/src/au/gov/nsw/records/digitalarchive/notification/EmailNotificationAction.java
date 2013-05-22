package au.gov.nsw.records.digitalarchive.notification;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.core.io.ClassPathResource;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The SMTP mail sender action. This action send email to the configured recipients with the configured message. 
 * @author wisanup
 *
 */
@XStreamAlias("mail-notification")
public class EmailNotificationAction {

	private String to;
	private String subject;
	private String message;
	private ClassPathResource cpr;	
	private EmailNotificationActionConfig config;
	private static final Log log = LogFactory.getLog(EmailNotificationAction.class);
	
	public void processAction() {
		
		List<String> recipients = new ArrayList<String>();
		for (String recipient: to.split(",")){
			recipients.add(recipient);
		}
		
		sendEmail(recipients, subject, message);
	}


	/**
	 * Send email to the given recipients with the given subject and message 
	 * @param recipients the list of recipient email to be sent to
	 * @param subject the subject of the email to be sent
	 * @param message the message content of the email to be sent
	 */
	public void sendEmail(List<String> recipients, String subject, String message){
		try {
			log.info(String.format("Sending email to [%s] ", recipients.toString()));
			HtmlEmail email = new HtmlEmail();

			email.setHostName(config.getMailHost());
			if (!config.getMailPassword().isEmpty()){
				email.setAuthentication(config.getMailUser(), config.getMailPassword());
			}
			email.setSmtpPort(config.getMailPort());
			email.setFrom(config.getMailUser());
			for (String to: recipients){
				email.addTo(to);
			}
			email.setSubject(String.format(subject));

			//email.setTextMsg(message);
			email.setHtmlMsg(message);

			email.setDebug(false);
			email.send();
			log.info(String.format("Email sent to [%s] ", recipients.toString()));
			// OK we're done here
			
		} catch (EmailException e) {
			log.error("Encountered an error while sending an email",e);
		}
	}
	

	public void prepare(int actionSetId, HttpServletRequest req) {
		
		if (config == null){
			ConfigDeserializer<EmailNotificationActionConfig> configLoader = new ConfigDeserializer<EmailNotificationActionConfig>();
			cpr = new ClassPathResource("emailnotification_config.xml");
			EmailNotificationActionConfig templateConf = new EmailNotificationActionConfig("localhost", "opengov@records.nsw.gov.au", "api_passwd", 25);
			//config = configLoader.load(templateConf, ConfigHelper.getMailNotificationConfig());
			config = configLoader.load(templateConf, cpr.getFilename());
		}
	}

	/**
	 * Set the recipients of the sending email
	 * @param to the recipients of the sending email
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Set the subject of the sending email
	 * @param subject the subject of the sending email
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Set the content of the sending mail
	 * @param text the content of the sending mail
	 */
	public void setText(String text) {
		this.message = text;
	}
}
