package guru.springframework.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import guru.springframework.domain.BuildManually;
import guru.springframework.domain.BuildManuallyDtls;
import guru.springframework.domain.MessageApplicationWithRepo;
import guru.springframework.jgit.api.CloneRemoteRepository;
import guru.springframework.services.MessageApplicationWithRepoService;
import guru.springframework.services.MessageFlowApplicationWiseService;
import guru.springframework.services.MessageLibraryWithRepoService;
import guru.springframework.services.MessageSetLibraryWiseService;
import guru.springframework.utils.FileUtility;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@PropertySource("classpath:config.properties")
public class BuildManuallyController {

    private MessageApplicationWithRepoService messageapplicationwithrepoService;
    private MessageFlowApplicationWiseService messageflowapplicationwiseService;
    private MessageLibraryWithRepoService messagelibrarywithrepoService;
    private MessageSetLibraryWiseService messagesetlibrarywiseService;

  	@Value("${msgflowtype}")
  	private String msgflowtype;

  	@Value("${local_workspace}")
  	private String localWorkspace;
  	
  	@Value("${repo_url}")
  	private String repoURL;
    
    @Autowired
    public void setMessageApplicationWithReporService(MessageApplicationWithRepoService messageapplicationwithrepoService) {
        this.messageapplicationwithrepoService = messageapplicationwithrepoService;
    }
    
    @Autowired
    public void setMessageFlowApplicationWiseService(MessageFlowApplicationWiseService messageflowapplicationwiseService) {
        this.messageflowapplicationwiseService = messageflowapplicationwiseService;
    }
    
    @Autowired
    public void setMessageLibraryWithRepoService(MessageLibraryWithRepoService messagelibrarywithrepoService) {
        this.messagelibrarywithrepoService = messagelibrarywithrepoService;
    }
    
    @Autowired
    public void setMessageSetLibraryWiseService(MessageSetLibraryWiseService messagesetlibrarywiseService) {
        this.messagesetlibrarywiseService = messagesetlibrarywiseService;
    }

    @RequestMapping(value = "/buildmanually", method = RequestMethod.GET)
    public String list(Model model){
    	model.addAttribute("buildmanually", new BuildManually());
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
       
        return "buildmanually";
    }
    
    
    @RequestMapping(value = "buildmanually/dtls", method = RequestMethod.POST)
    public String buildManually(BuildManually manualDtls, Model model){
       
    	BuildManuallyDtls buildManuallyDtls = new BuildManuallyDtls();
    	
    	buildManuallyDtls.setBuildManually(manualDtls);
    	
    	model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
    	
    	model.addAttribute("buildmanualdtls", buildManuallyDtls);
        
    	return "buildmanualdtls";
    }
    
    @RequestMapping(value = "buildmanually/buildbarfiles", method = RequestMethod.POST)
    public String buildBARFiles(BuildManuallyDtls manualDtls, Model model){       
    	
    	System.out.println("***" + manualDtls.getApplicationWithRepo().size());
    	
    	List<String> applicationInfo = manualDtls.getApplicationWithRepo();
    	
    	for (String applicationInfoIndx : applicationInfo) {
			
    		MessageApplicationWithRepo applicationWithRepo =
    				messageapplicationwithrepoService.getMessageApplicationWithRepoById(Integer.valueOf(applicationInfoIndx));	
    		
    		System.out.println(applicationWithRepo.getName());
    		System.out.println(applicationWithRepo.getReponame());
    		System.out.println(applicationWithRepo.getRepolocation());
    		
    		String remoteURL = repoURL + "/" + applicationWithRepo.getRepolocation() + ".git";
    		
    		try {
				
    			CloneRemoteRepository.cloneRemoteRepository(remoteURL, localWorkspace);
    			
    			
			
    		} catch (InvalidRemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		File[] msgflowFiles = FileUtility.listf(localWorkspace + "\\" + applicationWithRepo.getName());
    		
    		String projectXML = null;
    		
    		for (File file : msgflowFiles) {
				
    			if(file.getName().contains(".project")){
    				projectXML = file.getAbsolutePath();
    				System.out.println("Project XML " + projectXML);
    				break;
    			}
			}
    		
    		
    		
		}
    	
    	
    	
    	BuildManuallyDtls buildManuallyDtls = new BuildManuallyDtls();
    	model.addAttribute("buildmanualdtls", buildManuallyDtls);
    	
    	return "buildmanualdtls";
    }

    
}
