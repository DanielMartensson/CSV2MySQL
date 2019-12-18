package se.danielmartensson.CSV2MySQL.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:mail.properties")
public class SendMail {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    public JavaMailSender emailSender;
	
	@Value("${mail.destination}")
	private String destination;
	
	@Value("${mail.subject}")
	private String subject;
	
	@Value("${mail.message}")
	private String message;
	
	private String sendMailDate;
	
	public void sendAlarmMail() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage(); 
		
		// Check if we have correct mail
		boolean valid = EmailValidator.getInstance().isValid(destination);
		if(valid == false)
			return;
		
		// Check if we have already send a mail this day
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String currentDate = simpleDateFormat.format(date);
		if(currentDate.equals(sendMailDate) == true) {
			return;
		}else {
			sendMailDate = currentDate; // Save
		}
		
		logger.info("Sending mail to: " + destination);
		
		// Send mail
		simpleMailMessage.setTo(destination);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(message);
                emailSender.send(simpleMailMessage);
		
	}
	
	/**
	 * This is used to reset the send date if we want a new error message by mail for every CSV batch
	 * @param sendMailDate
	 */
	public void setSendMailDate(String sendMailDate) {
		this.sendMailDate = sendMailDate;
	}
}
