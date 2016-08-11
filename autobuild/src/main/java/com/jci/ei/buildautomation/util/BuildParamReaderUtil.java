package com.jci.ei.buildautomation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
import com.jci.ei.buildautomation.exception.JCIException;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;

public class BuildParamReaderUtil {
	
	private static Logger logger = Logger.getLogger("JCIBuildAutomation");
	
	/**
	 * Reads the Build and Deploy parameters from PCA and populates the ProjectDTO
	 * @param notificationDTO
	 * @param apiSession
	 * @return populated ProjectDTO bean
	 */
	public static ProjectDTO readBuildAndDeployParam(NotificationDTO notificationDTO, APISession apiSession) {

		ProjectDTO projectDTO = new ProjectDTO();
		
		String pca = notificationDTO.getPca();
		String environment = notificationDTO.getEnvironment();
		
		logger.info("Fetching parent PCA fields for  >>"+pca);
		
		Map<String, String> itemFields = getItemFields(pca, apiSession);
		logger.info(itemFields);
		notificationDTO.setInitialPCAState(itemFields.get("State"));
		
		String deploymentObjects = itemFields.get("EI Deployment Object LIST");
		if(deploymentObjects != null && !"".equalsIgnoreCase(deploymentObjects)){
			String[] childPCAs = deploymentObjects.split(",");
			for(String childPCA : childPCAs){
				logger.info("Fetching child PCA fields for  >>"+childPCA);
				Map<String, String> childPCAFields = getItemFields(childPCA, apiSession);
				logger.info(childPCAFields);
				String resourceType = childPCAFields.get("EI Deployment Object Type");
				String populateParameterFlag = childPCAFields.get("EI (Re)Deploy");
				String referenceParameterFlag = childPCAFields.get("EI Reference Only");
				if("true".equalsIgnoreCase(populateParameterFlag)){
					populateBuildAndDeployParameters(resourceType, childPCA, childPCAFields, environment, projectDTO, apiSession);
				}
				if("true".equalsIgnoreCase(referenceParameterFlag)){
					populateReferenceParameters(resourceType, childPCA, childPCAFields, environment, projectDTO, apiSession);
				}
			}
		}else{
			JCIException jciException = new JCIException("NO_DEPLOYMENT_OBJECT", "No Deployment objects found for PCA "+pca);
			notificationDTO.getExceptionDetails().add(jciException);
			logger.error(jciException.getErrorMessage());
		}
		
		return projectDTO;
	}
	
	/**
	 * Reads the Deploy parameters from PCA and populates the ProjectDTO
	 * @param notificationDTO
	 * @param apiSession
	 * @return populated deployableResourceDTO bean
	 */
	public static DeployableResourceDTO readDeployParam(NotificationDTO notificationDTO, APISession apiSession) {

		DeployableResourceDTO deployableResourceDTO = new DeployableResourceDTO();
		
		String pca = notificationDTO.getPca();
		String environment = notificationDTO.getEnvironment();
		
		logger.info("Fetching parent PCA fields for  >>"+pca);
		
		Map<String, String> itemFields = getItemFields(pca, apiSession);
		notificationDTO.setInitialPCAState(itemFields.get("State"));
		
		String deploymentObjects = itemFields.get("EI Deployment Object LIST");
		if(deploymentObjects != null && !"".equalsIgnoreCase(deploymentObjects)){
			String[] childPCAs = deploymentObjects.split(",");
			for(String childPCA : childPCAs){
				logger.info("Fetching child PCA fields for  >>"+childPCA);
				Map<String, String> childPCAFields = getItemFields(childPCA, apiSession);
				logger.info(childPCAFields);
				String resourceType = childPCAFields.get("EI Deployment Object Type");
				String populateParameterFlag = childPCAFields.get("EI (Re)Deploy");
				if("true".equalsIgnoreCase(populateParameterFlag)){
					populateDeployParameters(resourceType, childPCA, childPCAFields, deployableResourceDTO, environment, apiSession);
				}
			}
		}else{
			JCIException jciException = new JCIException("NO_DEPLOYMENT_OBJECT", "No Deployment objects found for PCA "+pca);
			notificationDTO.getExceptionDetails().add(jciException);
			logger.error(jciException.getErrorMessage());
		}
		
		return deployableResourceDTO;
	}
	
