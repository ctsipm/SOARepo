package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;

public class DeployableResourceDTO {
	
	private List<MQDefinitionDTO> mqDefinitionDTOs;
	private List<BarFileResourceDTO> barFileResourceDTOs;
	
	public List<MQDefinitionDTO> getMQDefinitionDTOList() {
		if(mqDefinitionDTOs == null) mqDefinitionDTOs = new ArrayList<MQDefinitionDTO>();
		return mqDefinitionDTOs;
	}
	public List<BarFileResourceDTO> getBarFileResourceDTOList() {
		if(barFileResourceDTOs == null) barFileResourceDTOs = new ArrayList<BarFileResourceDTO>();
		return barFileResourceDTOs;
	}
}