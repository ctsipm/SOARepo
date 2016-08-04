package guru.springframework.controllers;

import java.util.ArrayList;
import java.util.List;

import guru.springframework.domain.ApplicationDeployedEGWise;
import guru.springframework.domain.EGDetail;
import guru.springframework.domain.MessageApplicationWithRepo;
import guru.springframework.services.ApplicationDeployedEGWiseService;
import guru.springframework.services.EGDetailService;
import guru.springframework.services.MessageApplicationWithRepoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ApplicationDeployedEGWiseController {

    private ApplicationDeployedEGWiseService applicationdeployedegwiseService;
    private MessageApplicationWithRepoService messageapplicationwithrepoService;
    private EGDetailService egdetailService;

    @Autowired
    public void setApplicationDeployedEGWiseService(ApplicationDeployedEGWiseService applicationdeployedegwiseService) {
        this.applicationdeployedegwiseService = applicationdeployedegwiseService;
    }
    
    @Autowired
    public void setMessageApplicationWithRepoService(MessageApplicationWithRepoService messageapplicationwithrepoService) {
        this.messageapplicationwithrepoService = messageapplicationwithrepoService;
    }
    
    @Autowired
    public void setEGDetailService(EGDetailService egdetailService) {
        this.egdetailService = egdetailService;
    }

    @RequestMapping(value = "/applicationdeployedegwises", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("applicationdeployedegwises", applicationdeployedegwiseService.listAllApplicationDeployedEGWises());
        return "applicationdeployedegwises";
    }

    @RequestMapping("applicationdeployedegwise/{id}")
    public String showApplicationDeployedEGWise(@PathVariable Integer id, Model model){
        model.addAttribute("applicationdeployedegwise", applicationdeployedegwiseService.getApplicationDeployedEGWiseById(id));
        return "applicationdeployedegwiseshow";
    }

    @RequestMapping("applicationdeployedegwise/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("applicationdeployedegwise", applicationdeployedegwiseService.getApplicationDeployedEGWiseById(id));
        
        Iterable<EGDetail> egdetails = egdetailService.listAllEGDetails();
        List<EGDetail> egdetails1 = new ArrayList<EGDetail>();
        
        for (EGDetail egdetail : egdetails) {
    		
			egdetail.setName(egdetail.getName() + " ---> " + egdetail.getBroker().getName());
			egdetails1.add(egdetail);
		
        }
        
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        model.addAttribute("egdetails", egdetails1);
        
        return "applicationdeployedegwiseform";
    }

    @RequestMapping("applicationdeployedegwise/new")
    public String newApplicationDeployedEGWise(Model model){
        model.addAttribute("applicationdeployedegwise", new ApplicationDeployedEGWise());        
        
        Iterable<EGDetail> egdetails = egdetailService.listAllEGDetails();
        List<EGDetail> egdetails1 = new ArrayList<EGDetail>();
        
        
        	for (EGDetail egdetail : egdetails) {
        		
        			egdetail.setName(egdetail.getName() + " ---> " + egdetail.getBroker().getName());
        			egdetails1.add(egdetail);
        		
        	}
		
        
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        model.addAttribute("egdetails", egdetails1);
        
        return "applicationdeployedegwiseform";
    }

    @RequestMapping(value = "applicationdeployedegwise", method = RequestMethod.POST)
    public String saveApplicationDeployedEGWise(ApplicationDeployedEGWise applicationdeployedegwise){
        applicationdeployedegwiseService.saveApplicationDeployedEGWise(applicationdeployedegwise);
        return "redirect:/applicationdeployedegwise/" + applicationdeployedegwise.getId();
    }

    @RequestMapping("applicationdeployedegwise/delete/{id}")
    public String delete(@PathVariable Integer id){
        applicationdeployedegwiseService.deleteApplicationDeployedEGWise(id);
        return "redirect:/applicationdeployedegwises";
    }

}
