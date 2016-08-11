package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;

public class MessageSetDTO {
	
	
	private ResourceDTO msetResource;
	private List<ResourceDTO> mxsdResources;
	private DeployEnvironmentDTO deployEnvironmentDTO;
	private String barFileName;
	
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
	
	public ResourceDTO getMsetResource() {
		return msetResource;
	}

	public void setMsetResource(ResourceDTO msetResource) {
		this.msetResource = msetResource;
	}

	public List<ResourceDTO> getMxsdResources() {
		if(mxsdResources == null) mxsdResources = new ArrayList<ResourceDTO>();
		return mxsdResources;
	}

	public void setMxsdResources(List<ResourceDTO> mxsdResources) {
		this.mxsdResources = mxsdResources;
	}

	public String getBarFileName() {
		return barFileName;
	}

	public void setBarFileName(String barFileName) {
		this.barFileName = barFileName;
	}

	public String getBarFileLocation(){
		return msetResource.getResourcePath();
	}
	 
}