	/**
	 * Populates the Build and Deploy parameters
	 * @param resourceType
	 * @param childPCANumber
	 * @param childPCAFields
	 * @param environment
	 * @param projectDTO
	 * @param apiSession
	 */
	private static void populateBuildAndDeployParameters(String resourceType, String childPCANumber, Map<String, String> childPCAFields, String environment, ProjectDTO projectDTO, APISession apiSession) {
		
		if("Message Flow".equalsIgnoreCase(resourceType)){
			MessageFlowDTO mflow = new MessageFlowDTO();
			String messageFlowResource = childPCAFields.get("EI Message Flow DDN");
			List<String> mFlowResourceSplit = split(messageFlowResource);
			if(mFlowResourceSplit != null && mFlowResourceSplit.size() == 2){
				String resourceName = mFlowResourceSplit.get(1);
				String msgFlowResourcePCA = mFlowResourceSplit.get(0);
				
				//Fetching the location of MSG FLOW resource from PCA
				Map<String, String> msgFlowResourceFields = getItemFields(msgFlowResourcePCA, apiSession);
				String resourcePath = msgFlowResourceFields.get("EI MKS Location").trim().replace("\\", "/");
				if (!resourcePath.endsWith("/")) {
					resourcePath = resourcePath + "/";
				}
				if (resourcePath.startsWith("/")) {
					resourcePath = resourcePath.substring(1);
				}				
				
				ResourceDTO msgFlowResourceDTO = new ResourceDTO();
				msgFlowResourceDTO.setVersion(childPCAFields.get("EI Mflow Version"));
				msgFlowResourceDTO.setResourceName(resourceName+".msgflow");
				msgFlowResourceDTO.setResourcePath(resourcePath);
				mflow.setMsgFlowResourceDTO(msgFlowResourceDTO);
				
				ResourceDTO esqlResourceDTO = new ResourceDTO();
				esqlResourceDTO.setVersion(childPCAFields.get("EI ESQL version"));
				esqlResourceDTO.setResourceName(resourceName+".esql");
				esqlResourceDTO.setResourcePath(resourcePath);
				mflow.setEsqlResourceDTO(esqlResourceDTO);
				
				//Updating the UDP values based on the environment
				List<UserDefinedPropertyDTO> userDefinedPropertyDTOs = extractUDPs(msgFlowResourceFields.get("EI UDP"), environment, apiSession);
				logger.info("Fetching UDP for  >>"+userDefinedPropertyDTOs);
				if(userDefinedPropertyDTOs != null){
					mflow.getUserDefinedPropertyDTOs().addAll(userDefinedPropertyDTOs);
				}
				
				//Updating the deployment environment details
				DeployEnvironmentDTO deploymentBrokerDetails = extractDeploymentBrokerDetails(childPCAFields, environment,	apiSession);
				if(deploymentBrokerDetails != null){
					//mflow.setDeployEnvironmentDTO(getMockDeploymentEnvironmentDTO(environment, deploymentBrokerDetails));
					mflow.setDeployEnvironmentDTO(deploymentBrokerDetails);
				}
				
				mflow.setResourcePCANumber(childPCANumber);
				projectDTO.getMessageFlowDTOList().add(mflow);
			}
			
			 
		}else if("Message Set".equalsIgnoreCase(resourceType)){
			MessageSetDTO messageSetDTO = new MessageSetDTO();
			String messageSetResource = childPCAFields.get("EI Message Set DDN");
			List<String> mSetResourceSplit = split(messageSetResource);
			if(mSetResourceSplit != null && mSetResourceSplit.size() == 2){
				String msgSetResourcePCA = mSetResourceSplit.get(0);
				Map<String, String> msetFieldDetails = getItemFields(msgSetResourcePCA, apiSession);
				
				logger.info("Fetching MSet Details  >>"+msetFieldDetails);
				
				ResourceDTO messageSetResourceDTO = new ResourceDTO();
				messageSetResourceDTO.setVersion(childPCAFields.get("EI Mset Version"));
				
				if (msetFieldDetails.get("EI mset name") == null) {
					messageSetResourceDTO.setResourceName("messageSet.mset");
				} else {
					messageSetResourceDTO.setResourceName(msetFieldDetails.get("EI mset name").trim());
				}
				
				String mxsdPath = replaceBackwareSlashWithForwardSlash(msetFieldDetails.get("EI MKS Location").trim()+"/"+msetFieldDetails.get("EI Message Set Name").trim()); 
				messageSetResourceDTO.setResourcePath(mxsdPath);

				if (msetFieldDetails.get("EI Project Interchange") == null) {
					messageSetResourceDTO.setPIName("");
					messageSetResourceDTO.setMessageSetName(msetFieldDetails.get("EI Message Set Name").trim());
				} else {
					messageSetResourceDTO.setPIName(msetFieldDetails.get("EI Project Interchange").trim());
					messageSetResourceDTO.setResourcePath(msetFieldDetails.get("EI MKS Location").trim().replace("\\", "/"));
					messageSetResourceDTO.setMessageSetName(msetFieldDetails.get("EI Message Set Name").trim());
				}				

				messageSetDTO.setMsetResource(messageSetResourceDTO);
				
				String mxsdResource = childPCAFields.get("EI Mxsds");
				if (mxsdResource.trim().length() > 0) {
					String[] mxsdPCANumbers = mxsdResource.split(",");
					if(mxsdPCANumbers != null && mxsdPCANumbers.length >0){
						for(String mxsdPCANumber : mxsdPCANumbers){
							Map<String, String> mxsdItemFields = getItemFields(mxsdPCANumber, apiSession);
							ResourceDTO mxsdResourceDTO = new ResourceDTO();
							mxsdResourceDTO.setVersion(mxsdItemFields.get("EI Mxsd Version"));
							mxsdResourceDTO.setResourceName(mxsdItemFields.get("EI Mxsd Name").trim()+".mxsd");
							mxsdResourceDTO.setResourcePath(mxsdPath);
							messageSetDTO.getMxsdResources().add(mxsdResourceDTO);
						}
					}
				}
				
				//Updating the deployment environment details
				DeployEnvironmentDTO deploymentBrokerDetails = extractDeploymentBrokerDetails(childPCAFields, environment, apiSession);
				if(deploymentBrokerDetails != null){
					//messageSetDTO.setDeployEnvironmentDTO(getMockDeploymentEnvironmentDTO(environment, deploymentBrokerDetails));
					messageSetDTO.setDeployEnvironmentDTO(deploymentBrokerDetails);
				}
				messageSetDTO.setResourcePCANumber(childPCANumber);
				
				projectDTO.getMessageSetDTOList().add(messageSetDTO);
			}
		} else if ("Queue and Subs Script".equalsIgnoreCase(resourceType)) {
			MQDefinitionDTO mqDefDTO = new MQDefinitionDTO();
			
			mqDefDTO.setVersion(childPCAFields.get("EI Version Number"));
			String resourcePath = childPCAFields.get("EI MKS Location").trim().replace("\\", "/");
			if (!resourcePath.endsWith("/")) {
				resourcePath = resourcePath + "/";
			}
			if (resourcePath.startsWith("/")) {
				resourcePath = resourcePath.substring(1);
			}
			mqDefDTO.setResourcePath(resourcePath);
			mqDefDTO.setResourceName(childPCAFields.get("EI Qdef & Subs Name").trim());
			
			//Updating the deployment environment details
			DeployEnvironmentDTO deploymentQMgrDetails = extractQMgrDetails(childPCAFields, environment, apiSession);
			if(deploymentQMgrDetails != null){
				//mqDefDTO.setDeployEnvironmentDTO(getMockDeploymentEnvironmentDTO("QA", deploymentQMgrDetails));
				mqDefDTO.setDeployEnvironmentDTO(deploymentQMgrDetails);
				
				mqDefDTO.setResourcePCANumber(childPCANumber);

				projectDTO.getMQDefinitionDTOList().add(mqDefDTO);
			}
		}
	}
	
