/**
 * 
 */
package com.jci.ei.buildautomation.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import com.jci.ei.buildautomation.dto.DeployEnvironmentDTO;
import com.jci.ei.buildautomation.dto.UserDefinedPropertyDTO;

/**
 * @author srccodes.com
 * @version 1.0	
 */
public class AntExecutorUtil {
	
	private static Logger logger = Logger.getLogger("JCIBuildAutomation");
	
    /**
     * Builds the bar file for Message flow and Message set projects
     * 
     * @param buildXmlFileFullPath
     * @param target
     * @throws IOException 
     */
    public static void executebuildTask(String workspaceLocation, String barfileLocation, String buildOutputLocation, String barFileName, String projectName, String resourcePath, String buildFileLocation, String wmbtkPath, boolean isMessageFlowBuild) throws BuildException, IOException{

        DefaultLogger consoleLogger = getConsoleLogger();

        // Prepare Ant project
        Project project = new Project();
        File buildFile = new File(buildFileLocation);
        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        
        
        project.setProperty("workspaceLoc", workspaceLocation);
        
        //Writing the bar file to a temporary location. Will be copied back to the sandbox once build is done
        String temporaryBarFileLocation = buildOutputLocation+"/"+barFileName;
        
		project.setProperty("barfileLocation", temporaryBarFileLocation);
        project.setProperty("projectName", projectName);
        project.setProperty("resourcePath", resourcePath);
        project.setProperty("mqsicreatebarFile.path", wmbtkPath+"/mqsicreatebar.exe");
        //project.setProperty("mqsicreatebarFile.path","mqsicreatebar.exe");
        //Added to fix problem with mqsicreatebar.exe: by jbosiko, Nov 16, 2015
        project.setProperty("mqsicreatebarFileExecution.path",wmbtkPath);
         
        
        project.addBuildListener(consoleLogger);

        // Capture event for Ant script build start / stop / failure
        try {
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            projectHelper.parse(project, buildFile);
            project.addReference("ant.projectHelper", projectHelper);
            
            //Calling Message Flow or Message Set targets based on the isMessageFlowBuild boolean flag
            String targetToExecute = isMessageFlowBuild?"buildMFProject":"buildMSProject";
            project.executeTarget(targetToExecute);
            
            project.fireBuildFinished(null);
            
            //Copying back the bar file from Temporary location to Sandbox location. Doing this as Ant script fails when the path has a space(Bar File in the example) - D:/SandBox/I2081_PEGASO_INVOICE/Deployment/Bar Files  
            FileUtils.copyFileToDirectory(new File(temporaryBarFileLocation), new File(barfileLocation));
            
        } catch (BuildException buildException) {
            project.fireBuildFinished(buildException);
            logger.error(buildException.toString());
            throw buildException;
        }
        
    }
    
   /**
    * Deploys a bar file on a broker
    * @param deployEnvironmentDTO
    * @param barFilePath
    * @param deployXMLFileLocation
    * @throws BuildException
    */
    public static void executeDeployTask(DeployEnvironmentDTO deployEnvironmentDTO, String barFilePath, String deployXMLFileLocation) throws BuildException{
    	
        DefaultLogger consoleLogger = getConsoleLogger();

        // Prepare Ant project
        Project project = new Project();
        File buildFile = new File(deployXMLFileLocation);
        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        
        
        project.setProperty("ipaddress", deployEnvironmentDTO.getHost());
        project.setProperty("port", deployEnvironmentDTO.getPort());
        project.setProperty("queuemgr", deployEnvironmentDTO.getQueueManager());
        project.setProperty("exegroup", deployEnvironmentDTO.getExecutionGroup());
        project.setProperty("barFilePath", barFilePath);
        project.setProperty("deployBatchFile.path", buildFile.getParentFile().getPath()+"/mqsideployscript.bat");
        
        
        project.addBuildListener(consoleLogger);

        // Capture event for Ant script build start / stop / failure
        try {
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            projectHelper.parse(project, buildFile);
            project.addReference("ant.projectHelper", projectHelper);
            
            project.executeTarget(project.getDefaultTarget());
            project.fireBuildFinished(null);
        } catch (BuildException buildException) {
            project.fireBuildFinished(buildException);
            logger.error(buildException.toString());
            throw buildException;
        }
    }
    
    /**
     * Updates the User defines properties on a bar file
     * @param barFilePath
     * @param messageFlowname
     * @param userDefinedPropertyDTOs
     * @param updateUDPFileLocation
     * @param wmbtkPath
     * @return
     * @throws BuildException
     */
    public static String executeUDPUpdateTask(String barFilePath, String messageFlowname, List<UserDefinedPropertyDTO> userDefinedPropertyDTOs, String updateUDPFileLocation, String wmbtkPath) throws BuildException{
    	
        DefaultLogger consoleLogger = getConsoleLogger();

        // Prepare Ant project
        Project project = new Project();
        File buildFile = new File(updateUDPFileLocation);
        project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        
        project.setProperty("barFile.path", barFilePath);
        project.setProperty("mqsiapplybaroverrideFile.path", wmbtkPath+"/mqsiapplybaroverride");
        
        StringBuffer udpBuffer = new StringBuffer();
        for(UserDefinedPropertyDTO userDefinedPropertyDTO : userDefinedPropertyDTOs){
        	udpBuffer.append(messageFlowname);
        	udpBuffer.append("#");
        	udpBuffer.append(userDefinedPropertyDTO.getName());
        	udpBuffer.append("=");
        	udpBuffer.append(userDefinedPropertyDTO.getValue());
        	udpBuffer.append(",");
        }
        
        String udp = udpBuffer.substring(0, udpBuffer.length()-1);
        
        project.setProperty("barFile.properties", udp);
        
        logger.info("*****  Updating the UDP>>"+udp+" on bar file>>"+barFilePath);
        
        project.addBuildListener(consoleLogger);

        // Capture event for Ant script build start / stop / failure
        try {
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            projectHelper.parse(project, buildFile);
            project.addReference("ant.projectHelper", projectHelper);
            
            project.executeTarget(project.getDefaultTarget());
            project.fireBuildFinished(null);
        } catch (BuildException buildException) {
            project.fireBuildFinished(buildException);
            logger.info(buildException.toString());
            throw buildException;
        }
        return udp;
    }
    
    /**
     * Logger to log output generated while executing ant script in console
     * 
     * @return
     */
    private static DefaultLogger getConsoleLogger() {
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
        
        return consoleLogger;
    }
}
