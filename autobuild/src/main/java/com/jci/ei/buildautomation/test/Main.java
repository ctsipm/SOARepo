package com.jci.ei.buildautomation.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jci.ei.buildautomation.util.APISession;
import com.jci.ei.buildautomation.util.Member;
import com.jci.ei.buildautomation.util.Project;
import com.jci.ei.buildautomation.util.Sandbox;
import com.mks.api.response.APIException;

public class Main {
	
	public static void main(String g []) throws IOException{
		
		Sandbox sandbox;
		
		//Connecting to MKS server
		APISession apiSession = new APISession();
		try {
			
			String projectConfigPath = "c:/Projects/EAI/MB7/project.pj";

			/*
			String subProjectPath = "/I0001  -  I1999  AE/I1001  -  I1500/I1201  -  I2150/";
			
			String subProjName = subProjectPath;
			if (subProjName.endsWith("/")) {
				subProjName = subProjName.substring(0, subProjName.length()-2);
			}
			while (subProjName.contains("/")) {
				subProjName = subProjName.substring(subProjName.indexOf("/")+1);
			}
			subProjName = subProjName+".pj";
			*/
			
			apiSession.connect("198.36.98.128", 7001, "APIUser", "mks123");
			
			//Refreshing the Member details as bar file versions might have changed if the request was for Build and Deploy
			//String projectConfigPath = "c:/Projects/EAI/MB7/I0001  -  I1999  AE/I1001  -  I1500/I1201  -  I2150/I1201  -  I2150.pj";

			//String projectConfigPath = "c:/Projects/EAI/POC_Build_Automation/project.pj";
			Project project = new Project(apiSession, projectConfigPath);
			
			/*
			Map<String, Member> members = project.listFiles("E:/5.1 MKS Sandbox");
					
			//Getting the bar file member
			//Member member = members.get("I2081_PEGASO_INVOICE/Deployment/Bar Files/I2081_OAG_INVOICE_BEMAPICS_V1-1.bar");
					
			//Checking out the bar file member to make if it writable on the Sandbox location before updating the UDP's
			//member.checkout(apiSession, null, "10307:9550");
			
			//Checking out the ESQL file member to make if it writable on the Sandbox location
			Member member = members.get("Publish Flows/Deployment/MSGFLOWS/SAPPS_JFTT_ABO_PUB.esql");
			
			System.out.println(member.getDescription());
			System.out.println(member.getMemberId());
			System.out.println(member.getMemberName());
			System.out.println(member.getName());
			System.out.println(member.getRelativeFilePath());
			System.out.println(member.getTargetFilePath());
			
			String subProjectPath = member.getRelativeFilePath().replace("Publish Flows/Deployment/MSGFLOWS/SAPPS_JFTT_ABO_PUB.esql", "");
			System.out.println(subProjectPath);
			
			member.checkout(apiSession, "1.1.1.1", "10307:9550");
			*/
			
			//Reverting the check out's
			sandbox = new Sandbox(apiSession, project, "E:/5.1 MKS Sandbox", "10307:9550");
			//sandbox.editItem("111367", "State=Development Started", apiSession);
			
			sandbox.resyncMembers("Common Msgflows");
			//sandbox.resyncMembers("/PUBLISH and ROUTING FLOWS/PUBLISH FLOWS/Publish Flows/Deployment/Java/PGP/Code/PGPJavaUtil");
			//sandbox.resyncMembers("/MKSEAIBE/Development/MessageFlows/Common");
			
			//sandbox.addLabel("I1234_SAPSRM_PO/Deployment/MSGFLOWS//I1234_OAG_POSTATUS_ONIT.esql", "Promoted to QA 08/11/14 PCA102543", "1.5", projectConfigPath);
			
			//sandbox.revertMembers();
			
			/*
			System.out.println("Start!!");
			File commonMessageFlowFolder = new File(sandbox.getSandboxDir()+"/Common Msgflows");
			if(commonMessageFlowFolder.exists() && commonMessageFlowFolder.isDirectory()){
				File[] listFiles = commonMessageFlowFolder.listFiles();
				File outputLocation = new File("C:/build/code"+"/Common Message Flows");
				for(File f : listFiles){
					if(f.isFile()){
						FileUtils.copyFileToDirectory(f, outputLocation);
					}
				}
				File commonJavaProject = new File(commonMessageFlowFolder.getAbsolutePath()+"/Deployment/Java");
				if(commonJavaProject.exists() && commonJavaProject.isDirectory()){
					copyDirectory2Directory(commonJavaProject, new File("C:/build/code"));
				}
			}
			File commonPGPJavaUtilFolder = new File(sandbox.getSandboxDir()+"/PUBLISH and ROUTING FLOWS/PUBLISH FLOWS/Publish Flows/Deployment/Java/PGP/Code/PGPJavaUtil");
			if(commonPGPJavaUtilFolder.exists() && commonPGPJavaUtilFolder.isDirectory()){
				File[] listFiles = commonPGPJavaUtilFolder.listFiles();
				File outputLocation = new File("C:/build/code"+"/PGPJavaUtil");
				for(File f : listFiles){
					if(f.isFile()){
						FileUtils.copyFileToDirectory(f, outputLocation);
					}else if (f.isDirectory()) {
						FileUtils.copyDirectoryToDirectory(f, outputLocation);
					}
				}				
			}
			File commonFolder = new File(sandbox.getSandboxDir()+"/MKSEAIBE/Development/MessageFlows/Common");
			if(commonFolder.exists() && commonFolder.isDirectory()){
				File[] listFiles = commonFolder.listFiles();
				File outputLocation = new File("C:/build/code"+"/Common");
				for(File f : listFiles){
					if(f.isFile()){
						FileUtils.copyFileToDirectory(f, outputLocation);
					}else if (f.isDirectory()) {
						FileUtils.copyDirectoryToDirectory(f, outputLocation);
					}
				}				
			}			
			System.out.println("End!!"); */
			
			try {
				apiSession.terminate();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.prinln(e);
		}

		
	}
	
	private static void copyDirectory2Directory(File inputLocation, File outputLocation) throws IOException{
		if(inputLocation != null && outputLocation != null){
			File[] listFiles = inputLocation.listFiles();
			for(File f : listFiles){
				FileUtils.copyDirectoryToDirectory(f, outputLocation);
			}
		}
	}	

}
