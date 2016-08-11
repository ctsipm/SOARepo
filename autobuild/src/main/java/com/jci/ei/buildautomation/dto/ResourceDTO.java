package com.jci.ei.buildautomation.dto;

public class ResourceDTO {

	private String resourcePath;
	private String resourceName;
	private String messageSetName;
	private String projectInterchangeName;
	private String version;
	private String subProjectPath;

	public String getMessageSetName() {
		return messageSetName;
	}	
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public void setPIName(String projectInterchangeName) {
		this.projectInterchangeName = projectInterchangeName;
	}
	public String getPIName() {
		return projectInterchangeName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setMessageSetName(String messageSetName) {
		this.messageSetName = messageSetName;
	}	
	public String getSubProjectPath() {
		return subProjectPath;
	}
	public void setSubProjectPath(String subProjectPath) {
		this.subProjectPath = subProjectPath;
	}	
	public String getResource() {
		resourcePath = resourcePath.replace("\\", "/");
		if (!resourcePath.endsWith("/")) {
			resourcePath = resourcePath + "/";
		}		
		return resourcePath+resourceName;
	}
	
	@Override
	public String toString() {
		return "Resource:"+resourceName+":"+resourcePath+":"+version;
	}

}