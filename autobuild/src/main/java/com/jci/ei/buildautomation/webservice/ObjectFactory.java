//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.15 at 07:15:24 PM IST 
//


package com.jci.ei.buildautomation.webservice;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jci.ei.buildautomation.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jci.ei.buildautomation.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BuildAndDeployServiceRequest }
     * 
     */
    public BuildAndDeployServiceRequest createBuildAndDeployServiceRequest() {
        return new BuildAndDeployServiceRequest();
    }

    /**
     * Create an instance of {@link BuildAndDeployServiceResponse }
     * 
     */
    public BuildAndDeployServiceResponse createBuildAndDeployServiceResponse() {
        return new BuildAndDeployServiceResponse();
    }

    /**
     * Create an instance of {@link BuildServiceRequest }
     * 
     */
    public BuildServiceRequest createBuildServiceRequest() {
        return new BuildServiceRequest();
    }

    /**
     * Create an instance of {@link DeployServiceRequest }
     * 
     */
    public DeployServiceRequest createDeployServiceRequest() {
        return new DeployServiceRequest();
    }

    /**
     * Create an instance of {@link DeployServiceResponse }
     * 
     */
    public DeployServiceResponse createDeployServiceResponse() {
        return new DeployServiceResponse();
    }

    /**
     * Create an instance of {@link BuildServiceResponse }
     * 
     */
    public BuildServiceResponse createBuildServiceResponse() {
        return new BuildServiceResponse();
    }

}