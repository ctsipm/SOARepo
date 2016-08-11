package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;


public class NotificationDTO {
	private String pca;
	private String environment;
	private String request;
	private String processStartTime;
	private String processFinishTime;
	private String initialPCAState;
	private BuildNotificationDTO buildNotificationDTO;
	private DeployNotificationDTO deployNotificationDTO;
	private List<Throwable> exceptionDetails;
	public static String statusWIP = null;
  	 
	public List<Throwable> getExceptionDetails() {
		if(exceptionDetails == null) exceptionDetails= new ArrayList<Throwable>();
		return exceptionDetails;
	}
	public void setExceptionDetails(List<Throwable> exceptionDetails) {
		this.exceptionDetails = exceptionDetails;
	}
	public String getProcessStartTime() {
		return processStartTime;
	}
	public void setProcessStartTime(String processStartTime) {
		this.processStartTime = processStartTime;
	}
	public String getProcessFinishTime() {
		return processFinishTime;
	}
	public void setProcessFinishTime(String processFinishTime) {
		this.processFinishTime = processFinishTime;
	}
	public String getPca() {
		return pca;
	}
	public void setPca(String pca) {
		this.pca = pca;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getInitialPCAState() {
		return initialPCAState;
	}
	public void setInitialPCAState(String initialPCAState) {
		this.initialPCAState = initialPCAState;
	}	
	public BuildNotificationDTO getBuildNotificationDTO() {
		if(buildNotificationDTO == null) buildNotificationDTO = new BuildNotificationDTO();
		return buildNotificationDTO;
	}
	public void setBuildNotificationDTO(BuildNotificationDTO buildNotificationDTO) {
		this.buildNotificationDTO = buildNotificationDTO;
	}
	public DeployNotificationDTO getDeployNotificationDTO() {
		if(deployNotificationDTO == null) deployNotificationDTO = new DeployNotificationDTO();
		return deployNotificationDTO;
	}
	public void setDeployNotificationDTO(DeployNotificationDTO deployNotificationDTO) {
		this.deployNotificationDTO = deployNotificationDTO;
	}
	
	public String getStatus(){
		if(exceptionDetails.size()>0) return "FAILURE";
		return "SUCCESS";
	}
	
}