package guru.springframework.repositories;


import guru.springframework.domain.MessageApplicationWithRepo;

import org.springframework.data.repository.CrudRepository;

public interface MessageApplicationWithRepoRepository extends CrudRepository<MessageApplicationWithRepo, Integer>{
}
