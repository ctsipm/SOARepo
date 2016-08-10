package guru.springframework.repositories;


import guru.springframework.domain.BuildManually;
import guru.springframework.domain.ProjectInfo;

import org.springframework.data.repository.CrudRepository;

public interface BuildManuallyRepository extends CrudRepository<BuildManually, Integer>{
}
