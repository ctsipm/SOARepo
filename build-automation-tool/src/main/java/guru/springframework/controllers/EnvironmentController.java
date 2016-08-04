package guru.springframework.controllers;


import guru.springframework.domain.Environment;
import guru.springframework.services.EnvironmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EnvironmentController {

    private EnvironmentService envDetailService;

    @Autowired
    public void setEnvironmentService(EnvironmentService envDetailService) {
        this.envDetailService = envDetailService;
    }
    
    @ModelAttribute("environments")
    public Iterable<Environment> populateEnvironments() {
        return this.envDetailService.listAllEnvironment();
    }

    @RequestMapping(value = "/envDetails", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("envDetails", envDetailService.listAllEnvironment());
        return "environmentdetails";
    }

    @RequestMapping("envDetail/{id}")
    public String showEnvironment(@PathVariable Integer id, Model model){
        model.addAttribute("envDetail", envDetailService.getEnvironment(id));
        return "environmentdetailshow";
    }

    @RequestMapping("envDetail/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("envDetail", envDetailService.getEnvironment(id));
        return "environmentdetailsform";
    }

    @RequestMapping("envDetail/new")
    public String newEnvironment(Model model){
        model.addAttribute("envDetail", new Environment());
        return "environmentdetailsform";
    }

    @RequestMapping(value = "envDetail", method = RequestMethod.POST)
    public String saveEnvironment(Environment envDetail){
        envDetailService.saveEnvironment(envDetail);
        return "redirect:/envDetail/" + envDetail.getId();
    }

    @RequestMapping("envDetail/delete/{id}")
    public String delete(@PathVariable Integer id){
        envDetailService.deleteEnvironment(id);
        return "redirect:/envDetails";
    }

}
