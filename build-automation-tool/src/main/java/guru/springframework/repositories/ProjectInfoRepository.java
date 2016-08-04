package guru.springframework.repositories;


import guru.springframework.domain.ProjectInfo;

import org.springframework.data.repository.CrudRepository;

public interface ProjectInfoRepository extends CrudRepository<ProjectInfo, Integer>{
}
