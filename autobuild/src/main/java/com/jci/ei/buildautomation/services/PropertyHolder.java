package com.jci.ei.buildautomation.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyHolder {

	private String buildWorkSpace;
	private String buildOutput;
	private String buildOutputMsgFlow;
	private String buildOutputMsgSet;
	private String mksHost;
	private String mksPort;
	private String mksUserName;
	private String mksPassword;
	private String sandboxLocation;
	private String cpidDefault;
	private String smtpHost;
	private String mailFromAddress;
	private String mailToAddress;
	private String mailCCAddress;
	private String wmbtkPath;
	private String buildMQScriptPath;
	private String resourcesPath;

	public String getBuildMQScriptPath() {
		return buildMQScriptPath;
	}

	public void setBuildMQScriptPath(String buildMQScriptPath) {
		this.buildMQScriptPath = buildMQScriptPath;
	}
	
	public String getWmbtkPath() {
		return wmbtkPath;
	}

	public void setWmbtkPath(String wmbtkPath) {
		this.wmbtkPath = wmbtkPath;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getMailFromAddress() {
		return mailFromAddress;
	}

	public void setMailFromAddress(String mailFromAddress) {
		this.mailFromAddress = mailFromAddress;
	}

	public String getMailToAddress() {
		return mailToAddress;
	}

	public void setMailToAddress(String mailToAddress) {
		this.mailToAddress = mailToAddress;
	}

	public String getMailCCAddress() {
		return mailCCAddress;
	}

	public void setMailCCAddress(String mailCCAddress) {
		this.mailCCAddress = mailCCAddress;
	}

	public String getSanboxLocation() {
		return sandboxLocation;
	}

	public void setSanboxLocation(String sanboxLocation) {
		this.sandboxLocation = sanboxLocation;
	}

	public String getCpidDefault() {
		return cpidDefault;
	}

	public void setCpidDefault(String cpidDefault) {
		this.cpidDefault = cpidDefault;
	}

	public String getBuildWorkSpace() {
		return buildWorkSpace;
	}

	public void setBuildWorkSpace(String buildWorkSpace) {
		this.buildWorkSpace = buildWorkSpace;
	}

	public String getBuildOutput() {
		return buildOutput;
	}

	public void setBuildOutput(String buildOutput) {
		this.buildOutput = buildOutput;
	}

	public String getBuildOutputMsgFlow() {
		return buildOutputMsgFlow;
	}

	public void setBuildOutputMsgFlow(String buildOutputMsgFlow) {
		this.buildOutputMsgFlow = buildOutputMsgFlow;
	}

	public String getBuildOutputMsgSet() {
		return buildOutputMsgSet;
	}

	public void setBuildOutputMsgSet(String buildOutputMsgSet) {
		this.buildOutputMsgSet = buildOutputMsgSet;
	}

	public String getMksHost() {
		return mksHost;
	}

	public void setMksHost(String mksHost) {
		this.mksHost = mksHost;
	}

	public String getMksPort() {
		return mksPort;
	}

	public void setMksPort(String mksPort) {
		this.mksPort = mksPort;
	}

	public String getMksUserName() {
		return mksUserName;
	}

	public void setMksUserName(String mksUserName) {
		this.mksUserName = mksUserName;
	}

	public String getMksPassword() {
		return mksPassword;
	}

	public void setMksPassword(String mksPassword) {
		this.mksPassword = mksPassword;
	}
	
	public String loadProperties(String fileName)
	{
		System.out.print("[i] Reading configuration from ");
		System.out.println(fileName);
		
		Properties props = new Properties();

		if (fileName != null)
		{
			try {
				props.load(new FileInputStream(fileName));
			} catch (FileNotFoundException e) 
			{
				System.err.println("[!] Error reading properties: "+e.getMessage());
				return "[!] Error reading properties: "+e.getMessage();
			} catch (IOException e) {
				System.err.println("[!] Error reading properties: "+e.getMessage());
				return "[!] Error reading properties: "+e.getMessage();
			}
		} else {
			System.err.println("[!] No config file specified.");
			return "[!] No config file specified.";
		}

		buildWorkSpace = props.getProperty("build_WorkSpace").trim();
		buildOutput = props.getProperty("buildDir_Output").trim();
		buildOutputMsgFlow = props.getProperty("buildDir_MSGFLOW").trim();
		buildOutputMsgSet = props.getProperty("buildDir_MSGSET").trim();
		buildMQScriptPath = props.getProperty("buildDir_MQSCRIPT").trim();
		
		mksHost = props.getProperty("mks_Host").trim();
		mksPort = props.getProperty("mks_Port").trim();
		mksUserName = props.getProperty("mks_userName").trim();
		mksPassword = props.getProperty("mks_Password").trim();
		
		sandboxLocation = props.getProperty("sandBox_Location").trim();
		cpidDefault = props.getProperty("cpid_Default").trim();

		smtpHost = props.getProperty("mail_smtp_host").trim();
		mailFromAddress = props.getProperty("mail_from_address").trim();
		mailToAddress = props.getProperty("mail_to_address").trim();
		mailCCAddress = props.getProperty("mail_cc_address").trim();
		
		wmbtkPath = props.getProperty("WMBToolKit_Path").trim();
		resourcesPath = props.getProperty("resources_path");
		
		return null;
	}

	public String getResourcePath(String fileName) 
	{
		StringBuilder newPath = new StringBuilder(512);
		newPath.append(resourcesPath).append("/").append(fileName);
		System.out.println("Resource's ["+fileName+"] path is ["+newPath.toString()+"]");
		
		return newPath.toString();
	}
}
