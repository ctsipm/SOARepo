package com.jci.ei.buildautomation.util;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mks.api.Command;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.mks.api.si.SIModelTypeName;


public class Sandbox
{
	private static Logger logger = Logger.getLogger("JCIBuildAutomation");
	
    // Our date format
    public static final SimpleDateFormat RLOG_DATEFORMAT = new SimpleDateFormat( "MMMMM d, yyyy - h:mm:ss a" );

    // File Separator
    private String fs = System.getProperty( "file.separator" );

    // MKS API Session Object
    private APISession api;

    // Other sandbox specific class variables
    private Project siProject;

    private String sandboxDir;

    private String cpid;

    // Flag to indicate the overall add operation was successful
    private boolean addSuccess;

    // Flag to indicate the overall check-in operation was successful
    private boolean ciSuccess;


    /**
     * The Sandbox constructor
     *
     * @param api       MKS API Session object
     * @param cmProject Project object
     * @param dir       Absolute path to the location for the Sandbox directory
     */
    public Sandbox( APISession api, Project cmProject, String dir, String cpid)
    {
        siProject = cmProject;
        sandboxDir = dir;
        this.api = api;
        //this.cpid = cpid;//System.getProperty( "maven.scm.integrity.cpid" );
        this.cpid = ( ( null == cpid || cpid.length() == 0 ) ? ":none" : cpid );
        addSuccess = true;
        ciSuccess = true;
    }

    /**
     * Attempts to figure out if the current sandbox already exists and is valid
     *
     * @param sandbox The client-side fully qualified path to the sandbox pj
     * @return true/false depending on whether or not this location has a valid sandbox
     * @throws APIException
     */
    private boolean isValidSandbox( String sandbox )
        throws APIException
    {
        Command cmd = new Command( Command.SI, "sandboxinfo" );
        cmd.addOption( new Option( "sandbox", sandbox ) );

        //api.getLogger().debug( "Validating existing sandbox: " + sandbox );
        Response res = api.runCommand( cmd );
        WorkItemIterator wit = res.getWorkItems();
        try
        {
            WorkItem wi = wit.next();
            return wi.getField( "fullConfigSyntax" ).getValueAsString().equalsIgnoreCase(
                siProject.getConfigurationPath() );
        }
        catch ( APIException aex )
        {
        	aex.printStackTrace();
           // api.getLogger().error( "MKS API Exception: " + eh.getMessage() );
           // api.getLogger().debug( eh.getCommand() + " completed with exit code " + eh.getExitCode() );
            return false;
        }
    }

