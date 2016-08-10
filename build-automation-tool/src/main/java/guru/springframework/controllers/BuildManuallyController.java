package guru.springframework.controllers;

import guru.springframework.domain.BuildManually;
import guru.springframework.domain.BuildManuallyDtls;
import guru.springframework.services.MessageApplicationWithRepoService;
import guru.springframework.services.MessageFlowApplicationWiseService;
import guru.springframework.services.MessageLibraryWithRepoService;
import guru.springframework.services.MessageSetLibraryWiseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BuildManuallyController {

    private MessageApplicationWithRepoService messageapplicationwithrepoService;
    private MessageFlowApplicationWiseService messageflowapplicationwiseService;
    private MessageLibraryWithRepoService messagelibrarywithrepoService;
    private MessageSetLibraryWiseService messagesetlibrarywiseService;

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
       
    	
    	System.out.println("***" + manualDtls.getApplicationWithRepo().getName());
    	
    	BuildManuallyDtls buildManuallyDtls = new BuildManuallyDtls();
    	model.addAttribute("buildmanualdtls", buildManuallyDtls);
    	
    	return "buildmanualdtls";
    }

    /*
    @RequestMapping("broker/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("broker", brokerService.getBrokerById(id));
        model.addAttribute("environments", envDetailService.listAllEnvironment());
        model.addAttribute("queuemanagers", queuemanagerdetailService.listAllQueuemanagers());
        return "brokerform";
    }

    @RequestMapping("broker/new")
    public String newBroker(Model model){
        model.addAttribute("broker", new Broker());
        model.addAttribute("environments", envDetailService.listAllEnvironment());
        model.addAttribute("queuemanagers", queuemanagerdetailService.listAllQueuemanagers());
        return "brokerform";
    }

    @RequestMapping(value = "broker", method = RequestMethod.POST)
    public String saveBroker(Broker broker){
        brokerService.saveBroker(broker);
        return "redirect:/broker/" + broker.getId();
    }

    @RequestMapping("broker/delete/{id}")
    public String delete(@PathVariable Integer id){
        brokerService.deleteBroker(id);
        return "redirect:/brokers";
    }
*/
}
