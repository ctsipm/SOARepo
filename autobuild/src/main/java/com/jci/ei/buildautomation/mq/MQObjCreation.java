package com.jci.ei.buildautomation.mq;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.*;
import com.jci.ei.buildautomation.dto.NotificationDTO;
import com.jci.ei.buildautomation.util.APISession;
import com.jci.ei.buildautomation.util.BuildParamReaderUtil;
import com.jci.ei.buildautomation.util.PCAUtil;

public class MQObjCreation {

	private static Logger logger = Logger.getLogger("JCIBuildAutomation");
	
	/**
	 * Connects to MQ Queue Manager
	 * @param paramQMName
	 * @param paramHostName
	 * @param paramPortNo
	 * @param paramChannelName
	 * @param notificationDTO
	 * @return MQQueueManager
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MQQueueManager connectMQ(String paramQMName, String paramHostName,
			String paramPortNo, String paramChannelName,
			NotificationDTO notificationDTO) {

		MQQueueManager localMQQueueManager = null;

		Hashtable localHashtable = new Hashtable();
		localHashtable.put("channel", paramChannelName);
		localHashtable.put("port", new Integer(paramPortNo));
		localHashtable.put("hostname", paramHostName);

		if (paramHostName.equalsIgnoreCase("LOCALHOST")) {
			MQEnvironment.hostname = null;
			MQEnvironment.channel = null;
			try {
				localMQQueueManager = new MQQueueManager(paramQMName);
			} catch (MQException e) {
				logger.error(e);
				notificationDTO.getExceptionDetails().add(e);
			}
		} else {
			try {
				localMQQueueManager = new MQQueueManager(paramQMName,
						localHashtable);
			} catch (MQException e) {
				logger.error(e);
				notificationDTO.getExceptionDetails().add(e);
			}
		}
		return localMQQueueManager;
	}

	/**
	 * Executes MQSC Script
	 * @param paramMQQueueManager
	 * @param mqscScript
	 * @return result of MQSC Script execution
	 */
	@SuppressWarnings("unused")
	public String executeMQSCScript(MQQueueManager paramMQQueueManager,
			String paramString) {

		String str = null;
		MQQueue localMQQueue1 = null;
		MQQueue localMQQueue2 = null;
		MQPutMessageOptions localMQPutMessageOptions = new MQPutMessageOptions();
		MQGetMessageOptions localMQGetMessageOptions = new MQGetMessageOptions();

		MQMessage localMQMessage = new MQMessage();

		try {
			localMQQueue1 = paramMQQueueManager.accessQueue(paramMQQueueManager
					.getCommandInputQueueName(), 16);
			localMQQueue2 = paramMQQueueManager.accessQueue(
					"SYSTEM.DEFAULT.MODEL.QUEUE", 4, null, "WMQTOOL.*", null);

			localMQMessage.messageType = 1;
			localMQMessage.format = "MQADMIN ";
			localMQMessage.expiry = 100;
			localMQMessage.feedback = 0;
			localMQMessage.replyToQueueName = localMQQueue2.getName();

			MQCFH.write(localMQMessage, 38, 2);
			MQCFIN.write(localMQMessage, 1017, 1);
			MQCFST.write(localMQMessage, 3014, paramString);

			localMQQueue1.put(localMQMessage, localMQPutMessageOptions);
			localMQGetMessageOptions.options = 16385;
			localMQGetMessageOptions.waitInterval = 10000;
			localMQMessage.messageId = MQConstants.MQMI_NONE;

			localMQQueue2.get(localMQMessage, localMQGetMessageOptions);

			try {
				if (localMQQueue2 != null) {
					localMQQueue2.close();
				}
				if (localMQQueue1 != null) {
					localMQQueue1.close();
				}
			} catch (MQException localMQException1) {
				logger.error(localMQException1);
			}

			if (localMQMessage == null) {
				return ("No response message from queue manager. Please check mq log for details");
			}
		}

		catch (MQException localMQException2) {
			logger.error(localMQException2);
		} catch (IOException localIOException1) {
			logger.error(localIOException1);
		} finally {
			try {
				if (localMQQueue2 != null) {
					localMQQueue2.close();
				}
				if (localMQQueue1 != null) {
					localMQQueue1.close();
				}
			} catch (MQException localMQException6) {
				logger.error(localMQException6);
			}
		}

		MQCFH localMQCFH = null;

		try {
			localMQCFH = new MQCFH(localMQMessage);
			if (localMQCFH != null) {
				StringBuffer localStringBuffer = new StringBuffer();
				for (int i = 0; i < localMQCFH.parameterCount; i++) {
					PCFParameter localPCFParameter = PCFParameter
							.nextParameter(localMQMessage);
					str = findText(localPCFParameter);
					if (str != null) {
						localStringBuffer.append(str + "\n");
					}
				}
				str = new String(localStringBuffer);
			}
		} catch (MQException localMQException5) {
			logger.error(localMQException5);
		} catch (IOException localIOException2) {
			logger.error(localIOException2);
		}
		return str;
	}

