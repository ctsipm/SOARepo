package guru.springframework.controllers;

import guru.springframework.domain.ProjectDeployables;
import guru.springframework.services.MessageApplicationWithRepoService;
import guru.springframework.services.MessageLibraryWithRepoService;
import guru.springframework.services.ProjectDeployablesService;
import guru.springframework.services.ProjectInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProjectDeployablesController {

    private ProjectDeployablesService projectdeployableService;
    private MessageApplicationWithRepoService messageapplicationwithrepoService;
    private MessageLibraryWithRepoService messagelibrarywithrepoService;
    private ProjectInfoService projectinfoService;

    @Autowired
    public void setProjectDeployableService(ProjectDeployablesService projectdeployableService) {
        this.projectdeployableService = projectdeployableService;
    }
    
    @Autowired
    public void setMessageApplicationWithRepoService(MessageApplicationWithRepoService messageapplicationwithrepoService) {
        this.messageapplicationwithrepoService = messageapplicationwithrepoService;
    }
    
    @Autowired
    public void setMessageLibraryWithRepoService(MessageLibraryWithRepoService messagelibrarywithrepoService) {
        this.messagelibrarywithrepoService = messagelibrarywithrepoService;
    }
    
    @Autowired
    public void setProjectInfoService(ProjectInfoService projectinfoService) {
        this.projectinfoService = projectinfoService;
    }

    @RequestMapping(value = "/projectdeployables", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("projectdeployables", projectdeployableService.listAllProjectDeployabless());
        return "projectdeployables";
    }

    @RequestMapping("projectdeployable/{id}")
    public String showProjectDeployable(@PathVariable Integer id, Model model){
        model.addAttribute("projectdeployable", projectdeployableService.getProjectDeployablesById(id));
        return "projectdeployableshow";
    }

    @RequestMapping("projectdeployable/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("projectdeployable", projectdeployableService.getProjectDeployablesById(id));
        model.addAttribute("projectinfos", projectinfoService.listAllProjectInfos());
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
        return "projectdeployableform";
    }

    @RequestMapping("projectdeployable/new")
    public String newProjectDeployable(Model model){
        model.addAttribute("projectdeployable", new ProjectDeployables());
        model.addAttribute("projectinfos", projectinfoService.listAllProjectInfos());
        model.addAttribute("messageapplicationwithrepos", messageapplicationwithrepoService.listAllMessageApplicationWithRepos());
        model.addAttribute("messagelibrarywithrepos", messagelibrarywithrepoService.listAllMessageLibraryWithRepos());
        return "projectdeployableform";
    }

    @RequestMapping(value = "projectdeployable", method = RequestMethod.POST)
    public String saveProjectDeployable(ProjectDeployables projectdeployable){
        projectdeployableService.saveProjectDeployables(projectdeployable);
        return "redirect:/projectdeployable/" + projectdeployable.getId();
    }

    @RequestMapping("projectdeployable/delete/{id}")
    public String delete(@PathVariable Integer id){
        projectdeployableService.deleteProjectDeployables(id);
        return "redirect:/projectdeployables";
    }

}
