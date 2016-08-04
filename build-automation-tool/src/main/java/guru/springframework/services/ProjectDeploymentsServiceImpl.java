package guru.springframework.services;

import guru.springframework.domain.ProjectDeployments;
import guru.springframework.repositories.ProjectDeploymentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectDeploymentsServiceImpl implements ProjectDeploymentsService {
    private ProjectDeploymentsRepository projectdeploymentsRepository;

    @Autowired
    public void setProjectDeploymentsRepository(ProjectDeploymentsRepository projectdeploymentsRepository) {
        this.projectdeploymentsRepository = projectdeploymentsRepository;
    }

    @Override
    public Iterable<ProjectDeployments> listAllProjectDeploymentss() {
        return projectdeploymentsRepository.findAll();
    }

    @Override
    public ProjectDeployments getProjectDeploymentsById(Integer id) {
        return projectdeploymentsRepository.findOne(id);
    }

    @Override
    public ProjectDeployments saveProjectDeployments(ProjectDeployments projectdeployments) {
        return projectdeploymentsRepository.save(projectdeployments);
    }

    @Override
    public void deleteProjectDeployments(Integer id) {
        projectdeploymentsRepository.delete(id);
    }
}
