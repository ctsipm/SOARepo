package com.jci.ei.buildautomation.dto;

public class DeployEnvironmentDTO {
	private String environment;
	private String host;
	private String queueManager;
	private String port;
	private String executionGroup;
	private String brokerName;
	private String svrconnChannel;
	
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getQueueManager() {
		return queueManager;
	}
	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getExecutionGroup() {
		return executionGroup;
	}
	public void setExecutionGroup(String executionGroup) {
		this.executionGroup = executionGroup;
	}
	public String getSvrConnChannel() {
		return svrconnChannel;
	}
	public void setSvrConnChannel(String svrconnChannel) {
		this.svrconnChannel = svrconnChannel;
	}	
}