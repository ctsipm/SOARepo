package guru.springframework.controllers;

import guru.springframework.domain.MessageApplicationWithRepo;
import guru.springframework.services.MessageApplicationWithRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageApplicationWithRepoController {

    private MessageApplicationWithRepoService messageapplicationwithrepoService;

    @Autowired
    public void setMessageApplicationWithRepoService(MessageApplicationWithRepoService messageapplicationwithrepoService) {
        this.messageapplicationwithrepoService = messageapplicationwithrepoService;
    }

    @RequestMapping(value = "/messageapplicationwithrepos", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        return "messageapplicationwithrepos";
    }

    @RequestMapping("messageapplicationwithrepo/{id}")
    public String showMessageApplicationWithRepo(@PathVariable Integer id, Model model){
        model.addAttribute("messageapplicationwithrepo", messageapplicationwithrepoService.getMessageApplicationWithRepoById(id));
        return "messageapplicationwithreposhow";
    }

    @RequestMapping("messageapplicationwithrepo/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("messageapplicationwithrepo", messageapplicationwithrepoService.getMessageApplicationWithRepoById(id));
        return "messageapplicationwithrepoform";
    }

    @RequestMapping("messageapplicationwithrepo/new")
    public String newMessageApplicationWithRepo(Model model){
        model.addAttribute("messageapplicationwithrepo", new MessageApplicationWithRepo());
        return "messageapplicationwithrepoform";
    }

    @RequestMapping(value = "messageapplicationwithrepo", method = RequestMethod.POST)
    public String saveMessageApplicationWithRepo(MessageApplicationWithRepo messageapplicationwithrepo){
        messageapplicationwithrepoService.saveMessageApplicationWithRepo(messageapplicationwithrepo);
        return "redirect:/messageapplicationwithrepo/" + messageapplicationwithrepo.getId();
    }

    @RequestMapping("messageapplicationwithrepo/delete/{id}")
    public String delete(@PathVariable Integer id){
        messageapplicationwithrepoService.deleteMessageApplicationWithRepo(id);
        return "redirect:/messageapplicationwithrepos";
    }

}
