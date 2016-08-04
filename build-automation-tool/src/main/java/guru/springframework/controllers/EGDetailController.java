package guru.springframework.controllers;

import guru.springframework.domain.EGDetail;
import guru.springframework.services.BrokerService;
import guru.springframework.services.BrokerService;
import guru.springframework.services.EGDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EGDetailController {

    private EGDetailService egdetailService;
    private BrokerService brokerService;

    @Autowired
    public void setEGDetailService(EGDetailService egdetailService) {
        this.egdetailService = egdetailService;
    }
    
    @Autowired
    public void setBrokerService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    @RequestMapping(value = "/egdetails", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("egdetails", egdetailService.listAllEGDetails());
        return "egdetails";
    }

    @RequestMapping("egdetail/{id}")
    public String showEGDetail(@PathVariable Integer id, Model model){
        model.addAttribute("egdetail", egdetailService.getEGDetailById(id));
        return "egdetailshow";
    }

    @RequestMapping("egdetail/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("egdetail", egdetailService.getEGDetailById(id));
        model.addAttribute("brokers", brokerService.listAllBrokers());
        return "egdetailform";
    }

    @RequestMapping("egdetail/new")
    public String newEGDetail(Model model){
        model.addAttribute("egdetail", new EGDetail());
        model.addAttribute("brokers", brokerService.listAllBrokers());
        return "egdetailform";
    }

    @RequestMapping(value = "egdetail", method = RequestMethod.POST)
    public String saveEGDetail(EGDetail egdetail){
        egdetailService.saveEGDetail(egdetail);
        return "redirect:/egdetail/" + egdetail.getId();
    }

    @RequestMapping("egdetail/delete/{id}")
    public String delete(@PathVariable Integer id){
        egdetailService.deleteEGDetail(id);
        return "redirect:/egdetails";
    }

}
