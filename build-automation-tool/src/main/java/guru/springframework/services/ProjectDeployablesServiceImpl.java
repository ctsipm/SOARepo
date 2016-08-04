package guru.springframework.services;

import guru.springframework.domain.ProjectDeployables;
import guru.springframework.repositories.ProjectDeployablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectDeployablesServiceImpl implements ProjectDeployablesService {
    private ProjectDeployablesRepository projectdeployablesRepository;

    @Autowired
    public void setProjectDeployablesRepository(ProjectDeployablesRepository projectdeployablesRepository) {
        this.projectdeployablesRepository = projectdeployablesRepository;
    }

    @Override
    public Iterable<ProjectDeployables> listAllProjectDeployabless() {
        return projectdeployablesRepository.findAll();
    }

    @Override
    public ProjectDeployables getProjectDeployablesById(Integer id) {
        return projectdeployablesRepository.findOne(id);
    }

    @Override
    public ProjectDeployables saveProjectDeployables(ProjectDeployables projectdeployables) {
        return projectdeployablesRepository.save(projectdeployables);
    }

    @Override
    public void deleteProjectDeployables(Integer id) {
        projectdeployablesRepository.delete(id);
    }
}
