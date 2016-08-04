package guru.springframework.services;

import guru.springframework.domain.ProjectInfo;
import guru.springframework.repositories.ProjectInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {
    private ProjectInfoRepository projectinfoRepository;

    @Autowired
    public void setProjectInfoRepository(ProjectInfoRepository projectinfoRepository) {
        this.projectinfoRepository = projectinfoRepository;
    }

    @Override
    public Iterable<ProjectInfo> listAllProjectInfos() {
        return projectinfoRepository.findAll();
    }

    @Override
    public ProjectInfo getProjectInfoById(Integer id) {
        return projectinfoRepository.findOne(id);
    }

    @Override
    public ProjectInfo saveProjectInfo(ProjectInfo projectinfo) {
        return projectinfoRepository.save(projectinfo);
    }

    @Override
    public void deleteProjectInfo(Integer id) {
        projectinfoRepository.delete(id);
    }
}
