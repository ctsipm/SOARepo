package com.jci.ei.buildautomation.dto;

public class MQDefinitionDTO {

	private String resourcePCANumber;
	private String resourcePath;
	private String resourceName;
	private String version;
	private DeployEnvironmentDTO deployEnvironmentDTO;
	private boolean deployStatus;
	
	
	public String getResourcePCANumber() {
		return resourcePCANumber;
	}

	public void setResourcePCANumber(String resourcePCANumber) {
		this.resourcePCANumber = resourcePCANumber;
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
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}	
	
	public DeployEnvironmentDTO getDeployEnvironmentDTO() {
		return deployEnvironmentDTO;
	}

	public void setDeployEnvironmentDTO(DeployEnvironmentDTO deployEnvironmentDTO) {
		this.deployEnvironmentDTO = deployEnvironmentDTO;
	}
	
	public boolean isDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(boolean deployStatus) {
		this.deployStatus = deployStatus;
	}	

}