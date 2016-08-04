package guru.springframework.controllers;

import guru.springframework.domain.ProjectInfo;
import guru.springframework.services.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProjectController {

    private ProjectInfoService projectinfoService;

    @Autowired
    public void setProjectInfoService(ProjectInfoService projectinfoService) {
        this.projectinfoService = projectinfoService;
    }

    @RequestMapping(value = "/projectinfos", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("projectinfos", projectinfoService.listAllProjectInfos());
        return "projectinfos";
    }

    @RequestMapping("projectinfo/{id}")
    public String showProjectInfo(@PathVariable Integer id, Model model){
        model.addAttribute("projectinfo", projectinfoService.getProjectInfoById(id));
        return "projectinfoshow";
    }

    @RequestMapping("projectinfo/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("projectinfo", projectinfoService.getProjectInfoById(id));
        return "projectinfoform";
    }

    @RequestMapping("projectinfo/new")
    public String newProjectInfo(Model model){
        model.addAttribute("projectinfo", new ProjectInfo());
        return "projectinfoform";
    }

    @RequestMapping(value = "projectinfo", method = RequestMethod.POST)
    public String saveProjectInfo(ProjectInfo projectinfo){
        projectinfoService.saveProjectInfo(projectinfo);
        return "redirect:/projectinfo/" + projectinfo.getId();
    }

    @RequestMapping("projectinfo/delete/{id}")
    public String delete(@PathVariable Integer id){
        projectinfoService.deleteProjectInfo(id);
        return "redirect:/projectinfos";
    }

}
