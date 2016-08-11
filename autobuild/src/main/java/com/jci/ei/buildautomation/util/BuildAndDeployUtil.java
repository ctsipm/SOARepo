package com.jci.ei.buildautomation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;

import com.ibm.broker.config.proxy.BrokerConnectionParameters;
import com.ibm.broker.config.proxy.BrokerProxy;
import com.ibm.broker.config.proxy.DeployResult;
import com.ibm.broker.config.proxy.ExecutionGroupProxy;
import com.ibm.broker.config.proxy.LogEntry;
import com.ibm.broker.config.proxy.MQBrokerConnectionParameters;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.jci.ei.buildautomation.dto.BarFileResourceDTO;
import com.jci.ei.buildautomation.dto.DeployEnvironmentDTO;
import com.jci.ei.buildautomation.dto.DeployableResourceDTO;
import com.jci.ei.buildautomation.dto.MQDefinitionDTO;
import com.jci.ei.buildautomation.dto.MessageFlowDTO;
import com.jci.ei.buildautomation.dto.MessageSetDTO;
import com.jci.ei.buildautomation.dto.NotificationDTO;
import com.jci.ei.buildautomation.dto.ProjectDTO;
import com.jci.ei.buildautomation.dto.ResourceDTO;
import com.jci.ei.buildautomation.dto.UserDefinedPropertyDTO;
import com.jci.ei.buildautomation.mq.MQObjCreation;
import com.jci.ei.buildautomation.mq.ReadandFormatQDef;
import com.jci.ei.buildautomation.services.PropertyHolder;
import com.mks.api.response.APIException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BuildAndDeployUtil implements Runnable{
	
	private APISession apiSession;
	private Project project;
	private NotificationDTO notificationDTO;
	
	private Sandbox sandbox;
	private Map<String, Member> members;
	private PropertyHolder propertyHolder;
	
	private File buildDir_MSGFLOW_File = null;
	private File buildDir_MSGSET_File = null;
	private File buildDir_BAR_File = null;
	private File buildDir_MQSCRIPT_File = null;
	
	private String buildScriptFilePath = null;
	private String updateUDPScriptFilePath = null;
	
	private static Logger logger = Logger.getLogger("JCIBuildAutomation");
	private static final int BROKER_TIMEOUT = 300000;
	
	//This should be returned from PCA. Hard coding for time being as client has not set up the PCA
	//String projectConfigPath = "c:/Projects/EAI/POC_Build_Automation/project.pj";
	String projectConfigPath = "c:/Projects/EAI/MB7/project.pj";
	
	public BuildAndDeployUtil(NotificationDTO notificationDTO,PropertyHolder propertyHolder){
		this.notificationDTO = notificationDTO;
		this.propertyHolder = propertyHolder;
		apiSession = new APISession();
		
		buildDir_MSGFLOW_File = new File(propertyHolder.getBuildOutputMsgFlow());
		buildDir_MSGSET_File = new File(propertyHolder.getBuildOutputMsgSet());
		buildDir_BAR_File = new File(propertyHolder.getBuildOutput());
		buildDir_MQSCRIPT_File = new File(propertyHolder.getBuildMQScriptPath());
		
		//buildScriptFilePath = this.getClass().getClassLoader().getResource("build_compile.xml").getPath();
		//updateUDPScriptFilePath = this.getClass().getClassLoader().getResource("build_updateUDP.xml").getPath();
		
		buildScriptFilePath = propertyHolder.getResourcePath("build_compile.xml");
		updateUDPScriptFilePath = propertyHolder.getResourcePath("build_updateUDP.xml");
		
		//PropertyConfigurator.configure(propertyHolder.getLog4jPropsPath());
	}
	
	/**
	 * Clears the build folders
	 */
	private void clearfolders() {
		try {
			FileUtils.cleanDirectory(buildDir_MSGFLOW_File);
			FileUtils.cleanDirectory(buildDir_MSGSET_File);
			FileUtils.cleanDirectory(buildDir_BAR_File);
			FileUtils.cleanDirectory(buildDir_MQSCRIPT_File);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * Builds a bar file for the input PCA and environment details
	 * @param notificationDTO containing the PCA and environment details
	 * @return DeployableResourceDTO beans 
	 */
	public DeployableResourceDTO build(NotificationDTO notificationDTO){
		List<BarFileResourceDTO> barFileResources = null;
		DeployableResourceDTO deployableResourceDTO = new DeployableResourceDTO();
			try {
				ProjectDTO projectDTO = BuildParamReaderUtil.readBuildAndDeployParam(notificationDTO, apiSession);
				populateProject(projectConfigPath);
				createSandBox(propertyHolder.getSanboxLocation());
				barFileResources = checkoutAndBuildFiles(projectDTO, notificationDTO);
				deployableResourceDTO.getBarFileResourceDTOList().addAll(barFileResources);
				if (projectDTO.getMQDefinitionDTOList() != null) {
					deployableResourceDTO.getMQDefinitionDTOList().addAll(projectDTO.getMQDefinitionDTOList());
				}
			} catch (Exception e) {
				logger.error(">>Exception in Build.."+e);
				e.printStackTrace();
				notificationDTO.getExceptionDetails().add(e);
				logger.error(notificationDTO.getExceptionDetails());
			}
			
		return deployableResourceDTO;
	}
	
	/**
	 * Extracts the PCA attributes from MKS, creates project, creates sandbox and deploys a bar file for the input PCA and environment details
	 * @param notificationDTO
	 * @return status of deployment
	 */
	public void deploy(NotificationDTO notificationDTO){
		boolean deployStatus = false;
		List<BarFileResourceDTO> barFileResources = null;
		List<MQDefinitionDTO> mqDefinationList = null;
		DeployableResourceDTO deployableResourceDTO = new DeployableResourceDTO();
		try {
			
			//Fetching the Deploy parameters
			deployableResourceDTO = BuildParamReaderUtil.readDeployParam(notificationDTO, apiSession);
			barFileResources = deployableResourceDTO.getBarFileResourceDTOList();
			mqDefinationList = deployableResourceDTO.getMQDefinitionDTOList();			
			
			populateProject(projectConfigPath);
			createSandBox(propertyHolder.getSanboxLocation());
			
			if (!mqDefinationList.isEmpty()) {
				deployStatus = createMQObject(mqDefinationList, notificationDTO);
			} else {
				deployStatus = true;
			}
			
			if (deployStatus && !barFileResources.isEmpty()) {
				deploy(barFileResources, notificationDTO);
			}
			
		} catch (Exception e) {
			logger.error(">>Exception in Build.."+e);
			e.printStackTrace();
			notificationDTO.getExceptionDetails().add(e);
			logger.error(notificationDTO.getExceptionDetails());
		}
		
	}

	/**
	 * Deploys the bar files 
	 * @param mqDefinationList
	 * @param notificationDTO
	 * @return Boolean
	 * @throws APIException
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */	
	public boolean createMQObject(List<MQDefinitionDTO> mqDefinationList, NotificationDTO notificationDTO) throws APIException, IOException, ClassNotFoundException, SQLException{
		
		boolean deployStatus = false;
		MQObjCreation mqObjC = new MQObjCreation();
		
		for(MQDefinitionDTO mqDef : mqDefinationList){
			String qMgrName = mqDef.getDeployEnvironmentDTO().getQueueManager();
			String hostName = mqDef.getDeployEnvironmentDTO().getHost();
			String potNumber = mqDef.getDeployEnvironmentDTO().getPort();
			String channelName = mqDef.getDeployEnvironmentDTO().getSvrConnChannel();
			
			logger.info("Connecting Queue Manager: " + qMgrName + " Host Name: " + hostName + " Port: " + potNumber + " Channel Name: " + channelName);
			MQQueueManager queueManager = mqObjC.connectMQ(qMgrName, hostName, potNumber, channelName, notificationDTO);
			
			if (queueManager != null) {
				
				logger.info("Connected to Queue Manager successfully");
				
				ReadandFormatQDef Qdef = new ReadandFormatQDef();

				String realativeFilePath = mqDef.getResourcePath();
				if (!realativeFilePath.endsWith("/")) {
					realativeFilePath = realativeFilePath + "/";
				}
				String filename = mqDef.getResourceName();
				
				logger.info(realativeFilePath+filename);
				
				//Getting the mq or subscription script member
				Member member = members.get(realativeFilePath+filename);

				//Checking out the bar file member to make if it writable on the Sandbox location before updating the UDP's
				member.checkout(apiSession, mqDef.getVersion(), propertyHolder.getCpidDefault());
				
				logger.info("1");
				
				String subProjectPath = member.getRelativeFilePath().replace(realativeFilePath+filename, "");
				if (!subProjectPath.endsWith("/")) {
					subProjectPath = subProjectPath + "/";
				}
				if (!subProjectPath.startsWith("/")) {
					subProjectPath = "/" + subProjectPath;
				}
				String absoluteFilePath = sandbox.getSandboxDir()+subProjectPath+realativeFilePath;
				
				logger.info("**** Member resynchronize>>");
				sandbox.resyncMembers(subProjectPath);
				
				//Reverting the check out's
				sandbox.revertMembers(subProjectPath);
				
				File srcFile = new File(absoluteFilePath+filename);
				
				//Copying the mq or subscription script to Build directory
				FileUtils.copyFileToDirectory(srcFile, buildDir_MQSCRIPT_File);
			
				String[] mqscCommands = Qdef.readQDef(buildDir_MQSCRIPT_File+"/"+filename);
				
				for(String mqscCommand: mqscCommands){
					
					logger.info("Executing MQSC Command: " + mqscCommand);
					
					if (!"DEV".equalsIgnoreCase(notificationDTO.getEnvironment())) {

						String sourceVal = null;
						String targetVal = null;
						String chlName = null;
						String chlType = null;
						String connName = null;
						Boolean chlFlag = false;
						Pattern pat = null;
						Pattern pchlType = null;
						Pattern pconnName = null;
						Matcher mat = null;					
						
						if (mqscCommand.trim().startsWith("DEFINE QR") || mqscCommand.trim().startsWith("DEF QR")) {
							pat = Pattern.compile("(?<=RQMNAME\\(').*?(?=')");
						} else if (mqscCommand.trim().startsWith("DEFINE SUB") || mqscCommand.trim().startsWith("DEF SUB")) {
							pat = Pattern.compile("(?<=DESTQMGR\\().*?(?=\\))");
						} else if (mqscCommand.trim().startsWith("DEFINE CH") || mqscCommand.trim().startsWith("DEF CH")) {
							
							chlFlag = true;
							pchlType = Pattern.compile("(?<=CHLTYPE\\().*?(?=\\))");
							pconnName = Pattern.compile("(?<=CONNAME\\(').*?(?=')");
							
							if (mqscCommand.trim().startsWith("DEFINE CHL") || mqscCommand.trim().startsWith("DEF CHL")) {
								pat = Pattern.compile("(?<=CHL\\().*?(?=\\))");
							} else if (mqscCommand.trim().startsWith("DEFINE CHANNEL") || mqscCommand.trim().startsWith("DEF CHANNEL")) {
								pat = Pattern.compile("(?<=CHANNEL\\().*?(?=\\))");
							}
						}

						if (pat != null) {
							if (chlFlag) {
								//Retrieving Channel Name 
								mat = pat.matcher(mqscCommand);
								if (mat.find()) {
									chlName = mat.group();
								}
								//Retrieving Channel Type 
								mat = pchlType.matcher(mqscCommand);
								if (mat.find()) {
									chlType = mat.group();
								}
								//If Channel type = Sender
								if ("SDR".equalsIgnoreCase(chlType)) {
									mat = pconnName.matcher(mqscCommand);
									if (mat.find()) {
										connName = mat.group();
									}
								}
							} else {
								mat = pat.matcher(mqscCommand);
								if (mat.find()) {
									sourceVal = mat.group().replaceAll("'", "");
								}							
							}
						}
						
						if (sourceVal != null || chlFlag) {
							
							if (chlFlag) {
								
								String[] queueManagerSplit = null;
								queueManagerSplit = chlName.split("\\.");

								for(String queueManagerName: queueManagerSplit){
									
									targetVal = mqObjC.getCorrQmgrNameForDev(apiSession, notificationDTO, queueManagerName);
									mqscCommand = mqscCommand.replaceAll(queueManagerName, targetVal);
								}
								
								targetVal = mqObjC.getChannelConnName(notificationDTO, queueManagerSplit[1], apiSession);
				        		logger.info("Replacing CONNAME: " + connName + " From PCA by " + targetVal);
				        		mqscCommand = mqscCommand.replace(connName, targetVal);
								
							} else {
								targetVal = mqObjC.getCorrQmgrNameForDev(apiSession, notificationDTO, sourceVal);
								mqscCommand = mqscCommand.replaceAll(sourceVal, targetVal);
							}

							logger.info("Executing Modified MQSC Command: " + mqscCommand);
						}						
						
					}
					
					logger.info(mqObjC.executeMQSCScript(queueManager, mqscCommand));
					
				}

				List<String> mqObjCrtAttributes = new ArrayList<String>();
				mqObjCrtAttributes.add("QueueManager Host:::" + hostName);
				mqObjCrtAttributes.add("QueueManager Port:::" + potNumber);
				mqObjCrtAttributes.add("QueueManager Name:::" + qMgrName);
				mqObjCrtAttributes.add("Channel Name:::" + channelName);
				mqObjCrtAttributes.add("MQ Script:::" + mqDef.getResourceName());

				notificationDTO.getDeployNotificationDTO().getMQDef().add(mqObjCrtAttributes);
				
				mqDef.setDeployStatus(true);
				
				//Update PCA Status for MQ Object Creation
				updateDeploymentStatusInPCA(mqDef, notificationDTO);				
				deployStatus = true;
				
				try {
					queueManager.disconnect();
				} catch (MQException e) {
					logger.error(e);
					notificationDTO.getExceptionDetails().add(e);
				}				
			}
		}
		
		return deployStatus;
	}	
	
	/**
	 * Deploys the bar files 
	 * @param barFileResources
	 * @param notificationDTO
	 * @throws APIException
	 */
	public void deploy(List<BarFileResourceDTO> barFileResources, NotificationDTO notificationDTO) throws Exception{
		for(BarFileResourceDTO resourceDTO : barFileResources){
			try{
				logger.info("*****Starting UDP update on bar file "+resourceDTO.getResource()+"******");
	
				String updatedUDPValues = null;
				String barFileRelativePathUDP = null;
				String barFileAbsolutePathUDP = null;
				String barFilePath = null;				
				String subProjectPath = null;
				
				List<UserDefinedPropertyDTO> userDefinedPropertyDTOs = resourceDTO.getUserDefinedPropertyDTOs();			
				
				if(userDefinedPropertyDTOs != null && userDefinedPropertyDTOs.size() > 0){
					
					//Refreshing the Member details as bar file versions might have changed if the request was for Build and Deploy
					members = project.listFiles(propertyHolder.getSanboxLocation());
					
					//Getting the bar file member
					Member member = members.get(resourceDTO.getResource());
					
					subProjectPath = member.getRelativeFilePath().replace(resourceDTO.getResource(), "");
					if (!subProjectPath.endsWith("/")) {
						subProjectPath = subProjectPath + "/";
					}
					if (!subProjectPath.startsWith("/")) {
						subProjectPath = "/" + subProjectPath;
					}
					barFilePath = sandbox.getSandboxDir()+subProjectPath+resourceDTO.getResource();
					
					//Checking out the bar file member to make if it writable on the Sandbox location before updating the UDP's
					File barFileSrc = new File(barFilePath);
					sandbox.thaw(barFileSrc, resourceDTO.getResourceName());
					member.checkout(apiSession, null, propertyHolder.getCpidDefault());
					
					//Updating the UDP's
					updatedUDPValues = AntExecutorUtil.executeUDPUpdateTask(barFilePath, resourceDTO.getFlowName(), userDefinedPropertyDTOs, updateUDPScriptFilePath, propertyHolder.getWmbtkPath());
					
					String barFileNameUDP = resourceDTO.getResourceName().replaceFirst(".bar", "_"+notificationDTO.getEnvironment()+".bar");
					barFileRelativePathUDP = resourceDTO.getResource().replaceFirst(".bar", "_"+notificationDTO.getEnvironment()+".bar");
					barFileAbsolutePathUDP = propertyHolder.getSanboxLocation() + subProjectPath + barFileRelativePathUDP;
					
					//Target BAR File
					File barFileUDP = new File(barFileAbsolutePathUDP);
					boolean barFileExist = barFileUDP.exists();
					
					//Checking if the bar file is already present. If yes, checking out the same
					Member barFileMember = null;
					if(barFileExist){
						barFileMember = members.get(BuildParamReaderUtil.replaceBackwareSlashWithForwardSlash(barFileRelativePathUDP));
						if (barFileMember != null) {
							sandbox.thaw(barFileUDP, barFileNameUDP);
							barFileMember.checkout(apiSession, null, propertyHolder.getCpidDefault());
						}
					}
					
					logger.info("Copy "+barFileSrc+" to "+barFileUDP);
					FileUtils.copyFile(barFileSrc, barFileUDP);
					
					//Checking in the bar file if it was already present, else adding a new member for the created file
					if(barFileExist && barFileMember != null){
						logger.info("Checking in barfile.." + barFileNameUDP);
						sandbox.checkin(barFileUDP, barFileNameUDP, "Checking in barfile..");
					}else{
						logger.info("Adding new bar file member.." + barFileNameUDP);
						sandbox.add(barFileUDP, "Adding new bar file member:"+barFileNameUDP);
					}					
					
					//Reverting the check out's and freeze the bar files
					sandbox.revertMembers(subProjectPath);					
					sandbox.freeze(barFileSrc, resourceDTO.getResourceName());
					sandbox.freeze(barFileUDP, barFileNameUDP);
					
				}else{
					//Refreshing the Member details as bar file versions might have changed if the request was for Build and Deploy
					members = project.listFiles(propertyHolder.getSanboxLocation());
					
					logger.info(resourceDTO.getResource());
					
					//Getting the bar file member
					Member member = members.get(resourceDTO.getResource());
					subProjectPath = member.getRelativeFilePath().replace(resourceDTO.getResource(), "");
					if (!subProjectPath.endsWith("/")) {
						subProjectPath = subProjectPath + "/";
					}
					if (!subProjectPath.startsWith("/")) {
						subProjectPath = "/" + subProjectPath;
					}
					barFilePath = sandbox.getSandboxDir()+subProjectPath+resourceDTO.getResource();
				}
				
				logger.info("*****Starting the Deploy process for "+resourceDTO.getResource()+"******");
				DeployEnvironmentDTO deployEnvironmentDTO = resourceDTO.getDeployEnvironmentDTO();
				if (barFileAbsolutePathUDP != null) {
					//AntExecutorUtil.executeDeployTask(deployEnvironmentDTO, barFileAbsolutePathUDP, deployScriptFilePath);
					deployBarFile(deployEnvironmentDTO, barFileAbsolutePathUDP);
				}else {
					//AntExecutorUtil.executeDeployTask(deployEnvironmentDTO, barFilePath, deployScriptFilePath);
					deployBarFile(deployEnvironmentDTO, barFilePath);
				}
				
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
				String subProjectConfigPath = projectConfigPath.replace("/project.pj", "")+subProjectPath+subProjName;
				logger.info(subProjectConfigPath);
				
				//Updating the promotion labels on bar files
				if (barFileRelativePathUDP != null) {
					sandbox.addLabel(barFileRelativePathUDP, createLabel(notificationDTO.getEnvironment(), notificationDTO.getPca()), null, subProjectConfigPath);
				}
				
				List<String> barDeployAttributes = new ArrayList<String>();
				barDeployAttributes.add("Deploy Broker Host:::"+deployEnvironmentDTO.getHost());
				barDeployAttributes.add("Deploy Broker Port:::"+deployEnvironmentDTO.getPort());
				barDeployAttributes.add("Deploy Queue Manager:::"+deployEnvironmentDTO.getQueueManager());
				barDeployAttributes.add("Deploy Execution group:::"+deployEnvironmentDTO.getExecutionGroup());
				barDeployAttributes.add("Bar file:::"+resourceDTO.getResource());
				
				if(updatedUDPValues != null){
					barDeployAttributes.add("Updated UDP Values:::"+updatedUDPValues);
				}
				notificationDTO.getDeployNotificationDTO().getBarFile().add(barDeployAttributes);
				resourceDTO.setDeployStatus(true);
				
				//Updating the promotion labels on bundled files based on the environment to which it is promoted
				updateLabelOnFiles(resourceDTO.getBundledFiles(), deployEnvironmentDTO.getEnvironment(), notificationDTO.getPca(), subProjectConfigPath);
				
			}catch(BuildException bex){
				bex.printStackTrace();
				notificationDTO.getExceptionDetails().add(bex);
			}catch(IOException ioex){
				ioex.printStackTrace();
				notificationDTO.getExceptionDetails().add(ioex);				
			}
		}
		
		//Updating back the Deployment status into PCA
		updateDeploymentStatusInPCA(barFileResources, notificationDTO);
		
	}

	/**
	 * Deploy bar files
	 * @param bundledFiles
	 * @param deployEnvironment
	 * @param parentPCA
	 * @throws APIException
	 */
	private void deployBarFile(DeployEnvironmentDTO deployEnvironmentDTO, String barfilePath) throws Exception {	
	
		BrokerConnectionParameters bcp =
			new MQBrokerConnectionParameters(deployEnvironmentDTO.getHost(), Integer.parseInt(deployEnvironmentDTO.getPort()), deployEnvironmentDTO.getQueueManager());
		if (bcp != null) {
			BrokerProxy broker = BrokerProxy.getInstance(bcp);
			if (broker != null) {
				ExecutionGroupProxy eg = broker.getExecutionGroupByName(deployEnvironmentDTO.getExecutionGroup());
				if (eg != null) {
					DeployResult deployResult = eg.deploy(barfilePath, true, BROKER_TIMEOUT);
					String completionCode = deployResult.getCompletionCode().toString();
					logger.info("Deploy Status" + completionCode);
					if ("success".compareTo(completionCode) != 0) {
						Enumeration<LogEntry> desc = deployResult.getDeployResponses();
						while (desc.hasMoreElements()){
							logger.error(desc.nextElement()); 
					      }						
						throw new Exception(deployResult.toString());
					}else{
						Enumeration<LogEntry> desc = deployResult.getDeployResponses();
						while (desc.hasMoreElements()){
							logger.info(desc.nextElement()); 
					      }						
					}
				} else {
					throw new Exception("Connection to EG Refused");
				}
			}else{
				throw new Exception("Connection to Broker Refused");
			}
		}else{
			throw new Exception("Connection to Broker Refused");
		}
	}
	
	/**
	 * Updates the labels on files
	 * @param bundledFiles
	 * @param deployEnvironment
	 * @param parentPCA
	 * @throws APIException
	 */
	private void updateLabelOnFiles(List<ResourceDTO> bundledFiles, String deployEnvironment, String parentPCA, String subProjectConfigPath) throws APIException {
		if(bundledFiles != null && bundledFiles.size()>0 && deployEnvironment!= null && !"".equals(deployEnvironment)){
			for(ResourceDTO file : bundledFiles){
				if (!("".equals(file.getPIName()) || file.getPIName() == null)) {
					sandbox.addLabel(file.getResourcePath()+"/"+file.getPIName(), createLabel(deployEnvironment, parentPCA), file.getVersion(), subProjectConfigPath);
				} else {
					sandbox.addLabel(file.getResource(), createLabel(deployEnvironment, parentPCA), file.getVersion(), subProjectConfigPath);
				}
			}
		}
	}

	/**
	 * Creates label string with build environment and Parent PCA input
	 * @param deployEnvironment
	 * @param parentPCA
	 * @return created label
	 */
	private String createLabel(String deployEnvironment, String parentPCA) {
		String todaysDate = new SimpleDateFormat("MM/dd/yy").format(Calendar.getInstance().getTime());
		String label = null;
		logger.info(deployEnvironment);
		if ("DEV".equalsIgnoreCase(deployEnvironment)) {
			label = "Successful Deployment to DEV " + todaysDate + " PCA" + parentPCA.replace(":", "-");
		} else {
			label = "Promoted to " + deployEnvironment + " " + todaysDate + " PCA" + parentPCA.replace(":", "-");
		}
		logger.info(label);
		return label;
	}

	/**
	 * Updates the deployment status back to the PCA
	 * @param barFileResources
	 * @param notificationDTO
	 * @throws APIException
	 */
	private void updateDeploymentStatusInPCA(
			List<BarFileResourceDTO> barFileResources, NotificationDTO notificationDTO) throws APIException {
		boolean parentPCADeployStatus = true;
		
		for(BarFileResourceDTO barFileResourceDTO : barFileResources){
			boolean barFileDeployStatus = barFileResourceDTO.isDeployStatus();
			String deployObjectPCANumber = barFileResourceDTO.getPcaNumber();
			
			//Setting the parent PCA status to false if any one bar file deploy fails			
			if(barFileDeployStatus == false) parentPCADeployStatus = false;
			
			//Updating the EI Deployment Indicator attribute with the deploy status
			logger.info("*****Updating the EI Deployment Indicator attribute for PCA:"+deployObjectPCANumber);
			sandbox.editItem(deployObjectPCANumber, "EI Deployment Indicator="+barFileDeployStatus, apiSession);
			
		}
		
		//Updating the State attribute in Parent PCA
		if(parentPCADeployStatus){
			String parentPCANumber = notificationDTO.getPca();
			logger.info("*****Updating the State attribute for PCA:"+parentPCANumber);
			
			//TODO : Commenting this as failing due to improper state setup in PCA. Not able to update the state from 'Customer Contacted' to 'Promoted to <Environment value> Environment'. 
			/*if ("QA".equalsIgnoreCase(notificationDTO.getEnvironment())) {
				if("Build".equals(notificationDTO.getRequest())){
					if ("Ready for MKS Sign Off".equals(notificationDTO.getInitialPCAState())) {
						sandbox.editItem(parentPCANumber, "State=Ready for Promotion to QA", apiSession);
					}else if ("BPT Fix Promoted to Dev.".equals(notificationDTO.getInitialPCAState())) {
						sandbox.editItem(parentPCANumber, "State=Ready to Promote Fix to QA", apiSession);
					}
				}else if("Deploy".equals(notificationDTO.getRequest())){
					if ("Ready for Promotion to QA".equals(notificationDTO.getInitialPCAState())) {
						sandbox.editItem(parentPCANumber, "State=Promoted to QA Environment", apiSession);
					}else if ("Promotion to QA Approved".equals(notificationDTO.getInitialPCAState())) {
						sandbox.editItem(parentPCANumber, "State=Promoted to QA Environment", apiSession);
					}else if ("Ready to Promote Fix to QA".equals(notificationDTO.getInitialPCAState())) {
						sandbox.editItem(parentPCANumber, "State=BPT Fix Promoted to QA", apiSession);
					}
				}else if("Build and Deploy".equals(notificationDTO.getRequest())){
					if ("Ready for MKS Sign Off".equals(notificationDTO.getInitialPCAState())) {
						sandbox.editItem(parentPCANumber, "State=Promoted to QA Environment", apiSession);
					}else if ("BPT Fix Promoted to Dev.".equals(notificationDTO.getInitialPCAState())) {
						sandbox.editItem(parentPCANumber, "State=BPT Fix Promoted to QA", apiSession);
					}
				}
			}*/
		}
	}

	/**
	 * Updates the deployment status back to the PCA
	 * @param mqDefinitionDTO
	 * @param notificationDTO
	 * @throws APIException
	 */
	private void updateDeploymentStatusInPCA(
			MQDefinitionDTO mqDef, NotificationDTO notificationDTO) throws APIException {
		
			boolean mqScriptCreateStatus = mqDef.isDeployStatus();
			String deployObjectPCANumber = mqDef.getResourcePCANumber();
			
			//Updating the EI Deployment Indicator attribute with the deploy status
			logger.info("*****Updating the EI Deployment Indicator attribute for PCA:" + deployObjectPCANumber);
			sandbox.editItem(deployObjectPCANumber, "EI Deployment Indicator=" + mqScriptCreateStatus, apiSession);
			
	}
	
	/**
	 * Builds bar files and deploys the same for a given PCA number and environment 
	 * @param notificationDTO
	 */
	public void buildAndDeploy(NotificationDTO notificationDTO) {		
		
		boolean deployStatus = false;
		List<BarFileResourceDTO> barFileResources = null;
		List<MQDefinitionDTO> mqDefinationList = null;
		DeployableResourceDTO deployableResourceDTO = new DeployableResourceDTO();		
		
		deployableResourceDTO = build(notificationDTO);
		barFileResources = deployableResourceDTO.getBarFileResourceDTOList();
		
		if(notificationDTO.getExceptionDetails().size() == 0){
			try {
				
				mqDefinationList = deployableResourceDTO.getMQDefinitionDTOList();
				
				if (!mqDefinationList.isEmpty()) {
					deployStatus = createMQObject(mqDefinationList, notificationDTO);
				} else {
					deployStatus = true;
				}
				
				if (deployStatus && !barFileResources.isEmpty()) {
					deploy(barFileResources, notificationDTO);
				}
				
			} catch (Exception e) {
				logger.error(">>Exception in Build and Deploy.."+e);
				e.printStackTrace();
				notificationDTO.getExceptionDetails().add(e);
				logger.error(notificationDTO.getExceptionDetails());
			}
		}
		logger.info("****BUILD and DEPLOY Done *****");
	}
	
	/**
	 * Checks out the required files from MKS, builds the bar file and checks ini the same into MKS
	 * @param projectDTO
	 * @param notificationDTO
	 * @return List of BarFileResourceDTO beans
	 * @throws Exception 
	 */
	private List<BarFileResourceDTO> checkoutAndBuildFiles(ProjectDTO projectDTO, NotificationDTO notificationDTO) throws Exception{

		List<BarFileResourceDTO> barFileResources = new ArrayList<BarFileResourceDTO>();
		
		if(projectDTO != null){		
			
			//Build Message Set project
			processMessageSetProject(projectDTO, notificationDTO, barFileResources);

			//Build Message Flows
			processMessageFlowProject(projectDTO, notificationDTO, barFileResources);			
			
			//Reverting the checkout of members 
			//sandbox.revertMembers();
		}
		return barFileResources;
	}

	/**
	 * Processes the build of Message flow project 
	 * @param projectDTO
	 * @param notificationDTO
	 * @param barFileResources
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void processMessageFlowProject(ProjectDTO projectDTO, NotificationDTO notificationDTO,
			List<BarFileResourceDTO> barFileResources) throws Exception {
		
		BarFileResourceDTO barFileResource;
		
		List<MessageSetDTO> refMessageSetDTOList = projectDTO.getRefMessageSetDTOList();
		List<String> msetProjectName = new ArrayList<String>();
		
		if(refMessageSetDTOList != null){
			logger.info("******Starting to build Reference Message Set project******");
			for(MessageSetDTO messageSetDTO : refMessageSetDTOList){
				
				ResourceDTO mSetResourceDTO = messageSetDTO.getMsetResource();
				List<ResourceDTO> mxsdResourceDTO = messageSetDTO.getMxsdResources();
				
				if(mSetResourceDTO != null){
					
					String resourceProjectName = null;
					String msetResourcePath = mSetResourceDTO.getResourcePath();
					File msetResourceFile = new File(msetResourcePath);
					String msetResourceName = mSetResourceDTO.getResourceName();
					String msetPIName = mSetResourceDTO.getPIName();
					String version = mSetResourceDTO.getVersion();
					
					
					if ("".equals(msetPIName)) {
						
						resourceProjectName = msetResourceFile.getParentFile().getName();
						
						Member member = members.get(msetResourcePath+"/"+msetResourceName);
						member.checkout(apiSession, version, propertyHolder.getCpidDefault());

						String subProjectPath = member.getRelativeFilePath().replace(msetResourcePath+"/"+msetResourceName, "");
						if (!subProjectPath.endsWith("/")) {
							subProjectPath = subProjectPath + "/";
						}
						if (!subProjectPath.startsWith("/")) {
							subProjectPath = "/" + subProjectPath;
						}
						
						for (ResourceDTO resourceDTO : mxsdResourceDTO) {

							String mxsdResourceName = resourceDTO.getResourceName();
							String mxsdVersion = resourceDTO.getVersion();

							member = members.get(msetResourcePath+"/"+mxsdResourceName);
							member.checkout(apiSession, mxsdVersion, propertyHolder.getCpidDefault());
						}
						
						logger.info("**** Member resynchronize>>");
						sandbox.resyncMembers(subProjectPath);
						
						File mSetSrcDir = new File(sandbox.getSandboxDir()+subProjectPath+msetResourceFile.getParentFile().getPath());
						FileUtils.copyDirectoryToDirectory(mSetSrcDir, new File(propertyHolder.getBuildWorkSpace()));
						
						sandbox.revertMembers(subProjectPath);						
						
					} else {
				        
						resourceProjectName = msetPIName.replace(".zip", "");
						
						logger.info("**** MSet PI Build Start>>");
				        
						Member member = members.get(msetResourcePath+"/"+msetPIName);
						member.checkout(apiSession, version, propertyHolder.getCpidDefault());

						String subProjectPath = member.getRelativeFilePath().replace(msetResourcePath+"/"+msetPIName, "");
						if (!subProjectPath.endsWith("/")) {
							subProjectPath = subProjectPath + "/";
						}
						if (!subProjectPath.startsWith("/")) {
							subProjectPath = "/" + subProjectPath;
						}				        

				        File zipfile = new File(sandbox.getSandboxDir()+subProjectPath+msetResourcePath+"/"+msetPIName);
				        File directory = new File(propertyHolder.getBuildWorkSpace());
						
						unzip(zipfile, directory);
						sandbox.revertMembers(subProjectPath);
						
						logger.info("**** MSet PI Build End>>");
					}
					
					msetProjectName.add(resourceProjectName);
				}
			}
		}		
		
		List<MessageFlowDTO> refMessageFlowDTOList = projectDTO.getRefMessageFlowDTOList();
		if(refMessageFlowDTOList != null){
			logger.info("******Starting to build Reference Message flow project******");
			
			for(MessageFlowDTO messageFlowDTO : refMessageFlowDTOList){

				ResourceDTO esqlResourceDTO = messageFlowDTO.getEsqlResourceDTO();
				ResourceDTO msgFlowResourceDTO = messageFlowDTO.getMsgFlowResourceDTO();
				
				//Processing the esql files
				if(esqlResourceDTO != null){
					String esqlResourceName = esqlResourceDTO.getResourceName();
					String esqlResourcePath = esqlResourceDTO.getResourcePath();
					String version = esqlResourceDTO.getVersion();
					
					Member member = members.get(esqlResourcePath+esqlResourceName);
					logger.info("Member found>>>>>"+member);
					boolean checkout = member.checkout(apiSession, version, propertyHolder.getCpidDefault());
					logger.info("**** Member checkout>>"+checkout);					

					String subProjectPath = member.getRelativeFilePath().replace(esqlResourcePath+esqlResourceName, "");
					if (!subProjectPath.endsWith("/")) {
						subProjectPath = subProjectPath + "/";
					}
					if (!subProjectPath.startsWith("/")) {
						subProjectPath = "/" + subProjectPath;
					}
					
					logger.info("**** Member resynchronize>>");
					sandbox.resyncMembers(subProjectPath);
					
					File srcFile = new File(sandbox.getSandboxDir()+subProjectPath+esqlResourcePath+esqlResourceName);
					
					//Copying the esql file to Build directory
					FileUtils.copyFileToDirectory(srcFile, buildDir_MSGFLOW_File);
					
					sandbox.revertMembers(subProjectPath);

				}
				
				//Processing the message flow files
				if(msgFlowResourceDTO != null){
					String msgflowResourceName = msgFlowResourceDTO.getResourceName();
					String msgflowResourcePath = msgFlowResourceDTO.getResourcePath();
					String version = msgFlowResourceDTO.getVersion();
			
					//Retrieving the member and checking out the same
					Member member = members.get(msgflowResourcePath+msgflowResourceName);
					member.checkout(apiSession, version, propertyHolder.getCpidDefault());

					String subProjectPath = member.getRelativeFilePath().replace(msgflowResourcePath+msgflowResourceName, "");
					if (!subProjectPath.endsWith("/")) {
						subProjectPath = subProjectPath + "/";
					}
					if (!subProjectPath.startsWith("/")) {
						subProjectPath = "/" + subProjectPath;
					}					
					
					logger.info("**** Member resynchronize>>");
					sandbox.resyncMembers(subProjectPath);			

					File srcFile = new File(sandbox.getSandboxDir()+subProjectPath+msgflowResourcePath+msgflowResourceName);
					
					//Copying the message flow file to Build directory
					FileUtils.copyFileToDirectory(srcFile, buildDir_MSGFLOW_File);
					
					sandbox.revertMembers(subProjectPath);
					
				}
			}
		}		
		
		List<MessageFlowDTO> messageFlowDTOList = projectDTO.getMessageFlowDTOList();
		if(messageFlowDTOList != null){
			logger.info("******Starting to build Message flow project******");
			
			//Copying common message flow project
			copyCommonMessageFlowProjects();
			
			for(MessageFlowDTO messageFlowDTO : messageFlowDTOList){
				
				String versionText = null;
				ResourceDTO esqlResourceDTO = messageFlowDTO.getEsqlResourceDTO();
				ResourceDTO msgFlowResourceDTO = messageFlowDTO.getMsgFlowResourceDTO();
				List<UserDefinedPropertyDTO> userDefinedPropertyDTOs = messageFlowDTO.getUserDefinedPropertyDTOs();
				
				String barfileName = null;
				
				//Notification List
				List<String> msgFlowAttributes = new ArrayList<String>();
				int fileCounter = 0;
				
				//Processing the esql files
				if(esqlResourceDTO != null){
					String esqlResourceName = esqlResourceDTO.getResourceName();
					String esqlResourcePath = esqlResourceDTO.getResourcePath();
					String version = esqlResourceDTO.getVersion();
					versionText = "E "+version;
					
					msgFlowAttributes.add("File name"+ ++fileCounter +":::"+esqlResourceName);
					msgFlowAttributes.add("File version"+ fileCounter +":::"+version);
					
					Member member = members.get(esqlResourcePath+esqlResourceName);
					logger.info("Member found>>>>>"+member);
					boolean checkout = member.checkout(apiSession, version, propertyHolder.getCpidDefault());
					logger.info("**** Member checkout>>"+checkout);

					String subProjectPath = member.getRelativeFilePath().replace(esqlResourcePath+esqlResourceName, "");
					if (!subProjectPath.endsWith("/")) {
						subProjectPath = subProjectPath + "/";
					}
					if (!subProjectPath.startsWith("/")) {
						subProjectPath = "/" + subProjectPath;
					}
					
					logger.info("Setting subProjectPath in ESQL ResourceDTO " + subProjectPath);
					esqlResourceDTO.setSubProjectPath(subProjectPath);
					
					logger.info("**** Member resynchronize>>");
					sandbox.resyncMembers(subProjectPath);
					
					File srcFile = new File(sandbox.getSandboxDir()+subProjectPath+esqlResourcePath+esqlResourceName);
					
					logger.info("**** File >>"+srcFile);
					
					//Copying the esql file to Build directory
					FileUtils.copyFileToDirectory(srcFile, buildDir_MSGFLOW_File);
					
					barfileName = constructBarFileName(esqlResourceName, version);
				}
				
				
				//Processing the message flow files
				if(msgFlowResourceDTO != null){
					String msgflowResourceName = msgFlowResourceDTO.getResourceName();
					String msgflowResourcePath = msgFlowResourceDTO.getResourcePath();
					String version = msgFlowResourceDTO.getVersion();
					versionText = versionText + "   M " + version;
					
					msgFlowAttributes.add("File name"+ ++fileCounter +":::"+msgflowResourceName);
					msgFlowAttributes.add("File version"+ fileCounter +":::"+version);
					
					//Retrieving the member and checking out the same
					Member member = members.get(msgflowResourcePath+msgflowResourceName);
					member.checkout(apiSession, version, propertyHolder.getCpidDefault());

					String subProjectPath = member.getRelativeFilePath().replace(msgflowResourcePath+msgflowResourceName, "");
					if (!subProjectPath.endsWith("/")) {
						subProjectPath = subProjectPath + "/";
					}
					if (!subProjectPath.startsWith("/")) {
						subProjectPath = "/" + subProjectPath;
					}					
					
					logger.info("Setting subProjectPath in MFLOW ResourceDTO " + subProjectPath);
					msgFlowResourceDTO.setSubProjectPath(subProjectPath);
					
					logger.info("**** Member resynchronize>>");
					sandbox.resyncMembers(subProjectPath);			
					
					String resourcePath = sandbox.getSandboxDir()+subProjectPath+msgflowResourcePath+msgflowResourceName;
					updateVersionMFInfo(resourcePath, versionText);
					File srcFile = new File(resourcePath);
					
					//Copying the message flow file to Build directory
					FileUtils.copyFileToDirectory(srcFile, buildDir_MSGFLOW_File);
					
					//Copying the project file to Build directory
					FileUtils.copyFileToDirectory(new File(sandbox.getSandboxDir()+subProjectPath+msgflowResourcePath+".project"), buildDir_MSGFLOW_File);
					try{
						String barFileLocation = getBarFileLocation(new File(msgflowResourcePath));
						logger.info(barFileLocation);
						String barFileRelativePath = barFileLocation+"/"+barfileName;
						logger.info(barFileRelativePath);
						String barFileAbsolutePath = propertyHolder.getSanboxLocation() + subProjectPath + barFileRelativePath;
						logger.info(barFileAbsolutePath);
						File barFile = new File(barFileAbsolutePath);
						boolean barFileExist = barFile.exists();
						
						//Checking if the bar file is already present. If yes, checking out the same
						if(barFileExist){
							Member barFileMember = members.get(BuildParamReaderUtil.replaceBackwareSlashWithForwardSlash(barFileRelativePath));
							sandbox.thaw(barFile, barfileName);							
							barFileMember.checkout(apiSession, null, propertyHolder.getCpidDefault());
						}
						
						String messageFlowBuildFolderName = new File(propertyHolder.getBuildOutputMsgFlow()).getName();
						String messageFlowRefBuildFolderName = messageFlowBuildFolderName;
						
						if (!msetProjectName.isEmpty()) {
							for (Iterator iterator = msetProjectName.iterator(); iterator.hasNext();) {
								messageFlowRefBuildFolderName = (String) iterator.next() + " " + messageFlowRefBuildFolderName.trim();
							}
						}
						
						//Triggering the build
						AntExecutorUtil.executebuildTask(propertyHolder.getBuildWorkSpace(), barFile.getParentFile().getPath(), propertyHolder.getBuildOutput(), barfileName, messageFlowRefBuildFolderName, messageFlowBuildFolderName+"/"+msgflowResourceName, buildScriptFilePath, propertyHolder.getWmbtkPath(), true);
						
						//Checking in the bar file if it was already present, else adding a new member for the created file
						if(barFileExist){
							sandbox.checkin(barFile, barfileName, "Checking in barfile"+barfileName);
						}else{
							sandbox.add(barFile, "Adding new bar file member:"+barfileName);
						}
						
						sandbox.freeze(barFile, barfileName);	
						
						//Updating the checked in bar file name and location into PCA
						updateBarFileDetailsInPCA(messageFlowDTO.getResourcePCANumber(), barfileName, barFileLocation);
						
						barFileResource = new BarFileResourceDTO();
						barFileResource.setResourceName(barfileName);
						barFileResource.setResourcePath(barFileLocation);
						barFileResource.setPcaNumber(messageFlowDTO.getResourcePCANumber());
						barFileResource.getBundledFiles().add(esqlResourceDTO);
						barFileResource.getBundledFiles().add(msgFlowResourceDTO);
						
						if(userDefinedPropertyDTOs != null && userDefinedPropertyDTOs.size() > 0){
							barFileResource.setUserDefinedPropertyDTOs(userDefinedPropertyDTOs);
							barFileResource.setFlowName(trucateFileExtension(msgflowResourceName));
						}
						barFileResource.setDeployEnvironmentDTO(messageFlowDTO.getDeployEnvironmentDTO());
						barFileResources.add(barFileResource);
						
						msgFlowAttributes.add("Bar file name:::"+barfileName);
						msgFlowAttributes.add("Bar file location:::"+barFileRelativePath);
						msgFlowAttributes.add("Bar file checkin time:::"+formatDate(Calendar.getInstance()));
						
						sandbox.revertMembers(subProjectPath);
						
					}catch(BuildException bex){
						//Reverting the checkout of members in case of exception 
						logger.error("Exception occured in Build>>>>>"+bex);
						sandbox.revertMembers(subProjectPath);
						throw bex;
					}
					
				}
				
				//Adding the message Flow attributes to the list
				notificationDTO.getBuildNotificationDTO().getMsgFlow().add(msgFlowAttributes);
			}
			
			logger.info("******Message flow project build completed******");
			if (!msetProjectName.isEmpty()) {
				for (Iterator iterator = msetProjectName.iterator(); iterator.hasNext();) {
					FileUtils.deleteDirectory(new File(propertyHolder.getBuildWorkSpace()+"/"+(String) iterator.next()));
				}
			}			
		}
	}
	
	/**
	 * Copies the Common Message flow projects and Common Java projects into the workspace 
	 * @throws IOException
	 */
	private void copyCommonMessageFlowProjects() throws IOException {
		File commonMessageFlowFolder = new File(sandbox.getSandboxDir()+"/Common Msgflows");
		if(commonMessageFlowFolder.exists() && commonMessageFlowFolder.isDirectory()){
			File[] listFiles = commonMessageFlowFolder.listFiles();
			File outputLocation = new File(propertyHolder.getBuildWorkSpace()+"/Common Message Flows");
			for(File f : listFiles){
				if(f.isFile()){
					FileUtils.copyFileToDirectory(f, outputLocation);
				}
			}
			File commonJavaProject = new File(commonMessageFlowFolder.getAbsolutePath()+"/Deployment/Java");
			if(commonJavaProject.exists() && commonJavaProject.isDirectory()){
				copyDirectory2Directory(commonJavaProject, new File(propertyHolder.getBuildWorkSpace()));
			}
		}
		File commonPGPJavaUtilFolder = new File(sandbox.getSandboxDir()+"/PUBLISH and ROUTING FLOWS/PUBLISH FLOWS/Publish Flows/Deployment/Java/PGP/Code/PGPJavaUtil");
		if(commonPGPJavaUtilFolder.exists() && commonPGPJavaUtilFolder.isDirectory()){
			File[] listFiles = commonPGPJavaUtilFolder.listFiles();
			File outputLocation = new File(propertyHolder.getBuildWorkSpace()+"/PGPJavaUtil");
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
			File outputLocation = new File(propertyHolder.getBuildWorkSpace()+"/Common");
			for(File f : listFiles){
				if(f.isFile()){
					FileUtils.copyFileToDirectory(f, outputLocation);
				}else if (f.isDirectory()) {
					FileUtils.copyDirectoryToDirectory(f, outputLocation);
				}
			}				
		}		
	}

	/*private void copyCommonMessageFlowProjects() throws IOException {
		File commonMessageFlowFolder = new File(sandbox.getSandboxDir()+"/Common");
		if(commonMessageFlowFolder.exists() && commonMessageFlowFolder.isDirectory()){
			copyDirectory2Directory(commonMessageFlowFolder, new File(propertyHolder.getBuildWorkSpace()));
			File commonMessageFlowProject = new File(sandbox.getSandboxDir()+"/Common/Common Message Flows");
			if(commonMessageFlowProject.exists() && commonMessageFlowProject.isDirectory()){
				File commonJavaProject = new File(commonMessageFlowProject.getAbsolutePath()+"/Deployment/Java");
				if(commonJavaProject.exists() && commonJavaProject.isDirectory()){
					copyDirectory2Directory(commonJavaProject, new File(propertyHolder.getBuildWorkSpace()));
				}
			}
		}
	}*/
	
	/**
	 * Recursively copies the Folders present in input location to the Output location
	 * @param inputLocation
	 * @param outputLocation
	 * @throws IOException
	 */
	private void copyDirectory2Directory(File inputLocation, File outputLocation) throws IOException{
		if(inputLocation != null && outputLocation != null){
			File[] listFiles = inputLocation.listFiles();
			for(File f : listFiles){
				FileUtils.copyDirectoryToDirectory(f, outputLocation);
			}
		}
	}	

	/**
	 * Updates PCA attributes with values
	 * @param resourcePCANumber
	 * @param barfileName
	 * @param barFileLocation
	 * @throws APIException
	 */
	private void updateBarFileDetailsInPCA(String resourcePCANumber,
			String barfileName, String barFileLocation) throws APIException {
		
		logger.info("********Calling updateBarFileDetailsInPCA Method with Parameteres--"+"Resource PCA Number:"+resourcePCANumber+"Barfile Location:"+barFileLocation+"Bar file name:"+barfileName);
		if(resourcePCANumber != null && !"".equalsIgnoreCase(resourcePCANumber) && 
				barfileName != null && !"".equalsIgnoreCase(barfileName) && 
						barFileLocation != null && !"".equalsIgnoreCase(barFileLocation)){
			
			//Updating the Bar file name in PCA
			String updateField = "EI Create Bar Filename="+barfileName;
			sandbox.editItem(resourcePCANumber, updateField, apiSession);

			//Updating the Bar file location in PCA
			updateField = "EI Bar File MKS Location="+barFileLocation;
			sandbox.editItem(resourcePCANumber, updateField, apiSession);

		}
	}

	/**
	 * Processes the build of Message set project
	 * @param projectDTO
	 * @param notificationDTO
	 * @param barFileResources
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void processMessageSetProject(ProjectDTO projectDTO, NotificationDTO notificationDTO, List<BarFileResourceDTO> barFileResources) throws Exception {
		
		BarFileResourceDTO barFileResource;

		List<MessageSetDTO> refMessageSetDTOList = projectDTO.getRefMessageSetDTOList();
		List<String> refMsetProjectName = new ArrayList<String>();
		
		if(refMessageSetDTOList != null){
			logger.info("******Starting to build Reference Message Set project******");
			for(MessageSetDTO messageSetDTO : refMessageSetDTOList){
				
				ResourceDTO mSetResourceDTO = messageSetDTO.getMsetResource();
				List<ResourceDTO> mxsdResourceDTO = messageSetDTO.getMxsdResources();
				
				if(mSetResourceDTO != null){
					
					String msetResourcePath = mSetResourceDTO.getResourcePath();
					File msetResourceFile = new File(msetResourcePath);
					String msetResourceName = mSetResourceDTO.getResourceName();
					String msetPIName = mSetResourceDTO.getPIName();
					String version = mSetResourceDTO.getVersion();
					
					String resourceRefProjectName = null;
					
					if ("".equals(msetPIName)) {

						resourceRefProjectName = msetResourceFile.getParentFile().getName();
						
						Member member = members.get(msetResourcePath+"/"+msetResourceName);
						member.checkout(apiSession, version, propertyHolder.getCpidDefault());
						String subProjectPath = member.getRelativeFilePath().replace(msetResourcePath+"/"+msetResourceName, "");

						if (!subProjectPath.endsWith("/")) {
							subProjectPath = subProjectPath + "/";
						}
						if (!subProjectPath.startsWith("/")) {
							subProjectPath = "/" + subProjectPath;
						}

						for (ResourceDTO resourceDTO : mxsdResourceDTO) {

							String mxsdResourceName = resourceDTO.getResourceName();
							String mxsdVersion = resourceDTO.getVersion();
							
							member = members.get(msetResourcePath+"/"+mxsdResourceName);
							member.checkout(apiSession, mxsdVersion, propertyHolder.getCpidDefault());
						}
						
						logger.info("**** Member resynchronize>>");
						sandbox.resyncMembers(subProjectPath);
						
						File mSetSrcDir = new File(sandbox.getSandboxDir()+subProjectPath+msetResourceFile.getParentFile().getPath());
						FileUtils.copyDirectoryToDirectory(mSetSrcDir, buildDir_MSGSET_File);
						
						sandbox.revertMembers(subProjectPath);						
						
					}else {
						
						resourceRefProjectName = msetPIName.replace(".zip", "");
						
						logger.info("**** MSet PI Build Start>>");
				        
						Member member = members.get(msetResourcePath+"/"+msetPIName);
						member.checkout(apiSession, version, propertyHolder.getCpidDefault());

						String subProjectPath = member.getRelativeFilePath().replace(msetResourcePath+"/"+msetPIName, "");
						if (!subProjectPath.endsWith("/")) {
							subProjectPath = subProjectPath + "/";
						}
						if (!subProjectPath.startsWith("/")) {
							subProjectPath = "/" + subProjectPath;
						}				        

				        File zipfile = new File(sandbox.getSandboxDir()+subProjectPath+msetResourcePath+"/"+msetPIName);
						
						unzip(zipfile, buildDir_MSGSET_File);
						sandbox.revertMembers(subProjectPath);
						
						logger.info("**** MSet PI Build End>>");
						
					}
					
					refMsetProjectName.add(resourceRefProjectName);

				}
			}
		}
		
		List<MessageSetDTO> messageSetDTOList = projectDTO.getMessageSetDTOList();
		if(messageSetDTOList != null){
			logger.info("******Starting to build Message Set project******");
			for(MessageSetDTO messageSetDTO : messageSetDTOList){
				
				//Notification List
				List<String> msgSetAttributes = new ArrayList<String>();
				
				ResourceDTO mSetResourceDTO = messageSetDTO.getMsetResource();
				List<ResourceDTO> mxsdResourceDTO = messageSetDTO.getMxsdResources();
				
				String resourceProjectName = null;
				
				if(mSetResourceDTO != null){
					
					String versionText = null;
					
					String msetResourcePath = mSetResourceDTO.getResourcePath();
					File msetResourceFile = new File(msetResourcePath);
					
					String msetResourceName = mSetResourceDTO.getResourceName();
					String msetPIName = mSetResourceDTO.getPIName();
					String msetName = mSetResourceDTO.getMessageSetName();
					
					String msetBarName = msetName;
					
					String version = mSetResourceDTO.getVersion();
					versionText = "Mset "+version;
					
					msgSetAttributes.add("File name:::"+msetResourceName);
					msgSetAttributes.add("File version:::"+version);
					
					String subProjectPath = "";
					
					if ("".equals(msetPIName)) {

						resourceProjectName = msetResourceFile.getParentFile().getName();
						
						Member member = members.get(msetResourcePath+"/"+msetResourceName);
						member.checkout(apiSession, version, propertyHolder.getCpidDefault());

						subProjectPath = member.getRelativeFilePath().replace(msetResourcePath+"/"+msetResourceName, "");
						if (!subProjectPath.endsWith("/")) {
							subProjectPath = subProjectPath + "/";
						}
						if (!subProjectPath.startsWith("/")) {
							subProjectPath = "/" + subProjectPath;
						}
						
						logger.info("Setting subProjectPath in MSET ResourceDTO " + subProjectPath);
						mSetResourceDTO.setSubProjectPath(subProjectPath);
						
						for (ResourceDTO resourceDTO : mxsdResourceDTO) {

							String mxsdResourceName = resourceDTO.getResourceName();
							String mxsdVersion = resourceDTO.getVersion();
							
							versionText = versionText+"   "+mxsdResourceName.replace(".mxsd", " Mxsd")+" "+mxsdVersion;
							
							msgSetAttributes.add("File name:::"+mxsdResourceName);
							msgSetAttributes.add("File version:::"+mxsdVersion);						
							
							member = members.get(msetResourcePath+"/"+mxsdResourceName);
							member.checkout(apiSession, mxsdVersion, propertyHolder.getCpidDefault());
							
							logger.info("Setting subProjectPath in MXSD ResourceDTO " + subProjectPath);
							resourceDTO.setSubProjectPath(subProjectPath);
							
						}
						
						logger.info("**** Member resynchronize>>");
						sandbox.resyncMembers(subProjectPath);
						
						updateVersionMSInfo(sandbox.getSandboxDir()+subProjectPath+msetResourcePath+"/"+msetResourceName, versionText);
																						
						File mSetSrcDir = new File(sandbox.getSandboxDir()+subProjectPath+msetResourceFile.getParentFile().getPath());
						FileUtils.copyDirectoryToDirectory(mSetSrcDir, buildDir_MSGSET_File);
						
					}else{
						
						resourceProjectName = msetPIName.replace(".zip", "");
						
						logger.info("**** MSet PI Build Start>>");
				        
						Member member = members.get(msetResourcePath+"/"+msetPIName);
						member.checkout(apiSession, version, propertyHolder.getCpidDefault());

						subProjectPath = member.getRelativeFilePath().replace(msetResourcePath+"/"+msetPIName, "");
						if (!subProjectPath.endsWith("/")) {
							subProjectPath = subProjectPath + "/";
						}
						if (!subProjectPath.startsWith("/")) {
							subProjectPath = "/" + subProjectPath;
						}				        

						logger.info("Setting subProjectPath in MSET ResourceDTO " + subProjectPath);
						mSetResourceDTO.setSubProjectPath(subProjectPath);
						
				        File zipfile = new File(sandbox.getSandboxDir()+subProjectPath+msetResourcePath+"/"+msetPIName);
						
						unzip(zipfile, buildDir_MSGSET_File);
						sandbox.revertMembers(subProjectPath);
						
						updateVersionMSInfo(buildDir_MSGSET_File+"/"+msetPIName.replace(".zip", "")+"/"+msetName+"/"+msetResourceName, versionText);
						
						logger.info("**** MSet PI Build End>>");
						
					}
					
					try{
						String barfileName = constructBarFileName(msetBarName, version);
						String barFileLocation = getBarFileLocation(new File(msetResourcePath));
						String barFileRelativePath = barFileLocation+"/"+barfileName;
						String barFileAbsolutePath = propertyHolder.getSanboxLocation() + subProjectPath + barFileRelativePath;
						File barFile = new File(barFileAbsolutePath);
						boolean barFileExist = barFile.exists();
						
						//Checking if the bar file is already present. If yes, checking out the same
						Member barFileMember = null;
						if(barFileExist){
							barFileMember = members.get(BuildParamReaderUtil.replaceBackwareSlashWithForwardSlash(barFileRelativePath));
							if (barFileMember != null) {
								sandbox.thaw(barFile, barfileName);	
								barFileMember.checkout(apiSession, null, propertyHolder.getCpidDefault());
							}
						}

						String messageSetRefBuildFolderName = resourceProjectName;
						logger.info(messageSetRefBuildFolderName);
						if (!refMsetProjectName.isEmpty()) {
							for (Iterator iterator = refMsetProjectName.iterator(); iterator.hasNext();) {
								messageSetRefBuildFolderName = (String) iterator.next() + " " + messageSetRefBuildFolderName.trim();
							}
						}						
						
						logger.info(messageSetRefBuildFolderName);
						
						//Triggering the build
						AntExecutorUtil.executebuildTask(propertyHolder.getBuildOutputMsgSet(), barFile.getParentFile().getPath(), propertyHolder.getBuildOutput(), barfileName, messageSetRefBuildFolderName, resourceProjectName+"/"+msetName+"/"+msetResourceName, buildScriptFilePath, propertyHolder.getWmbtkPath(), false);
						
						//Checking in the bar file if it was already present, else adding a new member for the created file
						if(barFileExist && barFileMember != null){
							sandbox.checkin(barFile, barfileName, "Checking in barfile..");
						}else{
							sandbox.add(barFile, "Adding new bar file member:"+barfileName);
						}
						
						sandbox.freeze(barFile, barfileName);
						
						//Updating the checked in bar file name and location into PCA
						updateBarFileDetailsInPCA(messageSetDTO.getResourcePCANumber(), barfileName, barFileLocation);
						
						//Constructing the BarFileDTO - Start
						barFileResource = new BarFileResourceDTO();
						barFileResource.setResourceName(barfileName);
						barFileResource.setResourcePath(barFileLocation);
						barFileResource.setDeployEnvironmentDTO(messageSetDTO.getDeployEnvironmentDTO());
						barFileResource.setPcaNumber(messageSetDTO.getResourcePCANumber());
						barFileResource.getBundledFiles().add(mSetResourceDTO);
						barFileResource.getBundledFiles().addAll(mxsdResourceDTO);
						barFileResources.add(barFileResource);
						//Constructing the BarFileDTO - End
						
						msgSetAttributes.add("Bar file name:::"+barfileName);
						msgSetAttributes.add("Bar file location:::"+barFileRelativePath);
						msgSetAttributes.add("Bar file checkin time:::"+formatDate(Calendar.getInstance()));
						
						sandbox.revertMembers(subProjectPath);
						
					}catch(BuildException bex){							
						//Reverting the checkout of members in case of exception 
						logger.error("Exception occured in Build>>>>>"+bex);
						sandbox.revertMembers(subProjectPath);
						throw bex;
					}
				}
				
				//Adding the message Flow attributes to the list
				notificationDTO.getBuildNotificationDTO().getMsgSet().add(msgSetAttributes);
				
			}
			logger.info("******Message Set project build completed******");
		}
	}
	
	/**
	 * Constructs the bar file name based on Application name and version 
	 * @param appname
	 * @param version
	 * @return created bar file name
	 */
	private static String constructBarFileName(String appname, String version){
		String barFileName = null;
		if(appname != null && version != null){
			appname = appname.replace(".esql", "");			
			barFileName = appname+"_V"+version.replace(".", "-")+".bar";
		}
		return barFileName;
	}
	
	/**
	 * Truncates a message flow file extension from a file name
	 * @param fileName
	 * @return truncated file name
	 */
	private static String trucateFileExtension(String fileName){
		return fileName.replace(".msgflow", "");			
	}
	
	/**
	 * Connects to MKS repository
	 * @throws APIException
	 */
	public void connect() throws APIException{
		try {
			apiSession.connect(propertyHolder.getMksHost(), Integer.parseInt(propertyHolder.getMksPort()), propertyHolder.getMksUserName(), propertyHolder.getMksPassword());
		} catch (APIException e) {
			throw e;
		}
	}
	
	/**
	 * Terminates a MKS connection
	 * @throws APIException
	 * @throws IOException
	 */
	public void terminate() throws APIException, IOException{
		apiSession.terminate();
	}
	
	/**
	 * Populate the Project bean with the values for the input MKS Project path
	 * @param projectConfigPath
	 * @throws APIException
	 */
	public void populateProject(String projectConfigPath) throws APIException{
		logger.info("*****Populating project******");
		try {
			project = new Project(apiSession, projectConfigPath);

		} catch (APIException e) {
			throw e;
		}
	}
	
	/**
	 * Create a sandbox for the populated project in the input location
	 * @param sandBoxPath
	 * @throws APIException
	 */
	public void createSandBox(String sandBoxPath) throws APIException{
		logger.info("*****Creating sand box******");
		sandbox = new Sandbox(apiSession, project, sandBoxPath, propertyHolder.getCpidDefault());
		try {
			// create sandbox imports all the files in the specified project to local file system in Read-Only mode
			sandbox.create();
			members = project.listFiles(sandBoxPath);			
			System.out.println("");
		} catch (APIException e) {
			logger.error("Issue in check out.."+e);
			throw e;
		}
	}
	
	/**
	 * Formats the input calendar object to dd/M/yyyy format
	 * @param cal
	 * @return the formated date
	 */
	public static String formatDate(Calendar cal){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
		return sdf.format(cal.getTime()); 
	}

	@Override
	public void run() {
		synchronized (this.getClass()) {
			
			//Clearing the folders
			clearfolders();
			
			String request = this.notificationDTO.getRequest();

			logger.info("REQUEST TYPE>>"+request+" PCA: "+this.notificationDTO.getPca()+", Env: "+this.notificationDTO.getEnvironment());
			
			try {
				//Connecting to MKS server
				connect();
				if("Build".equals(request)){
					build(this.notificationDTO);		
				}else if("Deploy".equals(request)){
					deploy(this.notificationDTO);
				}else if("Build and Deploy".equals(request)){
					buildAndDeploy(this.notificationDTO);
				}
			} catch (APIException e) {
				logger.error(">>Exception in establishing MKS connection.."+e);
				notificationDTO.getExceptionDetails().add(e);
			} finally{
				try {
					//Disconnecting from the MKS server
					terminate();
				} catch (APIException e) {
					logger.error(">>Exception in terminating MKS connection.."+e);
					logger.error(notificationDTO.getExceptionDetails());
					e.printStackTrace();
					notificationDTO.getExceptionDetails().add(e);
				} catch (IOException e) {
					logger.error(">>Exception in terminating MKS connection.."+e);
					logger.error(notificationDTO.getExceptionDetails());
					e.printStackTrace();
					notificationDTO.getExceptionDetails().add(e);
				}
			}
			
			notificationDTO.setProcessFinishTime(formatDate(Calendar.getInstance()));
			
			//Triggering notification mail
			NotificationUtil.triggerMail(notificationDTO, propertyHolder);
			
			NotificationDTO.statusWIP = "false";
		}
	}

	/**
	 * Gets the folder location where bar file will be checked in 
	 * @param resource
	 * @return Bar file folder location
	 */
	private static String getBarFileLocation(File resource){
		
		if(resource != null){
			File parentFile = resource.getParentFile();
			if(parentFile != null && "Deployment".equals(parentFile.getName())){
				return parentFile.getPath()+"\\Bar Files";
			}else{
				if (parentFile == null) {
					return "Bar Files";
				} else {
					return getBarFileLocation(parentFile);
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets the folder location where message flow is checked out 
	 * @param filepath
	 * @param versionText
	 */
	private static void updateVersionMFInfo(String filepath, String versionText) throws Exception{

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);

		Node eClassifiers = doc.getElementsByTagName("eClassifiers").item(0);
		NodeList list = eClassifiers.getChildNodes();

		Boolean vExist = false;
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);

			// get the version element, and update the value
			if ("version".equals(node.getNodeName())) {
				NamedNodeMap attr = node.getAttributes();
				Node nodeAttr = attr.getNamedItem("string");
				nodeAttr.setTextContent(versionText.trim());
				vExist = true;
			}
		}

		if (!vExist) {
			// append a new node
			Element version = doc.createElement("version");
			version.setAttribute("xmi:type", "utility:ConstantString");
			version.setAttribute("string", versionText.trim());
			eClassifiers.appendChild(version);

		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);

		logger.info("Version Update Done Message Flow....");

	}
	
	/**
	 * Gets the folder location where message set is checked out 
	 * @param filepath
	 * @param versionText
	 */
	private static void updateVersionMSInfo(String filepath, String versionText) throws Exception{

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(filepath);

		Node msgCoreModel = doc.getElementsByTagName("msgCoreModel:MRMessageSet").item(0);
   		NamedNodeMap attr = msgCoreModel.getAttributes();
   		Node nodeAttr = attr.getNamedItem("msetVersionNo");
   		nodeAttr.setTextContent(versionText.trim());

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filepath));
		transformer.transform(source, result);

		logger.info("Version Update Done for Message Set....");

	}

	/**
	 * Unzip the file in a directory location
	 * @param zipfile
	 * @param directory
	 */	
    private static void unzip(File zipfile, File directory) throws IOException {
        ZipFile zfile = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> entries = zfile.entries();
        while (entries.hasMoreElements()) {
          ZipEntry entry = entries.nextElement();
          File file = new File(directory, entry.getName());
          if (entry.isDirectory()) {
            file.mkdirs();
          } else {
            file.getParentFile().mkdirs();
            InputStream in = zfile.getInputStream(entry);
            try {
              copy(in, file);
            } finally {
              in.close();
            }
          }
        }
      }

	/**
	 * Copy InputStream to OutputStream
	 * @param in
	 * @param out
	 */	
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
          int readCount = in.read(buffer);
          if (readCount < 0) {
            break;
          }
          out.write(buffer, 0, readCount);
        }
      }

	/**
	 * Copy File to OutputStream
	 * @param file
	 * @param out
	 */	
      @SuppressWarnings("unused")
	private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
          copy(in, out);
        } finally {
          in.close();
        }
      }

  	/**
  	 * Copy OutputStream to File
  	 * @param in
  	 * @param file
  	 */	
    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
          copy(in, out);
        } finally {
          out.close();
        }
      }
}