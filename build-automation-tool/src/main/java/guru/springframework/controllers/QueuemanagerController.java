package guru.springframework.controllers;

import guru.springframework.domain.Queuemanager;
import guru.springframework.services.EnvironmentService;
import guru.springframework.services.QueuemanagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class QueuemanagerController {

    private QueuemanagerService queuemanagerdetailService;
    private EnvironmentService envDetailService;

    @Autowired
    public void setQueuemanagerService(QueuemanagerService queuemanagerdetailService) {
        this.queuemanagerdetailService = queuemanagerdetailService;
    }
    
    @Autowired
    public void setEnvironmentService(EnvironmentService envDetailService) {
        this.envDetailService = envDetailService;
    }

    @RequestMapping(value = "/queuemanagerdetails", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("queuemanagerdetails", queuemanagerdetailService.listAllQueuemanagers());
        return "queuemanagerdetails";
    }

    @RequestMapping("queuemanagerdetail/{id}")
    public String showQueuemanager(@PathVariable Integer id, Model model){
        model.addAttribute("queuemanagerdetail", queuemanagerdetailService.getQueuemanagerById(id));
        return "queuemanagerdetailshow";
    }

    @RequestMapping("queuemanagerdetail/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("queuemanagerdetail", queuemanagerdetailService.getQueuemanagerById(id));
        model.addAttribute("environments", envDetailService.listAllEnvironment());
        return "queuemanagerdetailform";
    }

    @RequestMapping("queuemanagerdetail/new")
    public String newQueuemanager(Model model){
        model.addAttribute("queuemanagerdetail", new Queuemanager());
        model.addAttribute("environments", envDetailService.listAllEnvironment());
        return "queuemanagerdetailform";
    }

    @RequestMapping(value = "queuemanagerdetail", method = RequestMethod.POST)
    public String saveQueuemanager(Queuemanager queuemanagerdetail){
        queuemanagerdetailService.saveQueuemanager(queuemanagerdetail);
        return "redirect:/queuemanagerdetail/" + queuemanagerdetail.getId();
    }

    @RequestMapping("queuemanagerdetail/delete/{id}")
    public String delete(@PathVariable Integer id){
        queuemanagerdetailService.deleteQueuemanager(id);
        return "redirect:/queuemanagerdetails";
    }

}
