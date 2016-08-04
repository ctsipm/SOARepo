package guru.springframework.controllers;

import guru.springframework.domain.MessageLibraryWithRepo;
import guru.springframework.services.MessageLibraryWithRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageLibraryWithRepoController {

    private MessageLibraryWithRepoService messagelibrarywithrepoService;

    @Autowired
    public void setMessageLibraryWithRepoService(MessageLibraryWithRepoService messagelibrarywithrepoService) {
        this.messagelibrarywithrepoService = messagelibrarywithrepoService;
    }

    @RequestMapping(value = "/messagelibrarywithrepos", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
        return "messagelibrarywithrepos";
    }

    @RequestMapping("messagelibrarywithrepo/{id}")
    public String showMessageLibraryWithRepo(@PathVariable Integer id, Model model){
        model.addAttribute("messagelibrarywithrepo", messagelibrarywithrepoService.getMessageLibraryWithRepoById(id));
        return "messagelibrarywithreposhow";
    }

    @RequestMapping("messagelibrarywithrepo/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("messagelibrarywithrepo", messagelibrarywithrepoService.getMessageLibraryWithRepoById(id));
        return "messagelibrarywithrepoform";
    }

    @RequestMapping("messagelibrarywithrepo/new")
    public String newMessageLibraryWithRepo(Model model){
        model.addAttribute("messagelibrarywithrepo", new MessageLibraryWithRepo());
        return "messagelibrarywithrepoform";
    }

    @RequestMapping(value = "messagelibrarywithrepo", method = RequestMethod.POST)
    public String saveMessageLibraryWithRepo(MessageLibraryWithRepo messagelibrarywithrepo){
        messagelibrarywithrepoService.saveMessageLibraryWithRepo(messagelibrarywithrepo);
        return "redirect:/messagelibrarywithrepo/" + messagelibrarywithrepo.getId();
    }

    @RequestMapping("messagelibrarywithrepo/delete/{id}")
    public String delete(@PathVariable Integer id){
        messagelibrarywithrepoService.deleteMessageLibraryWithRepo(id);
        return "redirect:/messagelibrarywithrepos";
    }

}
