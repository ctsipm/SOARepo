package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {
	
	private List<MessageSetDTO> messageSetDTOList;
	private List<MessageFlowDTO> messageFlowDTOList;
	private List<MessageSetDTO> refMessageSetDTOList;
	private List<MessageFlowDTO> RefMessageFlowDTOList;	
	private List<MQDefinitionDTO> mqDefinitionDTOs;	
 
 	public List<MessageSetDTO> getMessageSetDTOList() {
 		if(messageSetDTOList == null) messageSetDTOList = new ArrayList<MessageSetDTO>();
		return messageSetDTOList;
	}
	public List<MessageFlowDTO> getMessageFlowDTOList() {
		if(messageFlowDTOList == null) messageFlowDTOList = new ArrayList<MessageFlowDTO>();
		return messageFlowDTOList;
	}
 	public List<MessageSetDTO> getRefMessageSetDTOList() {
 		if(refMessageSetDTOList == null) refMessageSetDTOList = new ArrayList<MessageSetDTO>();
		return refMessageSetDTOList;
	}
	public List<MessageFlowDTO> getRefMessageFlowDTOList() {
		if(RefMessageFlowDTOList == null) RefMessageFlowDTOList = new ArrayList<MessageFlowDTO>();
		return RefMessageFlowDTOList;
	}	
	public List<MQDefinitionDTO> getMQDefinitionDTOList() {
		if(mqDefinitionDTOs == null) mqDefinitionDTOs = new ArrayList<MQDefinitionDTO>();
		return mqDefinitionDTOs;
	} 	
}