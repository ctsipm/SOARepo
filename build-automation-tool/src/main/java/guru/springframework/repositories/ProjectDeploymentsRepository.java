package guru.springframework.repositories;


import guru.springframework.domain.ProjectDeployments;

import org.springframework.data.repository.CrudRepository;

public interface ProjectDeploymentsRepository extends CrudRepository<ProjectDeployments, Integer>{
}
