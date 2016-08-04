package guru.springframework.repositories;


import guru.springframework.domain.ReleaseWiseProjects;

import org.springframework.data.repository.CrudRepository;

public interface ReleaseWiseProjectsRepository extends CrudRepository<ReleaseWiseProjects, Integer>{
}
