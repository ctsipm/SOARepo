package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;

public class DeployNotificationDTO {
	private List<List<String>> barFile;
	private List<List<String>> mqDef;

	public List<List<String>> getBarFile() {
		if(barFile == null) barFile = new ArrayList<List<String>>();		
		return barFile;
	}

	public void setBarFile(List<List<String>> barFile) {
		this.barFile = barFile;
	}
  	
	public List<List<String>> getMQDef() {
		if(mqDef == null) mqDef = new ArrayList<List<String>>();		
		return mqDef;
	}

	public void setMQDef(List<List<String>> mqDef) {
		this.mqDef = mqDef;
	}
}