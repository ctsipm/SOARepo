package guru.springframework.jgit.api;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileSystemUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;



/**
 * Simple snippet which shows how to clone a repository from a remote source
 *
 * @author dominik.stadler at gmx.at
 */
public class CloneRemoteRepository {    
    

    private static final String REMOTE_URL = "https://github.com/ctsipm/SOARepo/AddressBook/AddressBook.git";
    private static final String REMOTE_URL1 = "https://github.com/ctsipm/SOARepo/AddressBook/AddressBookMessageSet.git";

    /*public static void main(String[] args) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
        
     // PROXY
        System.setProperty("http.proxyHost","10.245.90.37") ;
        System.setProperty("http.proxyPort", "6050") ;
        
     // PROXY
        System.setProperty("https.proxyHost","10.245.90.37") ;
        System.setProperty("https.proxyPort", "6050") ;
        
        // prepare a new folder for the cloned repository
        File localPath = File.createTempFile("C:\\AddressBook", "");
        localPath.delete();

        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
        try (Git result = Git.cloneRepository()
                .setURI(REMOTE_URL).setURI(REMOTE_URL1)
                .setDirectory(localPath)
                .call()) {
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
	        System.out.println("Having repository: " + result.getRepository().getDirectory());
        }
    }*/
    
    public static void cloneRemoteRepository(String remoteURL, String localRepo) throws IOException, InvalidRemoteException, TransportException, GitAPIException{
    	
    	// PROXY
        System.setProperty("http.proxyHost","10.245.90.37") ;
        System.setProperty("http.proxyPort", "6050") ;
        
        // PROXY
        System.setProperty("https.proxyHost","10.245.90.37") ;
        System.setProperty("https.proxyPort", "6050") ;
        
        // prepare a new folder for the cloned repository
        File localPath = null;
		try {
			localPath = File.createTempFile(localRepo, "");
			localPath.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // then clone
        System.out.println("Cloning from " + remoteURL + " to " + localPath);
        try (Git result = Git.cloneRepository()
                .setURI(remoteURL)
                .setDirectory(localPath)
                .call()) {
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
	        System.out.println("Having repository: " + result.getRepository().getDirectory());
        }
    	
    }
    
}
