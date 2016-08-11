package com.jci.ei.buildautomation.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

public class Label {
	private static Logger logger = Logger.getLogger("JCIBuildAutomation");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//createLabel("QA", "12345");
		
		String subProjectPath =  "/MKSEAIBE/Development/MessageFlows/";
		logger.info(subProjectPath);
		
		String subProjName = subProjectPath;
		if (subProjName.endsWith("/")) {
			subProjName = subProjName.substring(0, subProjName.length()-1);
		}
		while (subProjName.contains("/")) {
			subProjName = subProjName.substring(subProjName.indexOf("/")+1);
		}
		
		if (!subProjName.isEmpty() && !"MessageFlows".equals(subProjName)) {
			subProjName = subProjName+".pj";
		}else{
			subProjName = "project.pj";
		}
		
		logger.info(subProjectPath);
		logger.info(subProjName);
		
	}
	
	private static String createLabel(String deployEnvironment, String parentPCA) {
		String todaysDate = new SimpleDateFormat("MM/dd/yy").format(Calendar.getInstance().getTime());
		String label = null;
		System.out.println(deployEnvironment);
		if ("DEV".equalsIgnoreCase(deployEnvironment)) {
			label = "Successful Deployment to DEV " + todaysDate + " PCA" + parentPCA.replace(":", "-");
		} else {
			label = "Promoted to " + deployEnvironment + " " + todaysDate + " PCA" + parentPCA.replace(":", "-");
		}
		System.out.println(label);
		return label;
	}	

}
