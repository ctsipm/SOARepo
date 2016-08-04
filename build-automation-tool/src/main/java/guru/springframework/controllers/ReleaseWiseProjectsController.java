package guru.springframework.controllers;

import guru.springframework.domain.ReleaseWiseProjects;
import guru.springframework.services.ReleaseWiseProjectsService;
import guru.springframework.services.ReleaseInfoService;
import guru.springframework.services.ProjectInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReleaseWiseProjectsController {

    private ReleaseWiseProjectsService releasewiseprojectService;
    private ReleaseInfoService releaseinfoService;
    private ProjectInfoService projectinfoService;

    @Autowired
    public void setReleaseWiseProjectsService(ReleaseWiseProjectsService releasewiseprojectService) {
        this.releasewiseprojectService = releasewiseprojectService;
    }
    
    @Autowired
    public void setReleaseInfoService(ReleaseInfoService releaseinfoService) {
        this.releaseinfoService = releaseinfoService;
    }
    
    @Autowired
    public void setProjectInfoService(ProjectInfoService projectinfoService) {
        this.projectinfoService = projectinfoService;
    }

    @RequestMapping(value = "/releasewiseprojects", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("releasewiseprojects", releasewiseprojectService.listAllReleaseWiseProjectss());
        return "releasewiseprojects";
    }

    @RequestMapping("releasewiseproject/{id}")
    public String showReleaseWiseProjects(@PathVariable Integer id, Model model){
        model.addAttribute("releasewiseproject", releasewiseprojectService.getReleaseWiseProjectsById(id));
        return "releasewiseprojectshow";
    }

    @RequestMapping("releasewiseproject/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("releasewiseproject", releasewiseprojectService.getReleaseWiseProjectsById(id));
        model.addAttribute("releaseinfos", releaseinfoService.listAllReleaseInfos());
        model.addAttribute("projectinfos", projectinfoService.listAllProjectInfos());
        return "releasewiseprojectform";
    }

    @RequestMapping("releasewiseproject/new")
    public String newReleaseWiseProjects(Model model){
        model.addAttribute("releasewiseproject", new ReleaseWiseProjects());
        model.addAttribute("releaseinfos", releaseinfoService.listAllReleaseInfos());
        model.addAttribute("projectinfos", projectinfoService.listAllProjectInfos());
        return "releasewiseprojectform";
    }

    @RequestMapping(value = "releasewiseproject", method = RequestMethod.POST)
    public String saveReleaseWiseProjects(ReleaseWiseProjects releasewiseproject){
        releasewiseprojectService.saveReleaseWiseProjects(releasewiseproject);
        return "redirect:/releasewiseproject/" + releasewiseproject.getId();
    }

    @RequestMapping("releasewiseproject/delete/{id}")
    public String delete(@PathVariable Integer id){
        releasewiseprojectService.deleteReleaseWiseProjects(id);
        return "redirect:/releasewiseprojects";
    }

}