	/**
	 * Populates the Reference parameters
	 * @param resourceType
	 * @param childPCANumber
	 * @param childPCAFields
	 * @param environment
	 * @param projectDTO
	 * @param apiSession
	 */
	private static void populateReferenceParameters(String resourceType, String childPCANumber, Map<String, String> childPCAFields, String environment, ProjectDTO projectDTO, APISession apiSession) {
		
		if("Message Flow".equalsIgnoreCase(resourceType)){
			MessageFlowDTO mflow = new MessageFlowDTO();
			String messageFlowResource = childPCAFields.get("EI Message Flow DDN").trim();
			List<String> mFlowResourceSplit = split(messageFlowResource);
			if(mFlowResourceSplit != null && mFlowResourceSplit.size() == 2){
				String resourceName = mFlowResourceSplit.get(1);
				String msgFlowResourcePCA = mFlowResourceSplit.get(0);
				
				//Fetching the location of MSG FLOW resource from PCA
				Map<String, String> msgFlowResourceFields = getItemFields(msgFlowResourcePCA, apiSession);
				String resourcePath = msgFlowResourceFields.get("EI MKS Location").trim().replace("\\", "/");
				if (!resourcePath.endsWith("/")) {
					resourcePath = resourcePath + "/";
				}
				if (resourcePath.startsWith("/")) {
					resourcePath = resourcePath.substring(1);
				}				
				
				ResourceDTO msgFlowResourceDTO = new ResourceDTO();
				msgFlowResourceDTO.setVersion(childPCAFields.get("EI Mflow Version"));
				msgFlowResourceDTO.setResourceName(resourceName+".msgflow");
				msgFlowResourceDTO.setResourcePath(resourcePath);
				mflow.setMsgFlowResourceDTO(msgFlowResourceDTO);
				
				ResourceDTO esqlResourceDTO = new ResourceDTO();
				esqlResourceDTO.setVersion(childPCAFields.get("EI ESQL version"));
				esqlResourceDTO.setResourceName(resourceName+".esql");
				esqlResourceDTO.setResourcePath(resourcePath);
				mflow.setEsqlResourceDTO(esqlResourceDTO);
				
				//Updating the UDP values based on the environment 
				List<UserDefinedPropertyDTO> userDefinedPropertyDTOs = extractUDPs(msgFlowResourceFields.get("EI UDP"), environment, apiSession);
				if(userDefinedPropertyDTOs != null){
					mflow.getUserDefinedPropertyDTOs().addAll(userDefinedPropertyDTOs);
				}
				
				mflow.setResourcePCANumber(childPCANumber);
				projectDTO.getRefMessageFlowDTOList().add(mflow);
			}
			
			 
		}else if("Message Set".equalsIgnoreCase(resourceType)){
			MessageSetDTO messageSetDTO = new MessageSetDTO();
			String messageSetResource = childPCAFields.get("EI Message Set DDN").trim();
			List<String> mSetResourceSplit = split(messageSetResource);
			if(mSetResourceSplit != null && mSetResourceSplit.size() == 2){
				String msgSetResourcePCA = mSetResourceSplit.get(0);
				Map<String, String> msetFieldDetails = getItemFields(msgSetResourcePCA, apiSession);
				ResourceDTO messageSetResourceDTO = new ResourceDTO();
				messageSetResourceDTO.setVersion(childPCAFields.get("EI Mset Version"));
				
				if (msetFieldDetails.get("EI mset name") == null) {
					messageSetResourceDTO.setResourceName("messageSet.mset");
				} else {
					messageSetResourceDTO.setResourceName(msetFieldDetails.get("EI mset name").trim());
				}
				
				String mxsdPath = replaceBackwareSlashWithForwardSlash(msetFieldDetails.get("EI MKS Location").trim()+"/"+msetFieldDetails.get("EI Message Set Name").trim()); 
				messageSetResourceDTO.setResourcePath(mxsdPath);

				if (msetFieldDetails.get("EI Project Interchange") == null) {
					messageSetResourceDTO.setPIName("");
					messageSetResourceDTO.setMessageSetName(msetFieldDetails.get("EI Message Set Name").trim());
				} else {
					messageSetResourceDTO.setPIName(msetFieldDetails.get("EI Project Interchange").trim());
					messageSetResourceDTO.setResourcePath(msetFieldDetails.get("EI MKS Location").trim().replace("\\", "/"));
					messageSetResourceDTO.setMessageSetName(msetFieldDetails.get("EI Message Set Name").trim());
				}				
				
				messageSetDTO.setMsetResource(messageSetResourceDTO);
				
				String mxsdResource = childPCAFields.get("EI Mxsds");
				if (mxsdResource.trim().length() > 0) {
					String[] mxsdPCANumbers = mxsdResource.split(",");
					if(mxsdPCANumbers != null && mxsdPCANumbers.length >0){
						for(String mxsdPCANumber : mxsdPCANumbers){
							Map<String, String> mxsdItemFields = getItemFields(mxsdPCANumber, apiSession);
							ResourceDTO mxsdResourceDTO = new ResourceDTO();
							mxsdResourceDTO.setVersion(mxsdItemFields.get("EI Mxsd Version"));
							mxsdResourceDTO.setResourceName(mxsdItemFields.get("EI Mxsd Name").trim()+".mxsd");
							mxsdResourceDTO.setResourcePath(mxsdPath);
							messageSetDTO.getMxsdResources().add(mxsdResourceDTO);
						}
					}
				}
				
				//Updating the deployment environment details
				DeployEnvironmentDTO deploymentBrokerDetails = extractDeploymentBrokerDetails(childPCAFields, environment, apiSession);
				if(deploymentBrokerDetails != null){
					//messageSetDTO.setDeployEnvironmentDTO(getMockDeploymentEnvironmentDTO(environment, deploymentBrokerDetails));
					messageSetDTO.setDeployEnvironmentDTO(deploymentBrokerDetails);
				}
				messageSetDTO.setResourcePCANumber(childPCANumber);
				
				projectDTO.getRefMessageSetDTOList().add(messageSetDTO);
			}
		}
	}	
	
