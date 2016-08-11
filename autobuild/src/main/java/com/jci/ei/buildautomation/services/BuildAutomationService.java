package com.jci.ei.buildautomation.services;

import com.jci.ei.buildautomation.webservice.BuildAndDeployServiceResponse;
import com.jci.ei.buildautomation.webservice.BuildServiceResponse;
import com.jci.ei.buildautomation.webservice.DeployServiceResponse;


/**
 * The Interface BuildAutomationService.
 */
public interface BuildAutomationService
{ 
	public BuildServiceResponse build(String pcaNumber, String environment);
	public DeployServiceResponse deploy(String pcaNumber, String environment);
	public BuildAndDeployServiceResponse buildAndDeploy(String pcaNumber, String environment);
 
}