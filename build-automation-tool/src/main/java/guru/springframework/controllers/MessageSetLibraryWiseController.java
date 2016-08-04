package guru.springframework.controllers;


import guru.springframework.domain.MessageSetLibraryWise;
import guru.springframework.services.MessageLibraryWithRepoService;
import guru.springframework.services.MessageSetLibraryWiseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageSetLibraryWiseController {

    private MessageSetLibraryWiseService messagesetlibraryService;
    private MessageLibraryWithRepoService messagelibrarywithrepoService;

    @Autowired
    public void setMessageSetLibraryWiseService(MessageSetLibraryWiseService messagesetlibraryService) {
        this.messagesetlibraryService = messagesetlibraryService;
    }
    
    @Autowired
    public void setMessageLibraryWithRepoService(MessageLibraryWithRepoService messagelibrarywithrepoService) {
        this.messagelibrarywithrepoService = messagelibrarywithrepoService;
    }
        

    @RequestMapping(value = "/messagesetlibrarywises", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("messagesetlibrarywises", messagesetlibraryService.listAllMessageSetLibraryWises());
        return "messagesetlibrarywises";
    }

    @RequestMapping("messagesetlibrarywise/{id}")
    public String showMessageSetLibraryWise(@PathVariable Integer id, Model model){
        model.addAttribute("messagesetlibrarywise", messagesetlibraryService.getMessageSetLibraryWiseById(id));
        return "messagesetlibrarywiseshow";
    }

    @RequestMapping("messagesetlibrarywise/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("messagesetlibrarywise", messagesetlibraryService.getMessageSetLibraryWiseById(id));
        
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
        
        return "messagesetlibrarywiseform";
    }

    @RequestMapping("messagesetlibrarywise/new")
    public String newMessageSetLibraryWise(Model model){
        model.addAttribute("messagesetlibrarywise", new MessageSetLibraryWise());        
        
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
        
        return "messagesetlibrarywiseform";
    }

    @RequestMapping(value = "messagesetlibrarywise", method = RequestMethod.POST)
    public String saveMessageSetLibraryWise(MessageSetLibraryWise messagesetlibrary){
        messagesetlibraryService.saveMessageSetLibraryWise(messagesetlibrary);
        return "redirect:/messagesetlibrarywise/" + messagesetlibrary.getId();
    }

    @RequestMapping("messagesetlibrarywise/delete/{id}")
    public String delete(@PathVariable Integer id){
        messagesetlibraryService.deleteMessageSetLibraryWise(id);
        return "redirect:/messagesetlibrarywises";
    }

}