	/**
	 * Populates the Deploy parameters 
	 * @param resourceType
	 * @param childPCANumber
	 * @param childPCAFields
	 * @param deployableResourceDTO
	 * @param environment
	 * @param apiSession
	 */
	private static void populateDeployParameters(String resourceType, String childPCANumber, Map<String, String> childPCAFields, DeployableResourceDTO deployableResourceDTO, String environment, APISession apiSession) {
		
		if("Message Flow".equalsIgnoreCase(resourceType)){
			
			String messageFlowResource = childPCAFields.get("EI Message Flow DDN");
			List<String> mFlowResourceSplit = split(messageFlowResource);
			if(mFlowResourceSplit != null && mFlowResourceSplit.size() == 2){
				BarFileResourceDTO barFileResourceDTO = new BarFileResourceDTO();
				String resourceName = mFlowResourceSplit.get(1);
				barFileResourceDTO.setFlowName(resourceName);
				String msgFlowResourcePCA = mFlowResourceSplit.get(0);
				
				//Fetching the location of MSG FLOW resource from PCA
				Map<String, String> msgFlowResourceFields = getItemFields(msgFlowResourcePCA, apiSession);
				String resourcePath = msgFlowResourceFields.get("EI MKS Location").trim().replace("\\", "/");
				if (!resourcePath.endsWith("/")) {
					resourcePath = resourcePath + "/";
				}
				if (resourcePath.startsWith("/")) {
					resourcePath = resourcePath.substring(1);
				}			
				
				ResourceDTO msgFlowResourceDTO = new ResourceDTO();
				msgFlowResourceDTO.setVersion(childPCAFields.get("EI Mflow Version"));
				msgFlowResourceDTO.setResourceName(resourceName+".msgflow");
				msgFlowResourceDTO.setResourcePath(resourcePath);
				barFileResourceDTO.getBundledFiles().add(msgFlowResourceDTO);
				
				ResourceDTO esqlResourceDTO = new ResourceDTO();
				esqlResourceDTO.setVersion(childPCAFields.get("EI ESQL version"));
				esqlResourceDTO.setResourceName(resourceName+".esql");
				esqlResourceDTO.setResourcePath(resourcePath);
				barFileResourceDTO.getBundledFiles().add(esqlResourceDTO);
				
				//Updating the UDP values based on the environment				
				List<UserDefinedPropertyDTO> userDefinedPropertyDTOs = extractUDPs(msgFlowResourceFields.get("EI UDP"), environment, apiSession);
				if(userDefinedPropertyDTOs != null){
					barFileResourceDTO.setUserDefinedPropertyDTOs(userDefinedPropertyDTOs);
				}
				
				//Updating the deployment environment details
				DeployEnvironmentDTO deploymentBrokerDetails = extractDeploymentBrokerDetails(childPCAFields, environment,	apiSession);
				
				//barFileResourceDTO.setDeployEnvironmentDTO(getMockDeploymentEnvironmentDTO(environment, deploymentBrokerDetails));
				barFileResourceDTO.setDeployEnvironmentDTO(deploymentBrokerDetails);
				
				barFileResourceDTO.setResourceName(childPCAFields.get("EI Create Bar Filename"));
				String barResourcePath = childPCAFields.get("EI Bar File MKS Location").trim().replace("\\", "/");
				if (!barResourcePath.endsWith("/")) {
					barResourcePath = barResourcePath + "/";
				}
				if (barResourcePath.startsWith("/")) {
					barResourcePath = barResourcePath.substring(1);
				}				
				barFileResourceDTO.setResourcePath(barResourcePath);
				
				barFileResourceDTO.setPcaNumber(childPCANumber);
				deployableResourceDTO.getBarFileResourceDTOList().add(barFileResourceDTO);
			}
			
		}else if("Message Set".equalsIgnoreCase(resourceType)){
				
			BarFileResourceDTO barFileResourceDTO = new BarFileResourceDTO();
			
			String messageSetResource = childPCAFields.get("EI Message Set DDN");
			List<String> mSetResourceSplit = split(messageSetResource);
			if(mSetResourceSplit != null && mSetResourceSplit.size() == 2){
				String msgSetResourcePCA = mSetResourceSplit.get(0);
				Map<String, String> msetFieldDetails = getItemFields(msgSetResourcePCA, apiSession);
				ResourceDTO messageSetResourceDTO = new ResourceDTO();
				messageSetResourceDTO.setVersion(childPCAFields.get("EI Mset Version"));
				
				if (msetFieldDetails.get("EI mset name") == null) {
					messageSetResourceDTO.setResourceName("messageSet.mset");
				} else {
					messageSetResourceDTO.setResourceName(msetFieldDetails.get("EI mset name").trim());
				}
				
				String mxsdPath = replaceBackwareSlashWithForwardSlash(msetFieldDetails.get("EI MKS Location").trim()+"/"+msetFieldDetails.get("EI Message Set Name").trim()); 
				messageSetResourceDTO.setResourcePath(mxsdPath);
				
				if (msetFieldDetails.get("EI Project Interchange") == null) {
					messageSetResourceDTO.setPIName("");
					messageSetResourceDTO.setMessageSetName(msetFieldDetails.get("EI Message Set Name").trim());
				} else {
					messageSetResourceDTO.setPIName(msetFieldDetails.get("EI Project Interchange").trim());
					messageSetResourceDTO.setResourcePath(msetFieldDetails.get("EI MKS Location").trim().replace("\\", "/"));
					messageSetResourceDTO.setMessageSetName(msetFieldDetails.get("EI Message Set Name").trim());
				}				
				
				barFileResourceDTO.getBundledFiles().add(messageSetResourceDTO);
				
				String mxsdResource = childPCAFields.get("EI Mxsds");
				if (mxsdResource.trim().length() > 0) {
					String[] mxsdPCANumbers = mxsdResource.split(",");
					if(mxsdPCANumbers != null && mxsdPCANumbers.length >0){
						for(String mxsdPCANumber : mxsdPCANumbers){
							Map<String, String> mxsdItemFields = getItemFields(mxsdPCANumber, apiSession);
							ResourceDTO mxsdResourceDTO = new ResourceDTO();
							mxsdResourceDTO.setVersion(mxsdItemFields.get("EI Mxsd Version"));
							mxsdResourceDTO.setResourceName(mxsdItemFields.get("EI Mxsd Name").trim()+".mxsd");
							mxsdResourceDTO.setResourcePath(mxsdPath);
							barFileResourceDTO.getBundledFiles().add(mxsdResourceDTO);
						}
					}
				}
				
				//Updating the deployment environment details
				DeployEnvironmentDTO deploymentBrokerDetails = extractDeploymentBrokerDetails(childPCAFields, environment,	apiSession);
				
				//barFileResourceDTO.setDeployEnvironmentDTO(getMockDeploymentEnvironmentDTO(environment, deploymentBrokerDetails));
				barFileResourceDTO.setDeployEnvironmentDTO(deploymentBrokerDetails);
				
				barFileResourceDTO.setResourceName(childPCAFields.get("EI Create Bar Filename"));
				String barResourcePath = childPCAFields.get("EI Bar File MKS Location").trim().replace("\\", "/");
				if (!barResourcePath.endsWith("/")) {
					barResourcePath = barResourcePath + "/";
				}
				if (barResourcePath.startsWith("/")) {
					barResourcePath = barResourcePath.substring(1);
				}				
				barFileResourceDTO.setResourcePath(barResourcePath);
				barFileResourceDTO.setPcaNumber(childPCANumber);
				deployableResourceDTO.getBarFileResourceDTOList().add(barFileResourceDTO);
			}
			
		} else if ("Queue and Subs Script".equalsIgnoreCase(resourceType)) {
			MQDefinitionDTO mqDefDTO = new MQDefinitionDTO();
			
			mqDefDTO.setVersion(childPCAFields.get("EI Version Number"));
			String resourcePath = childPCAFields.get("EI MKS Location").trim().replace("\\", "/");
			if (!resourcePath.endsWith("/")) {
				resourcePath = resourcePath + "/";
			}
			if (resourcePath.startsWith("/")) {
				resourcePath = resourcePath.substring(1);
			}			
			mqDefDTO.setResourcePath(resourcePath);
			mqDefDTO.setResourceName(childPCAFields.get("EI Qdef & Subs Name").trim());
			
			//Updating the deployment environment details
			DeployEnvironmentDTO deploymentQMgrDetails = extractQMgrDetails(childPCAFields, environment,	apiSession);
			if(deploymentQMgrDetails != null){
				//mqDefDTO.setDeployEnvironmentDTO(getMockDeploymentEnvironmentDTO("QA", deploymentQMgrDetails));
				mqDefDTO.setDeployEnvironmentDTO(deploymentQMgrDetails);
				mqDefDTO.setResourcePCANumber(childPCANumber);
				
				deployableResourceDTO.getMQDefinitionDTOList().add(mqDefDTO);
			}
		}
	}

