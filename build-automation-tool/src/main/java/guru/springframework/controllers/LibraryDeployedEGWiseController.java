package guru.springframework.controllers;

import java.util.ArrayList;
import java.util.List;

import guru.springframework.domain.MessageLibraryWithRepo;
import guru.springframework.domain.EGDetail;
import guru.springframework.domain.LibraryDeployedEGWise;
import guru.springframework.services.MessageLibraryWithRepoService;
import guru.springframework.services.EGDetailService;
import guru.springframework.services.LibraryDeployedEGWiseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LibraryDeployedEGWiseController {

    private LibraryDeployedEGWiseService librarydeployedegwiseService;
    private MessageLibraryWithRepoService messagelibrarywithrepoService;
    private EGDetailService egdetailService;

    @Autowired
    public void setLibraryDeployedEGWiseService(LibraryDeployedEGWiseService librarydeployedegwiseService) {
        this.librarydeployedegwiseService = librarydeployedegwiseService;
    }
    
    @Autowired
    public void setMessageLibraryWithRepoService(MessageLibraryWithRepoService messagelibrarywithrepoService) {
        this.messagelibrarywithrepoService = messagelibrarywithrepoService;
    }
    
    @Autowired
    public void setEGDetailService(EGDetailService egdetailService) {
        this.egdetailService = egdetailService;
    }

    @RequestMapping(value = "/librarydeployedegwises", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("librarydeployedegwises", librarydeployedegwiseService.listAllLibraryDeployedEGWises());
        return "librarydeployedegwises";
    }

    @RequestMapping("librarydeployedegwise/{id}")
    public String showLibraryDeployedEGWise(@PathVariable Integer id, Model model){
        model.addAttribute("librarydeployedegwise", librarydeployedegwiseService.getLibraryDeployedEGWiseById(id));
        return "librarydeployedegwiseshow";
    }

    @RequestMapping("librarydeployedegwise/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("librarydeployedegwise", librarydeployedegwiseService.getLibraryDeployedEGWiseById(id));
        
        Iterable<EGDetail> egdetails = egdetailService.listAllEGDetails();
        List<EGDetail> egdetails1 = new ArrayList<EGDetail>();
        
        for (EGDetail egdetail : egdetails) {
    		
			egdetail.setName(egdetail.getName() + " ---> " + egdetail.getBroker().getName());
			egdetails1.add(egdetail);
		
        }
        
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
        model.addAttribute("egdetails", egdetails1);
        
        return "librarydeployedegwiseform";
    }

    @RequestMapping("librarydeployedegwise/new")
    public String newLibraryDeployedEGWise(Model model){
        model.addAttribute("librarydeployedegwise", new LibraryDeployedEGWise());        
        
        Iterable<EGDetail> egdetails = egdetailService.listAllEGDetails();
        List<EGDetail> egdetails1 = new ArrayList<EGDetail>();
        
        for (EGDetail egdetail : egdetails) {
    		
			egdetail.setName(egdetail.getName() + " ---> " + egdetail.getBroker().getName());
			egdetails1.add(egdetail);
		
        }
        
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
        model.addAttribute("egdetails", egdetails1);
        
        return "librarydeployedegwiseform";
    }

    @RequestMapping(value = "librarydeployedegwise", method = RequestMethod.POST)
    public String saveLibraryDeployedEGWise(LibraryDeployedEGWise librarydeployedegwise){
        librarydeployedegwiseService.saveLibraryDeployedEGWise(librarydeployedegwise);
        return "redirect:/librarydeployedegwise/" + librarydeployedegwise.getId();
    }

    @RequestMapping("librarydeployedegwise/delete/{id}")
    public String delete(@PathVariable Integer id){
        librarydeployedegwiseService.deleteLibraryDeployedEGWise(id);
        return "redirect:/librarydeployedegwises";
    }

}
