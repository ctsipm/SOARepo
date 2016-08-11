package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;

public class BarFileResourceDTO extends ResourceDTO {
	
	private List<UserDefinedPropertyDTO> userDefinedPropertyDTOs;
	private String flowName;
	private DeployEnvironmentDTO deployEnvironmentDTO;
	
	private List<ResourceDTO> bundledFiles; 
	
	private boolean deployStatus;
	private String pcaNumber;

	public List<ResourceDTO> getBundledFiles() {
		if(bundledFiles == null) bundledFiles = new ArrayList<ResourceDTO>();
		return bundledFiles;
	}

	public void setBundledFiles(List<ResourceDTO> bundledFiles) {
		this.bundledFiles = bundledFiles;
	}

	public String getPcaNumber() {
		return pcaNumber;
	}

	public void setPcaNumber(String pcaNumber) {
		this.pcaNumber = pcaNumber;
	}

	public boolean isDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(boolean deployStatus) {
		this.deployStatus = deployStatus;
	}

	public DeployEnvironmentDTO getDeployEnvironmentDTO() {
		return deployEnvironmentDTO;
	}

	public void setDeployEnvironmentDTO(DeployEnvironmentDTO deployEnvironmentDTO) {
		this.deployEnvironmentDTO = deployEnvironmentDTO;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public List<UserDefinedPropertyDTO> getUserDefinedPropertyDTOs() {
		return userDefinedPropertyDTOs;
	}

	public void setUserDefinedPropertyDTOs(
			List<UserDefinedPropertyDTO> userDefinedPropertyDTOs) {
		this.userDefinedPropertyDTOs = userDefinedPropertyDTOs;
	}

}