	/**
	 * Replace Backward Slash With Forward Slash 
	 * @param input
	 * @return String
	 */
	public static String replaceBackwareSlashWithForwardSlash(String input){
		input = input.replace("\\", "/");
		return input;
	}
	
	/**
	 * Extracts the Deployment Broker details from PCA and populates the DeployEnvironmentDTO bean
	 * @param childPCAFields
	 * @param environment
	 * @param apiSession
	 * @return populated DeployEnvironmentDTO bean
	 */
	private static DeployEnvironmentDTO extractDeploymentBrokerDetails(
			Map<String, String> childPCAFields, String environment,	APISession apiSession) {
		DeployEnvironmentDTO deployEnvironmentDTO = null;
		List<String> deploymentBrokerSplit = split(childPCAFields.get(PCAUtil.getDeploymentBrokerKey(environment)));
		if(deploymentBrokerSplit != null && deploymentBrokerSplit.size() == 2){
			deployEnvironmentDTO = new DeployEnvironmentDTO();
			String brokerPCA = deploymentBrokerSplit.get(0);
			Map<String, String> brokerFields = getItemFields(brokerPCA, apiSession);
			
			deployEnvironmentDTO.setBrokerName(brokerFields.get("EI Broker Name").trim());
			deployEnvironmentDTO.setExecutionGroup(childPCAFields.get(PCAUtil.getExecutionGrpKey(environment)));
			List<String> queueManagerSplit = split(brokerFields.get("EI QMGR DDN"));
			if(queueManagerSplit != null && queueManagerSplit.size() == 2){
				String queueManagerPCA = queueManagerSplit.get(0);
				
				//Fetching the queue manager details from PCA 
				Map<String, String> queueManagerFields = getItemFields(queueManagerPCA, apiSession);
				deployEnvironmentDTO.setQueueManager(queueManagerFields.get("EI QMGR Name").trim());
				deployEnvironmentDTO.setPort(queueManagerFields.get("EI Listener Port").trim());
				deployEnvironmentDTO.setHost(queueManagerFields.get("EI Server DDN").trim());
				deployEnvironmentDTO.setEnvironment(environment);
			}
		}
		return deployEnvironmentDTO;
	}

