package guru.springframework.controllers;

import guru.springframework.domain.ProjectDeployments;
import guru.springframework.services.EnvironmentService;
import guru.springframework.services.ProjectDeploymentsService;
import guru.springframework.services.ProjectInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProjectDeploymentsController {

    private ProjectDeploymentsService projectdeploymentService;
    private EnvironmentService environmentService;
    private ProjectInfoService projectinfoService;

    @Autowired
    public void setProjectDeploymentService(ProjectDeploymentsService projectdeploymentService) {
        this.projectdeploymentService = projectdeploymentService;
    }
    
    @Autowired
    public void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }
    
    @Autowired
    public void setProjectInfoService(ProjectInfoService projectinfoService) {
        this.projectinfoService = projectinfoService;
    }

    @RequestMapping(value = "/projectdeployments", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("projectdeployments", projectdeploymentService.listAllProjectDeploymentss());
        return "projectdeployments";
    }

    @RequestMapping("projectdeployment/{id}")
    public String showProjectDeployment(@PathVariable Integer id, Model model){
        model.addAttribute("projectdeployment", projectdeploymentService.getProjectDeploymentsById(id));
        return "projectdeploymentshow";
    }

    @RequestMapping("projectdeployment/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("projectdeployment", projectdeploymentService.getProjectDeploymentsById(id));
        model.addAttribute("projectinfos", projectinfoService.listAllProjectInfos());
        model.addAttribute("environments", environmentService.listAllEnvironment());
        return "projectdeploymentform";
    }

    @RequestMapping("projectdeployment/new")
    public String newProjectDeployment(Model model){
        model.addAttribute("projectdeployment", new ProjectDeployments());
        model.addAttribute("projectinfos", projectinfoService.listAllProjectInfos());
        model.addAttribute("environments", environmentService.listAllEnvironment());
        return "projectdeploymentform";
    }

    @RequestMapping(value = "projectdeployment", method = RequestMethod.POST)
    public String saveProjectDeployment(ProjectDeployments projectdeployment){
        projectdeploymentService.saveProjectDeployments(projectdeployment);
        return "redirect:/projectdeployment/" + projectdeployment.getId();
    }

    @RequestMapping("projectdeployment/delete/{id}")
    public String delete(@PathVariable Integer id){
        projectdeploymentService.deleteProjectDeployments(id);
        return "redirect:/projectdeployments";
    }

}
