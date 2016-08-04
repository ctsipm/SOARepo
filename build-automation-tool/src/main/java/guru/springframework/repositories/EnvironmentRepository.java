package guru.springframework.repositories;

import guru.springframework.domain.Environment;

import org.springframework.data.repository.CrudRepository;

public interface EnvironmentRepository extends CrudRepository<Environment, Integer>{
}