	/**
	 * Extracts the Queue Manager details from PCA and populates the DeployEnvironmentDTO bean
	 * @param childPCAFields
	 * @param environment
	 * @param apiSession
	 * @return populated DeployEnvironmentDTO bean
	 */
	private static DeployEnvironmentDTO extractQMgrDetails(
			Map<String, String> childPCAFields, String environment,	APISession apiSession) {
		
		DeployEnvironmentDTO deployEnvironmentDTO = new DeployEnvironmentDTO();
			
		List<String> queueManagerSplit = split(childPCAFields.get(PCAUtil.getQueueManagerKeyinPCA(environment)));
		
		if(queueManagerSplit != null && queueManagerSplit.size() == 2){
			String queueManagerPCA = queueManagerSplit.get(0);
				
			//Fetching the queue manager details from PCA 
			logger.info("Fetching Queue Manager PCA fields for  >>"+queueManagerPCA);
			Map<String, String> queueManagerFields = getItemFields(queueManagerPCA, apiSession);
			logger.info(queueManagerFields);
				
			deployEnvironmentDTO.setQueueManager(queueManagerFields.get("EI QMGR Name").trim());
			deployEnvironmentDTO.setPort(queueManagerFields.get("EI Listener Port").trim());
			deployEnvironmentDTO.setHost(queueManagerFields.get("EI Server DDN").trim());
			deployEnvironmentDTO.setEnvironment(environment);
			deployEnvironmentDTO.setSvrConnChannel(queueManagerFields.get("EI SVRCONN Channel Name").trim());
		}
		return deployEnvironmentDTO;
	}	
	
