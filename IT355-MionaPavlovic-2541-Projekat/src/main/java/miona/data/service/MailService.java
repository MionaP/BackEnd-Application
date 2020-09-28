package miona.data.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailService {
	

	 private MailSender mailSender;
	 
	 public void setMailSender(MailSender mailSender) {
			this.mailSender = mailSender;
		}
	 
	 /**
	  * Method used for sending e-mail to given address 
	  * @param to
	  * @param subject
	  * @param body
	  */
	 public void sendMail(String to, String subject, String body) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        mailSender.send(message);
	    }

}
