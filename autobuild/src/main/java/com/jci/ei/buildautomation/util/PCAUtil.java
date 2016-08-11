package com.jci.ei.buildautomation.util;

public class PCAUtil {
	
	public static String getUDPKey(String environment){
		if(environment.equalsIgnoreCase("DEV")){
			return "EI UDP Dev";
		}else if(environment.equalsIgnoreCase("QA")){
			return "EI UDP QA";
		}else if(environment.equalsIgnoreCase("PROD")){
			return "EI UDP Production";
		}
		return null;
	}
	
	public static String getDeploymentBrokerKey(String environment){
		if(environment.equalsIgnoreCase("DEV")){
			return "EI Dev Broker";
		}else if(environment.equalsIgnoreCase("QA")){
			return "EI QA Broker";
		}else if(environment.equalsIgnoreCase("PROD")){
			return "EI Prod Broker";
		}
		return null;
	}
	
	public static String getExecutionGrpKey(String environment){
		if(environment.equalsIgnoreCase("DEV")){
			return "EI Dev EG";
		}else if(environment.equalsIgnoreCase("QA")){
			return "EI QA EG";
		}else if(environment.equalsIgnoreCase("PROD")){
			return "EI Prod EG";
		}
		return null;
	}
	
	public static String getQueueManagerKeyinPCA(String environment){
		if(environment.equalsIgnoreCase("DEV")){
			return "EI Dev QMGR";
		}else if(environment.equalsIgnoreCase("QA")){
			return "EI QA QMGR";
		}else if(environment.equalsIgnoreCase("PROD")){
			return "EI Prod QMGR";
		}
		return null;
	}
	
	public static String getQueueManagerKeyinDB(String environment){
		if(environment.equalsIgnoreCase("DEV")){
			return "DEVQMGR";
		}else if(environment.equalsIgnoreCase("QA")){
			return "QAQMGR";
		}else if(environment.equalsIgnoreCase("PROD")){
			return "PRODQMGR";
		}
		return null;
	}
}
