package com.jci.ei.buildautomation.test;
import java.util.Enumeration;

import com.ibm.broker.config.proxy.*;

public class DeployBar {

	private static final int BROKER_TIMEOUT = 300000;
	
	/**
	 * @param args
	 * @throws ConfigManagerProxyLoggedException 
	 */
	public static void main(String[] args) throws Exception {

		String barfilePath = "E:/X. WMB 7 Test/GeneratedBarFiles/I2099_NXGEN_COSTDATA/I2099_NXGEN_COSTDATA_OAG.msgflow.generated.bar";
		
		BrokerConnectionParameters bcp =
			new MQBrokerConnectionParameters("localhost", 1414, "BROKERQM");
		if (bcp != null) {
			BrokerProxy broker = BrokerProxy.getInstance(bcp);
			if (broker != null) {
				ExecutionGroupProxy eg = broker.getExecutionGroupByName("default");
				if (eg != null) {
					DeployResult deployResult = eg.deploy(barfilePath, true, BROKER_TIMEOUT);
					String completionCode = deployResult.getCompletionCode().toString();
					System.out.println(completionCode);
					if ("success".compareTo(completionCode) != 0) {
						Enumeration<LogEntry> desc = deployResult.getDeployResponses();
						while (desc.hasMoreElements()){
					         System.out.println(desc.nextElement()); 
					      }						
						throw new Exception(deployResult.toString());
					}else{
						Enumeration<LogEntry> desc = deployResult.getDeployResponses();
						while (desc.hasMoreElements()){
					         System.out.println(desc.nextElement()); 
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
}