	/**
	 * Extracts UDP details from PCA and populates the UserDefinedPropertyDTO bean list
	 * @param udpPCAs
	 * @param environment
	 * @param apiSession
	 * @return List of UserDefinedPropertyDTO beans
	 */
	private static List<UserDefinedPropertyDTO> extractUDPs(String udpPCAs, String environment, APISession apiSession) {
		List<UserDefinedPropertyDTO> userDefinedPropertyDTOs = null;
		String[] udpPCANumbers = udpPCAs.split(",");
		if(udpPCANumbers != null && udpPCANumbers.length >0){
			userDefinedPropertyDTOs = new ArrayList<UserDefinedPropertyDTO>();
			for(String udpPCA : udpPCANumbers){
				if(udpPCA != null && !udpPCA.isEmpty()){
					Map<String, String> udpFields = getItemFields(udpPCA, apiSession);
					logger.info("Fetching UDP fields >>"+udpFields);
					if(udpFields != null && !udpFields.isEmpty()){
						String udpValue = udpFields.get(PCAUtil.getUDPKey(environment));
						if(udpValue != null && !"".equalsIgnoreCase(udpValue)){
							UserDefinedPropertyDTO udp = new UserDefinedPropertyDTO();
							String udpName = udpFields.get("EI Property name").trim();
							String udpNodeName = udpFields.get("EI Node Name");
							if (udpNodeName != null) {
								udpNodeName = udpNodeName.trim();
							}
							if(udpNodeName != null && !"".equalsIgnoreCase(udpNodeName)){
								udpName = udpNodeName+"."+udpName;
							}
							udp.setName(udpName);
							udp.setValue(udpValue);
							userDefinedPropertyDTOs.add(udp);
						}
					}					
				}
			}
		}
		return userDefinedPropertyDTOs;
	}

