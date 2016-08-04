package guru.springframework.controllers;

import guru.springframework.domain.Broker;
import guru.springframework.domain.Queuemanager;
import guru.springframework.services.BrokerService;
import guru.springframework.services.EnvironmentService;
import guru.springframework.services.QueuemanagerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BrokerController {

    private BrokerService brokerService;
    private EnvironmentService envDetailService;
    private QueuemanagerService queuemanagerdetailService;

    @Autowired
    public void setBrokerService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }
    
    @Autowired
    public void setEnvironmentService(EnvironmentService envDetailService) {
        this.envDetailService = envDetailService;
    }
    
    @Autowired
    public void setQueuemanagerService(QueuemanagerService queuemanagerdetailService) {
        this.queuemanagerdetailService = queuemanagerdetailService;
    }

    @RequestMapping(value = "/brokers", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("brokers", brokerService.listAllBrokers());
        return "brokers";
    }

    @RequestMapping("broker/{id}")
    public String showBroker(@PathVariable Integer id, Model model){
        model.addAttribute("broker", brokerService.getBrokerById(id));
        return "brokershow";
    }

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

}
