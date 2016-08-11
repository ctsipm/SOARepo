package com.jci.ei.buildautomation.services.endpoints;

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
*/

import com.jci.ei.buildautomation.services.BuildAutomationService;
import com.jci.ei.buildautomation.webservice.BuildAndDeployServiceRequest;
import com.jci.ei.buildautomation.webservice.BuildAndDeployServiceResponse;
import com.jci.ei.buildautomation.webservice.BuildServiceRequest;
import com.jci.ei.buildautomation.webservice.BuildServiceResponse;
import com.jci.ei.buildautomation.webservice.DeployServiceRequest;
import com.jci.ei.buildautomation.webservice.DeployServiceResponse;

/**
 * The Class BuildAutomationService.
 */
//@Endpoint
public class BuildAutomationServiceEndpoint {
	//private static final String TARGET_NAMESPACE = "http://com/jci/ei/buildautomation/webservice"; 

	//@Autowired
	private BuildAutomationService buildAutomationService;

	//@PayloadRoot(localPart = "BuildServiceRequest", namespace = TARGET_NAMESPACE)
	public //@ResponsePayload
	BuildServiceResponse build(/*@RequestPayload*/ BuildServiceRequest request) {
		return buildAutomationService.build(request.getPcaNumber(),
				request.getEnvironment());
	}

	//@PayloadRoot(localPart = "DeployServiceRequest", namespace = TARGET_NAMESPACE)
	public //@ResponsePayload
	DeployServiceResponse deploy(/*@RequestPayload*/ DeployServiceRequest request) {

		return buildAutomationService.deploy(request.getPcaNumber(),
				request.getEnvironment());
	}

	//@PayloadRoot(localPart = "BuildAndDeployServiceRequest", namespace = TARGET_NAMESPACE)
	public //@ResponsePayload
	BuildAndDeployServiceResponse buildAndDeploy(
			/*@RequestPayload*/ BuildAndDeployServiceRequest request) {
		return buildAutomationService.buildAndDeploy(request.getPcaNumber(),
				request.getEnvironment());
	}

	public void setBuildAutomationService(BuildAutomationService buildAutomationService) {
		this.buildAutomationService = buildAutomationService;
	}
}