	/**
	 * Finds a specific text
	 * @param paramPCFParameter
	 * @return result
	 */	
	public String findText(PCFParameter paramPCFParameter) {
		String str = null;
		if (paramPCFParameter.getParameter() == 3014) {
			str = paramPCFParameter.getStringValue();
		}
		return str;
	}

	/**
	 * Retrieve Corresponding QueueManager Name and PCA Number for DEV QueueManager from PCA
	 * @param apiSession
	 * @param notificationDTO
	 * @param sourceVal
	 * @return Map<String, String>
	 * @throws SQLException
	 */
	public Map<String, String> getCorrQmgrDetailsForDev(APISession apiSession, String sourceVal) throws SQLException
	{	
		
		/* Commented out by: JBOSIKO, July 8, 2015 - the following query seems to be referencing the field
		 * "EI Environment" instead of "Environment". That's why it does not find anything for dev as the 
		 * field query for item named "ID" just returns null.
		 * 
        String queryDefinition = "((field[\"Type\"]=\"EI QMGR\")and(field[\"State\"]=\"Active\")and" +
        							"(field[\"EI Environment\"]=\"DEV\")and(field[\"EI QMGR Name\"]contains\" + " +
        							sourceVal + "\"))";
        */
        
        String queryDefinition = "((field[\"Type\"]=\"EI QMGR\")and(field[\"State\"]=\"Active\")and" +
				"(field[\"EI Environment\"]=\"DEV\")and(field[\"EI QMGR Name\"]contains\"" +
				sourceVal + "\"))";

        logger.info("  [D] Query definition is ["+queryDefinition+"]");
        Map<String, String> pcaItems = BuildParamReaderUtil.getPCAItem(apiSession, queryDefinition);
        logger.info("  [D] # of keys from PCA: "+Integer.toString(pcaItems.size()));
        for (String keyn : pcaItems.keySet())
        {
        	logger.info("  [D] field key: "+keyn);
        }
        
        String pcaNum = pcaItems.get("ID");
        
        logger.info("Getting Queue Manager PCA Details for PCA: " + pcaNum);
		Map<String, String> queueManagerFields = BuildParamReaderUtil.getItemFields(pcaNum, apiSession);        
            
        return queueManagerFields;
	}	
	
	/**
	 * Retrieve Corresponding QueueManager Name for DEV QueueManager from PCA
	 * @param apiSession
	 * @param notificationDTO
	 * @param sourceVal
	 * @return String
	 * @throws SQLException
	 */
	public String getCorrQmgrNameForDev(APISession apiSession, NotificationDTO notificationDTO, String sourceVal) throws SQLException
	{	

        String environment = notificationDTO.getEnvironment();
        String targetField = PCAUtil.getQueueManagerKeyinPCA(environment);

		Map<String, String> queueManagerFields = getCorrQmgrDetailsForDev(apiSession, sourceVal);
		List<String> queueManagerSplit = BuildParamReaderUtil.split(queueManagerFields.get(targetField));
		String targetvalue = queueManagerSplit.get(1);
		
       	if (targetvalue != null) {
       		logger.info("Modifying MQ script by replacing DEV QMGR: " + sourceVal + " From PCA by " + environment + "QMGR: " + targetvalue);
		} else {
				// Throw exception if value is not present in PCA
		}
            
        return targetvalue;
	}

	/**
	 * Retrieve Channel Connection Name from PCA
	 * @param notificationDTO
	 * @param sourceVal
	 * @param apiSession
	 * @return String
	 * @throws SQLException 
	 */	
	public String getChannelConnName(NotificationDTO notificationDTO, String sourceVal, APISession apiSession) throws SQLException
	{	
		String connName = null;
        String environment = notificationDTO.getEnvironment();
        String targetField = PCAUtil.getQueueManagerKeyinPCA(environment);		
		
		Map<String, String> queueManagerFields = getCorrQmgrDetailsForDev(apiSession, sourceVal);
		List<String> queueManagerSplit = BuildParamReaderUtil.split(queueManagerFields.get(targetField));
		String qmgrPCA = queueManagerSplit.get(0);		

       	if (qmgrPCA != null) {
       		logger.info("Getting Queue Manager PCA Details for PCA: " + qmgrPCA);
       		Map<String, String> coorQueueManagerFields = BuildParamReaderUtil.getItemFields(qmgrPCA, apiSession);
       		logger.info(coorQueueManagerFields);
        		
       		connName = coorQueueManagerFields.get("EI Server DDN") + "(" + coorQueueManagerFields.get("EI Listener Port") + ")";
        		
		} else {

			// Throw exception if value is not present in PCA
		}
            
        return connName;
	}
	
}