    /**
     * Inspects the MKS API Response object's Item field to determine whether or nor a working file delta exists
     *
     * @param wfdelta MKS API Response object's Item representing the Working File Delta
     * @return true if the working file is a delta; false otherwise
     */
    private boolean isDelta( Item wfdelta )
    {
        // Return false if there is no working file
        if ( wfdelta.getField( "isDelta" ).getBoolean().booleanValue() )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Executes a 'si add' command using the message for the description
     *
     * @param memberFile Full path to the new member's location
     * @param message    Description for the new member's archive
     * @return MKS API Response object
     * @throws  
     * @throws APIException
     */
    public void add(File memberFile, String message) 
        
    {
    	
        // Setup the add command
        //api.getLogger().info( "Adding member: " + memberFile.getAbsolutePath() );
        Command siAdd = new Command( Command.SI, "add" );
        siAdd.addOption( new Option( "onExistingArchive", "sharearchive" ) );
        siAdd.addOption( new Option( "cpid", cpid));
        if ( null != message && message.length() > 0 )
        {
            siAdd.addOption( new Option( "description", message ) );
        }
        String parent = memberFile.getParent();
        parent = parent.replace("'", "");
		siAdd.addOption( new Option( "cwd", parent ) );
        String name = memberFile.getName();
		siAdd.addSelection(name);
		logger.info("Checking in file"+name+" from "+parent);
        try {
        	logger.info("******Started Check in for bar file "+name+"******");
			Response runCommand = api.runCommand( siAdd );
		} catch (APIException e) {
			logger.error("Exception occured on Bar file checkin..");
			e.printStackTrace();
		}
		logger.info("******Finished Check in for bar file "+name+"******");
    }

    /**
     * Executes a 'si ci' command using the relativeName for the member name and message for the description
     *
     * @param memberFile   Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @param message      Description for checking in the new update
     * @return MKS API Response object
     * @throws APIException
     */
    public Response checkin( File memberFile, String relativeName, String message)
        throws APIException
    {
        // Setup the check-in command
        //api.getLogger().info( "Checking in member:  " + memberFile.getAbsolutePath() );
        Command sici = new Command( Command.SI, "ci" );
        sici.addOption( new Option( "cpid", cpid));
        if ( null != message && message.length() > 0 )
        {
            sici.addOption( new Option( "description", message ) );
        }
        sici.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        sici.addSelection( relativeName );
        return api.runCommand( sici );
    }
    
    /**
     * Executes a 'si co' command using the relativeName for the member name and message for the description
     *
     * @param memberFile   Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @param message      Description for checking in the new update
     * @return MKS API Response object
     * @throws APIException
     */
    public Response checkout( File memberFile, String relativeName, String message)
        throws APIException
    {
        Command sici = new Command( Command.SI, "ci" );
        sici.addOption( new Option( "cpid", cpid));
        if ( null != message && message.length() > 0 )
        {
            sici.addOption( new Option( "description", message ) );
        }
        sici.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        sici.addSelection( relativeName );
        return api.runCommand( sici );
    }    

    /**
     * Executes a 'si drop' command using the relativeName for the member name
     *
     * @param memberFile   Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @return MKS API Response object
     * @throws APIException
     */
    private Response dropMember( File memberFile, String relativeName ) throws APIException
    {
        // Setup the drop command
       // api.getLogger().info( "Dropping member " + memberFile.getAbsolutePath() );
        Command siDrop = new Command( Command.SI, "drop" );
        siDrop.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        siDrop.addOption( new Option( "noconfirm" ) );
        siDrop.addOption( new Option( "cpid", cpid ) );
        siDrop.addOption( new Option( "delete" ) );
        siDrop.addSelection( relativeName );
        return api.runCommand( siDrop );
    }

    /**
     * Executes a 'si diff' command to see if the working file has actually changed.  Even though the
     * working file delta might be true, that doesn't always mean the file has actually changed.
     *
     * @param memberFile   Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @return MKS API Response object
     */
    private boolean hasMemberChanged( File memberFile, String relativeName )
    {
        // Setup the differences command
        Command siDiff = new Command( Command.SI, "diff" );
        siDiff.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        siDiff.addSelection( relativeName );
        try
        {
            // Run the diff command...
            Response res = api.runCommand( siDiff );
            try
            {
                // Return the changed flag...
                return res.getWorkItems().next().getResult().getField( "resultant" ).getItem().getField(
                    "different" ).getBoolean().booleanValue();
            }
            catch ( NullPointerException npe )
            {
               // api.getLogger().warn( "Couldn't figure out differences for file: " + memberFile.getAbsolutePath() );
               // api.getLogger().warn(
                    //"Null value found along response object for WorkItem/Result/Field/Item/Field.getBoolean()" );
               // api.getLogger().warn( "Proceeding with the assumption that the file has changed!" );
            }
        }
        catch ( APIException aex )
        {
            aex.printStackTrace();
           // api.getLogger().warn( "Couldn't figure out differences for file: " + memberFile.getAbsolutePath() );
           // api.getLogger().warn( eh.getMessage() );
           // api.getLogger().warn( "Proceeding with the assumption that the file has changed!" );
           // api.getLogger().debug( eh.getCommand() + " completed with exit Code " + eh.getExitCode() );
        }
        return true;
    }

    /**
     * Returns the full path name to the current Sandbox directory
     *
     * @return
     */
    public String getSandboxDir()
    {
        return sandboxDir;
    }

    /**
     * Executes a 'si lock' command using the relativeName of the file
     *
     * @param memberFile   Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @return MKS API Response object
     * @throws APIException
     */
    public Response lock( File memberFile, String relativeName )
        throws APIException
    {
        // Setup the lock command
       // api.getLogger().debug( "Locking member: " + memberFile.getAbsolutePath() );
        Command siLock = new Command( Command.SI, "lock" );
        siLock.addOption( new Option( "revision", ":member" ) );
        siLock.addOption( new Option( "cpid", cpid ) );
        siLock.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        siLock.addSelection( relativeName );
        // Execute the lock command
        return api.runCommand( siLock );
    }

    /**
     * Executes a 'si unlock' command using the relativeName of the file
     *
     * @param memberFile   Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @return MKS API Response object
     * @throws APIException
     */
    public Response unlock( File memberFile, String relativeName )
        throws APIException
    {
        // Setup the unlock command
       // api.getLogger().debug( "Unlocking member: " + memberFile.getAbsolutePath() );
        Command siUnlock = new Command( Command.SI, "unlock" );
        siUnlock.addOption( new Option( "revision", ":member" ) );
        siUnlock.addOption( new Option( "action", "remove" ) );
        siUnlock.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        siUnlock.addSelection( relativeName );
        // Execute the unlock command
        return api.runCommand( siUnlock );
    }

    /**
     * Removes the registration for the Sandbox in the user's profile
     *
     * @return The API Response associated with executing this command
     * @throws APIException
     */
    public Response drop()
        throws APIException
    {
        File project = new File( siProject.getProjectName() );
        File sandboxpj = new File( sandboxDir + fs + project.getName() );

        // Check to see if the sandbox file already exists and its OK to use
       // api.getLogger().debug( "Sandbox Project File: " + sandboxpj.getAbsolutePath() );
        Command cmd = new Command( Command.SI, "dropsandbox" );
        cmd.addOption( new Option( "delete", "members" ) );
        cmd.addOption( new Option( "sandbox", sandboxpj.getAbsolutePath() ) );
        cmd.addOption( new Option( "cwd", sandboxDir ) );
        return api.runCommand( cmd );
    }

    /**
     * Creates a new Sandbox in the sandboxDir specified
     *
     * @return true if the operation is successful; false otherwise
     * @throws APIException
     */
    public boolean create()
        throws APIException
    {
        File project = new File( siProject.getProjectName() );
        File sandboxpj = new File( sandboxDir + fs + project.getName() );

        if ( sandboxpj.isFile() )
        {
            // Validate this sandbox
            if ( isValidSandbox( sandboxpj.getAbsolutePath() ) )
            {
               // api.getLogger().debug(
                   // "Reusing existing Sandbox in " + sandboxDir + " for project " + siProject.getConfigurationPath() );
            	resyncMembers("Common Msgflows");
            	resyncMembers("/PUBLISH and ROUTING FLOWS/PUBLISH FLOWS/Publish Flows/Deployment/Java/PGP/Code/PGPJavaUtil");
            	resyncMembers("/MKSEAIBE/Development/MessageFlows/Common");
            	
            	//resyncMembers();
                return true;
            }
            else
            {
               // api.getLogger().error(
                   // "An invalid Sandbox exists in " + sandboxDir + ". Please provide a different location!" );
                return false;
            }
        }
        else 
        {
           // api.getLogger().debug(
               // "Creating Sandbox in " + sandboxDir + " for project " + siProject.getConfigurationPath() );
            try
            {
                Command cmd = new Command( Command.SI, "createsandbox" );
                cmd.addOption( new Option( "recurse" ) );
                //cmd.addOption( new Option( "nopopulate" ) );
                cmd.addOption( new Option( "populate" ) );
                cmd.addOption( new Option( "project", siProject.getConfigurationPath() ) );
                cmd.addOption( new Option( "cwd", sandboxDir ) );
                api.runCommand( cmd );
            }
            catch ( APIException aex )
            {
                // Check to see if this exception is due an existing sandbox registry entry
                if ( aex.getMessage().indexOf( "There is already a registered entry" ) > 0 )
                {
                    // This will re-validate the sandbox, if Maven blew away the old directory
                    return create();
                }
                else
                {
                    throw aex;
                }
            }
            return true;
        }
    }

    /**
     * Executes a 'si makewritable' command to allow edits to all files in the Sandbox directory
     *
     * @return MKS API Response object
     * @throws APIException
     */
    public Response makeWriteable()
        throws APIException
    {
       // api.getLogger().debug(
            //"Setting files to writeable in " + sandboxDir + " for project " + siProject.getConfigurationPath() );
        Command cmd = new Command( Command.SI, "makewritable" );
        cmd.addOption( new Option( "recurse" ) );
        cmd.addOption( new Option( "cwd", sandboxDir ) );
        return api.runCommand( cmd );
    }

    /**
     * Executes a 'si revert' command to roll back changes to all files in the Sandbox directory
     *
     * @throws APIException
     */
    public void revertMembers()
    {
       // api.getLogger().debug(
            //"Reverting changes in sandbox " + sandboxDir + " for project " + siProject.getConfigurationPath() );
        Command cmd = new Command( Command.SI, "revert");
        cmd.addOption( new Option( "recurse" ) );
        cmd.addOption( new Option( "cwd", sandboxDir ) );
        try {
			api.runCommand( cmd );
			
		} catch (APIException e) {
			logger.warn(e);
		}
    }
    
    /**
     * Executes a 'si revert' command to roll back changes to all files in the Sandbox directory
     *
     * @throws APIException
     */
    public void revertMembers(String subDir)
    {
        Command cmd = new Command( Command.SI, "revert");
        cmd.addOption( new Option( "recurse" ) );
		if (subDir.endsWith("/")) {
			subDir = subDir.substring(0, subDir.length()-1);
		}
		if (!subDir.startsWith("/")) {
			subDir = "/" + subDir;
		}        
        cmd.addOption( new Option( "cwd", sandboxDir+subDir) );
        try {
        	logger.info("**** Revert Operation >>" + sandboxDir+subDir);
			api.runCommand( cmd );
			
		} catch (APIException e) {
			logger.warn(e);
		}
    }    

    /**
     * Executes a 'si resync' command to synchronize the Sandbox directory
     *
     * @return MKS API Response object
     * @throws APIException
     */
    public void resyncMembers()
    {
        Command cmd = new Command( Command.SI, "resync");
        System.out.println(cmd);
        cmd.addOption( new Option( "overwriteChanged" ) );
        cmd.addOption( new Option( "recurse" ) );
        cmd.addOption( new Option( "cwd", sandboxDir ) );
        try {
        	Response res = api.runCommand( cmd );
        	System.out.println(res.getResult());
			
		} catch (APIException e) {
			System.out.println(e);
		}
    }  
    
    /**
     * Executes a 'si resync' command to synchronize the Sandbox directory
     *
     * @return MKS API Response object
     * @throws APIException
     */
    public void resyncMembers(String subDir)
    {
        Command cmd = new Command( Command.SI, "resync");
        System.out.println(cmd);
        cmd.addOption( new Option( "overwriteChanged" ) );
        cmd.addOption( new Option( "recurse" ) );
		
        if (subDir.endsWith("/")) {
			subDir = subDir.substring(0, subDir.length()-1);
		}
		if (!subDir.startsWith("/")) {
			subDir = "/" + subDir;
		}        
        
        cmd.addOption( new Option( "cwd", sandboxDir + subDir ) );
        try {
        	Response res = api.runCommand( cmd );
        	System.out.println(res.getResult());
			
		} catch (APIException e) {
			System.out.println(e);
		}
    }     
    
    /**
     * Returns the overall success of the add operation
     *
     * @return
     */
    public boolean getOverallAddSuccess()
    {
        return addSuccess;
    }

    /**
     * Inspects the MKS API Response object's Item field to determine whether or nor a working file exists
     *
     * @param wfdelta MKS API Response object's Item representing the Working File Delta
     * @return
     */
    public boolean hasWorkingFile( Item wfdelta )
    {
        // Return false if there is no working file
        if ( wfdelta.getField( "noWorkingFile" ).getBoolean().booleanValue() )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Executes a 'si viewsandbox' and parses the output for changed or dropped working files
     *
     * @return A list of MKS API Response WorkItem objects representing the changes in the Sandbox
     * @throws APIException
     */
    public List<WorkItem> getChangeList()
        throws APIException
    {
        // Store a list of files that were changed/removed to the repository
        List<WorkItem> changedFiles = new ArrayList<WorkItem>();
        // Setup the view sandbox command to figure out what has changed...
        Command siViewSandbox = new Command( Command.SI, "viewsandbox" );
        // Create the --fields option
        MultiValue mv = new MultiValue( "," );
        mv.add( "name" );
        mv.add( "context" );
        mv.add( "wfdelta" );
        mv.add( "memberarchive" );
        siViewSandbox.addOption( new Option( "fields", mv ) );
        siViewSandbox.addOption( new Option( "recurse" ) );
        siViewSandbox.addOption( new Option( "noincludeDropped" ) );
        siViewSandbox.addOption( new Option( "filterSubs" ) );
        siViewSandbox.addOption( new Option( "cwd", sandboxDir ) );

        // Run the view sandbox command
        Response r = api.runCommand( siViewSandbox );
        // Check-in all changed files, drop all members with missing working files
        for ( WorkItemIterator wit = r.getWorkItems(); wit.hasNext(); )
        {
            WorkItem wi = wit.next();
           // api.getLogger().debug( "Inspecting file: " + wi.getField( "name" ).getValueAsString() );

            if ( wi.getModelType().equals( SIModelTypeName.MEMBER ) )
            {
                Item wfdeltaItem = (Item) wi.getField( "wfdelta" ).getValue();
                // Proceed with this entry only if it is an actual working file delta
                if ( isDelta( wfdeltaItem ) )
                {
                    File memberFile = new File( wi.getField( "name" ).getValueAsString() );
                    if ( hasWorkingFile( wfdeltaItem ) )
                    {
                        // Only report on files that have actually changed...
                        if ( hasMemberChanged( memberFile, wi.getId() ) )
                        {
                            changedFiles.add( wi );
                        }
                    }
                    else
                    {
                        // Also report on dropped files
                        changedFiles.add( wi );
                    }
                }
            }
        }
        return changedFiles;
    }

    /**
     * Returns the overall success of the check-in operation
     *
     * @return
     */
    public boolean getOverallCheckInSuccess()
    {
        return ciSuccess;
    }

    /**
     * Creates one subproject per directory, as required.
     *
     * @param dirPath A relative path structure of folders
     * @return Response containing the result for the created subproject
     * @throws APIException
     */
    public Response createSubproject( String dirPath )
        throws APIException
    {
        // Setup the create subproject command
       // api.getLogger().debug( "Creating subprojects for: " + dirPath + "/project.pj" );
        Command siCreateSubproject = new Command( Command.SI, "createsubproject" );
        siCreateSubproject.addOption( new Option( "cpid", cpid ) );
        siCreateSubproject.addOption( new Option( "createSubprojects" ) );
        siCreateSubproject.addOption( new Option( "cwd", sandboxDir ) );
        siCreateSubproject.addSelection( dirPath + "/project.pj" );
        // Execute the create subproject command
        return api.runCommand( siCreateSubproject );
    }
 
    /**
     * 
     * @param pcaNumber
     * @param updateField
     * @param apiSession
     * @return the response returned after command execution
     * @throws APIException
     */
    public Response editItem(String pcaNumber, String updateField, APISession apiSession) throws APIException {
		Command cmd = new Command();
		Response response = null;
		cmd.setApp(Command.IM);
		cmd.setCommandName("editissue");
		cmd.addOption( new Option("field", updateField));
		cmd.addSelection(pcaNumber);		
		try {
			logger.info("Updating the field:value "+updateField+" for PCA number "+pcaNumber);
			response = apiSession.runCommand(cmd);
			logger.info(response);
		} catch (APIException e) {
			logger.error("Issue in Edit PCA .."+e);
			throw e;
		}
		return response;
	}
    
    /**
     * Adding Label to a MKS member
     * @throws APIException 
     */
	public void addLabel(String targetFile, String label, String revision, String projectPath) throws APIException {

		Command cmd = new Command( Command.SI, "addlabel" );
		cmd.addOption( new Option( "project", projectPath) );
        cmd.addOption( new Option( "label", label) );
        if (revision != null) {
        	cmd.addOption( new Option( "revision", revision) );
		}
        cmd.addSelection(targetFile);
		try {
			logger.info("Applying label "+label+" on target file "+targetFile + " version " + revision);
			Response response = api.runCommand(cmd);
			logger.info(response);
		} catch (APIException e) {
			logger.error("Error applying label"+e);
			throw e;
		}
	}
	
    /**
     * Executes a 'si freeze' command using the relativeName of the file
     *
     * @param memberFile Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @return MKS API Response object
     * @throws APIException
     */
    public Response freeze(File memberFile, String relativeName )
        throws APIException
    {
        Command siFreeze = new Command( Command.SI, "freeze" );
        siFreeze.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        siFreeze.addSelection( relativeName );
        logger.info("Freezing relativeName....");
        return api.runCommand( siFreeze );
    }
    
    /**
     * Executes a 'si thaw' command using the relativeName of the file
     *
     * @param memberFile Full path to the member's current sandbox location
     * @param relativeName Relative path from the nearest subproject or project
     * @throws APIException
     */
    public void thaw(File memberFile, String relativeName )
    {
        Command siThaw = new Command( Command.SI, "thaw" );
        siThaw.addOption( new Option( "cwd", memberFile.getParentFile().getAbsolutePath() ) );
        siThaw.addSelection( relativeName );
        try {
			logger.info("Thawing relativeName....");
        	api.runCommand( siThaw );
        	logger.info("Success!");
		} catch (APIException e) {
			logger.error(e);
		}
    }    
    
}