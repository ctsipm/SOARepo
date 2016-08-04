package guru.springframework.services;


import guru.springframework.domain.ProjectDeployments;

public interface ProjectDeploymentsService {
    Iterable<ProjectDeployments> listAllProjectDeploymentss();

    ProjectDeployments getProjectDeploymentsById(Integer id);

    ProjectDeployments saveProjectDeployments(ProjectDeployments projectdeployments);

    void deleteProjectDeployments(Integer id);
}
