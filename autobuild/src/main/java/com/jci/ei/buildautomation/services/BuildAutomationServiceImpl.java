package com.jci.ei.buildautomation.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.jci.ei.buildautomation.dto.NotificationDTO;
import com.jci.ei.buildautomation.util.BuildAndDeployUtil;
import com.jci.ei.buildautomation.webservice.BuildAndDeployServiceResponse;
import com.jci.ei.buildautomation.webservice.BuildServiceResponse;
import com.jci.ei.buildautomation.webservice.DeployServiceResponse;

/**
 * The Class BuildAutomationServiceImpl.
 */
public class BuildAutomationServiceImpl implements BuildAutomationService
{
	private static Logger logger = Logger.getLogger("JCIBuildAutomation");

	private PropertyHolder propertyHolder;
	
	@Override
	public BuildServiceResponse build(String pcaNumber, String environment) {
		BuildServiceResponse response = new BuildServiceResponse();
		
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setPca(pcaNumber);
		notificationDTO.setEnvironment(environment);
		notificationDTO.setRequest("Build");
		notificationDTO.setProcessStartTime(formatDate(Calendar.getInstance()));

		if ("true".equalsIgnoreCase(NotificationDTO.statusWIP)) {
			//Reject the Build thread
			logger.info("Build rejected for PCA number "+pcaNumber+" in "+environment+" environment. Another build is in progress. Please try later");
			response.setResponseMessage("Build rejected for PCA number "+pcaNumber+" in "+environment+" environment. Another build is in progress. Please try later");
		} else {
			//Starting the Build thread
			startThread(notificationDTO);
			NotificationDTO.statusWIP = "true";
			response.setResponseMessage("Build initiated for PCA number "+pcaNumber+" in "+environment+" environment");
		}
		
		//Returning back the response
		return response;

	}

	private void startThread(NotificationDTO notificationDTO) {
		Thread t = new Thread(new BuildAndDeployUtil(notificationDTO, propertyHolder));
		t.start();
	}

	@Override
	public DeployServiceResponse deploy(String pcaNumber, String environment) {
		DeployServiceResponse response = new DeployServiceResponse();
		
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setPca(pcaNumber);
		notificationDTO.setEnvironment(environment);
		notificationDTO.setRequest("Deploy");
		notificationDTO.setProcessStartTime(formatDate(Calendar.getInstance()));
	 
		if ("true".equalsIgnoreCase(NotificationDTO.statusWIP)) {
			//Reject the Deploy Thread
			logger.info("Deploy rejected for PCA number "+pcaNumber+" in "+environment+" environment. Another deployment is in progress. Please try later");
			response.setResponseMessage("Deploy rejected for PCA number "+pcaNumber+" in "+environment+" environment. Another deployment is in progress. Please try later");
		} else {
			//Starting the Deploy Thread
			startThread(notificationDTO);
			NotificationDTO.statusWIP = "true";
			response.setResponseMessage("Deploy initiated for PCA number "+pcaNumber+" in "+environment+" environment");
		}		
		
		//Returning back the response
		return response;

	}

	@Override
	public BuildAndDeployServiceResponse buildAndDeploy(String pcaNumber,
			String environment) {
		BuildAndDeployServiceResponse response = new BuildAndDeployServiceResponse();
		
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setPca(pcaNumber);
		notificationDTO.setEnvironment(environment);
		notificationDTO.setRequest("Build and Deploy");
		notificationDTO.setProcessStartTime(formatDate(Calendar.getInstance()));
	 
		if ("true".equalsIgnoreCase(NotificationDTO.statusWIP)) {
			//Reject the Build and Deploy Thread
			logger.info("Build and deploy rejected for PCA number "+pcaNumber+" in "+environment+" environment. Another build and deployment is in progress. Please try later");
			response.setResponseMessage("Build and deploy rejected for PCA number "+pcaNumber+" in "+environment+" environment. Another build and deployment is in progress. Please try later");
		} else {
			//Starting the Build and Deploy Thread
			startThread(notificationDTO);
			NotificationDTO.statusWIP = "true";
			response.setResponseMessage("Build and deploy initiated for PCA number "+pcaNumber+" in "+environment+" environment");
		}		
		
		//Returning back the response
		return response;

	}

	public static String formatDate(Calendar cal){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
		return sdf.format(cal.getTime()); 
	}

	public void setPropertyHolder(PropertyHolder propertyHolder) {
		this.propertyHolder = propertyHolder;
	}
 
}