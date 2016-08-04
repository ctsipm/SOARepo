package guru.springframework.controllers;


import guru.springframework.domain.MessageFlowApplicationWise;
import guru.springframework.services.MessageApplicationWithRepoService;
import guru.springframework.services.MessageFlowApplicationWiseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageFlowApplicationWiseController {

    private MessageFlowApplicationWiseService messageflowapplicationService;
    private MessageApplicationWithRepoService messageapplicationwithrepoService;

    @Autowired
    public void setMessageFlowApplicationWiseService(MessageFlowApplicationWiseService messageflowapplicationService) {
        this.messageflowapplicationService = messageflowapplicationService;
    }
    
    @Autowired
    public void setMessageApplicationWithRepoService(MessageApplicationWithRepoService messageapplicationwithrepoService) {
        this.messageapplicationwithrepoService = messageapplicationwithrepoService;
    }
        

    @RequestMapping(value = "/messageflowapplicationwises", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("messageflowapplicationwises", messageflowapplicationService.listAllMessageFlowApplicationWises());
        return "messageflowapplicationwises";
    }

    @RequestMapping("messageflowapplicationwise/{id}")
    public String showMessageFlowApplicationWise(@PathVariable Integer id, Model model){
        model.addAttribute("messageflowapplicationwise", messageflowapplicationService.getMessageFlowApplicationWiseById(id));
        return "messageflowapplicationwiseshow";
    }

    @RequestMapping("messageflowapplicationwise/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("messageflowapplicationwise", messageflowapplicationService.getMessageFlowApplicationWiseById(id));
        
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        
        return "messageflowapplicationwiseform";
    }

    @RequestMapping("messageflowapplicationwise/new")
    public String newMessageFlowApplicationWise(Model model){
        model.addAttribute("messageflowapplicationwise", new MessageFlowApplicationWise());        
        
        System.out.println("List size is" + messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        
        return "messageflowapplicationwiseform";
    }

    @RequestMapping(value = "messageflowapplicationwise", method = RequestMethod.POST)
    public String saveMessageFlowApplicationWise(MessageFlowApplicationWise messageflowapplication){
        messageflowapplicationService.saveMessageFlowApplicationWise(messageflowapplication);
        return "redirect:/messageflowapplicationwise/" + messageflowapplication.getId();
    }

    @RequestMapping("messageflowapplicationwise/delete/{id}")
    public String delete(@PathVariable Integer id){
        messageflowapplicationService.deleteMessageFlowApplicationWise(id);
        return "redirect:/messageflowapplicationwises";
    }

}