	/**
	 * Splits the resource string into 2 values based on delimited | and returns the values as List 
	 * @param resource
	 * @return constructed list
	 */
	public static List<String> split(String resource) {
		
		List<String> messageFlowSplit = null;
		if(resource != null){
			int indexOf = resource.indexOf("|");
			if(indexOf != -1){
				messageFlowSplit = new ArrayList<String>();
				messageFlowSplit.add(resource.substring(0, indexOf).trim());
				messageFlowSplit.add(resource.substring(indexOf+1, resource.length()).trim());
			}
		}
		return messageFlowSplit;
	}

	/**
     * Gets the attributes attached to a PCA number and construct the attribute as a key,value pair map
     * @param pcaNumber
     * @return Map of Field name and Field value
     */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getItemFields(String pcaNumber, APISession api) {
		Command cmd = new Command();
		cmd.setApp(Command.IM);
		cmd.setCommandName("viewissue");
		cmd.addSelection(pcaNumber);
		Map<String, String> fields = new HashMap<String, String>();
		try {
			Response response = api.runCommand(cmd);
			if (response != null) {
			WorkItemIterator wii = response.getWorkItems();
				while (wii.hasNext()) {
				    WorkItem wi = wii.next();
				    Iterator<Field> iterator = wi.getFields();
			        while (iterator.hasNext()) {
				        Field field = iterator.next();
				        fields.put(field.getName(), field.getValueAsString());
			        }
				}
			}
		} catch (APIException e) {
			logger.error(e);
		}
		return fields;
	}
	
	/**
     * Gets the list of Issue for a query
     * @param queryDefinition
     * @return Map of Field name and Field value
     */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getPCAItem(APISession api, String queryDefinition) {
		
		Command cmd = new Command();
		cmd.setApp(Command.IM);
		cmd.setCommandName("issues");
		cmd.addOption( new Option( "queryDefinition", queryDefinition ));
		
		Map<String, String> pcaItems = new HashMap<String, String>();
		try {
			Response response = api.runCommand(cmd);
			if (response != null) {
			WorkItemIterator wii = response.getWorkItems();
				while (wii.hasNext()) {
				    WorkItem wi = wii.next();
				    Iterator<Field> iterator = wi.getFields();
			        while (iterator.hasNext()) {
				        Field field = iterator.next();
				        pcaItems.put(field.getName(), field.getValueAsString());
			        }
				}
			}
		} catch (APIException e) {
			logger.error(e);
		}
		
		return pcaItems;
	}		
}