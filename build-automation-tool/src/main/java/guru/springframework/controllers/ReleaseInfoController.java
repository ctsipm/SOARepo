package guru.springframework.controllers;

import guru.springframework.domain.ReleaseInfo;
import guru.springframework.services.ReleaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReleaseInfoController {

    private ReleaseInfoService releaseinfoService;

    @Autowired
    public void setReleaseInfoService(ReleaseInfoService releaseinfoService) {
        this.releaseinfoService = releaseinfoService;
    }

    @RequestMapping(value = "/releaseinfos", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("releaseinfos", releaseinfoService.listAllReleaseInfos());
        return "releaseinfos";
    }

    @RequestMapping("releaseinfo/{id}")
    public String showReleaseInfo(@PathVariable Integer id, Model model){
        model.addAttribute("releaseinfo", releaseinfoService.getReleaseInfoById(id));
        return "releaseinfoshow";
    }

    @RequestMapping("releaseinfo/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("releaseinfo", releaseinfoService.getReleaseInfoById(id));
        return "releaseinfoform";
    }

    @RequestMapping("releaseinfo/new")
    public String newReleaseInfo(Model model){
        model.addAttribute("releaseinfo", new ReleaseInfo());
        return "releaseinfoform";
    }

    @RequestMapping(value = "releaseinfo", method = RequestMethod.POST)
    public String saveReleaseInfo(ReleaseInfo releaseinfo){
        releaseinfoService.saveReleaseInfo(releaseinfo);
        return "redirect:/releaseinfo/" + releaseinfo.getId();
    }

    @RequestMapping("releaseinfo/delete/{id}")
    public String delete(@PathVariable Integer id){
        releaseinfoService.deleteReleaseInfo(id);
        return "redirect:/releaseinfos";
    }

}
