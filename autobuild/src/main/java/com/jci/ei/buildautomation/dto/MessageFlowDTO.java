package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;


public class MessageFlowDTO {

	private ResourceDTO msgFlowResourceDTO;
	private ResourceDTO esqlResourceDTO;
	private List<UserDefinedPropertyDTO> userDefinedPropertyDTOs;
	private DeployEnvironmentDTO deployEnvironmentDTO;
	private String resourcePCANumber;
	
	
	public String getResourcePCANumber() {
		return resourcePCANumber;
	}

	public void setResourcePCANumber(String resourcePCANumber) {
		this.resourcePCANumber = resourcePCANumber;
	}

	public DeployEnvironmentDTO getDeployEnvironmentDTO() {
		return deployEnvironmentDTO;
	}

	public void setDeployEnvironmentDTO(DeployEnvironmentDTO deployEnvironmentDTO) {
		this.deployEnvironmentDTO = deployEnvironmentDTO;
	}

	public List<UserDefinedPropertyDTO> getUserDefinedPropertyDTOs() {
		if(userDefinedPropertyDTOs == null) userDefinedPropertyDTOs = new ArrayList<UserDefinedPropertyDTO>();
		return userDefinedPropertyDTOs;
	}

	public void setUserDefinedPropertyDTOs(
			List<UserDefinedPropertyDTO> userDefinedPropertyDTOs) {
		this.userDefinedPropertyDTOs = userDefinedPropertyDTOs;
	}

	public String getBarFileLocation(){
		return esqlResourceDTO.getResourcePath();
	}
	
	public ResourceDTO getMsgFlowResourceDTO() {
		return msgFlowResourceDTO;
	}
	public void setMsgFlowResourceDTO(ResourceDTO msgFlowResourceDTO) {
		this.msgFlowResourceDTO = msgFlowResourceDTO;
	}
	public ResourceDTO getEsqlResourceDTO() {
		return esqlResourceDTO;
	}
	public void setEsqlResourceDTO(ResourceDTO esqlResourceDTO) {
		this.esqlResourceDTO = esqlResourceDTO;
	}
	
	@Override
	public String toString() {
		return "Size of UDP:"+userDefinedPropertyDTOs.size();
	}
}