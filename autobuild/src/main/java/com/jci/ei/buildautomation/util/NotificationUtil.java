package com.jci.ei.buildautomation.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jci.ei.buildautomation.dto.BuildNotificationDTO;
import com.jci.ei.buildautomation.dto.DeployNotificationDTO;
import com.jci.ei.buildautomation.dto.NotificationDTO;
import com.jci.ei.buildautomation.services.PropertyHolder;
	
public class NotificationUtil {
	
	/**
	 * Triggers email notification
	 * @param notificationDTO
	 * @param propertyHolder
	 */	
	public static void triggerMail(NotificationDTO notificationDTO, PropertyHolder propertyHolder) {
		
		DeployNotificationDTO deployNotificationDTO = notificationDTO.getDeployNotificationDTO();		
		BuildNotificationDTO buildNotificationDTO = notificationDTO.getBuildNotificationDTO();
		List<Throwable> exceptionDetails = notificationDTO.getExceptionDetails();
		
		String requestType = notificationDTO.getRequest();
		String subject = requestType +" status for PCA # "+notificationDTO.getPca()+" in "+notificationDTO.getEnvironment();
		StringBuffer messageText = new StringBuffer() ;
		messageText.append("************Status : "+ notificationDTO.getStatus()+" ************");
		messageText.append("\t\n");
		
		messageText.append("******"+requestType+" Request received at "+notificationDTO.getProcessStartTime()+"******");
		messageText.append("\t\n");
		
		if(exceptionDetails.size() > 0 ){
			messageText.append("******Error Details******");
			messageText.append("\t\n");
			for(Throwable  t : exceptionDetails){
				messageText.append(t);
				messageText.append("\t\n");
			}
		}
		
		if(buildNotificationDTO != null){
			List<List<String>> msgFlow = buildNotificationDTO.getMsgFlow();
			if(msgFlow != null && msgFlow.size()>0){
				messageText.append("******Message Flow build/checkin details******");
				messageText.append("\t\n");
				for(List<String> attributes : msgFlow){
					splitAttributesAndPopulateMessage(attributes, messageText);
				}
			}
			
			List<List<String>> msgSet = buildNotificationDTO.getMsgSet();
			if(msgSet != null && msgSet.size()>0){
				messageText.append("******Message Set build/checkin details******");
				messageText.append("\t\n");
				for(List<String> attributes : msgSet){
					splitAttributesAndPopulateMessage(attributes, messageText);
				}
			}
			
			
			List<List<String>> udp = buildNotificationDTO.getUdp();
			if(udp != null && udp.size()>0){
				messageText.append("******UDP update Details******");
				messageText.append("\t\n");
				for(List<String> attributes : udp){
					splitAttributesAndPopulateMessage(attributes, messageText);
				}
			}
			
		}
		
		if(deployNotificationDTO != null){
			List<List<String>> barFile = deployNotificationDTO.getBarFile();
			List<List<String>> mqDef = deployNotificationDTO.getMQDef();
			if(mqDef != null && mqDef.size()>0){
				messageText.append("******MQ Object Creation******");
				messageText.append("\t\n");
				for(List<String> attributes : mqDef){
					splitAttributesAndPopulateMessage(attributes, messageText);
				}
			}			
			if(barFile != null && barFile.size()>0){
				messageText.append("******Deploy Details******");
				messageText.append("\t\n");
				for(List<String> attributes : barFile){
					splitAttributesAndPopulateMessage(attributes, messageText);
				}
			}
		}
		
		messageText.append("******"+requestType+" Request completed at "+notificationDTO.getProcessFinishTime()+"******");

		boolean sessionDebug = false;
		// Create some properties and get the default Session.
		Properties props = System.getProperties();
		props.put("mail.host", propertyHolder.getSmtpHost());
		props.put("mail.transport.protocol", "smtp");
		Session session = Session.getDefaultInstance(props, null);
		// Set debug on the Session so we can see what is going on
		// Passing false will not echo debug info, and passing true
		// will.
		session.setDebug(sessionDebug);
		try {
			// Instantiate a new MimeMessage and fill it with the
			// required information.
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(propertyHolder.getMailFromAddress()));
			InternetAddress[] address = { new InternetAddress(propertyHolder.getMailToAddress()) };
			msg.setRecipients(Message.RecipientType.TO, address);
			InternetAddress[] address_Cc = { new InternetAddress(propertyHolder.getMailCCAddress()) };
			msg.setRecipients(Message.RecipientType.CC, address_Cc);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(messageText.toString());
			// Hand the message to the default transport service
			// for delivery.
			Transport.send(msg);

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

	/**
	 * Splits Attributes and Populate Message
	 * @param attributes
	 * @param messageText
	 */	
	private static void splitAttributesAndPopulateMessage(List<String> attributes, StringBuffer messageText) {
		if(attributes != null && attributes.size()>0){
			
			for(String attribute : attributes){
				String[] split = attribute.split(":::");
				messageText.append(split[0]);
				messageText.append(":");
				messageText.append(split[1]);
				messageText.append("\t\n");
			}
		}
	}
}
