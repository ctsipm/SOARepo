package guru.springframework.repositories;


import guru.springframework.domain.ProjectDeployables;

import org.springframework.data.repository.CrudRepository;

public interface ProjectDeployablesRepository extends CrudRepository<ProjectDeployables, Integer>{
}
