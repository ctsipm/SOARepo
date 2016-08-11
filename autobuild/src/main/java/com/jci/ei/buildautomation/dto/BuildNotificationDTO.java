package com.jci.ei.buildautomation.dto;

import java.util.ArrayList;
import java.util.List;

public class BuildNotificationDTO {
	private List<List<String>> msgFlow;
	private List<List<String>> msgSet;
	private List<List<String>> udp;
	
	public List<List<String>> getMsgFlow() {
		if(msgFlow == null) msgFlow = new ArrayList<List<String>>();
		return msgFlow;
	}
	public void setMsgFlow(List<List<String>> msgFlow) {
		this.msgFlow = msgFlow;
	}
	public List<List<String>> getMsgSet() {
		if(msgSet == null) msgSet = new ArrayList<List<String>>();
		return msgSet;
	}
	public void setMsgSet(List<List<String>> msgSet) {
		this.msgSet = msgSet;
	}
	public List<List<String>> getUdp() {
		if(udp == null) udp = new ArrayList<List<String>>();
		return udp;
	}
	public void setUdp(List<List<String>> udp) {
		this.udp = udp;
	}
	
}