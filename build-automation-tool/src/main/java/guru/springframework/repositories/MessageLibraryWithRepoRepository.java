package guru.springframework.repositories;

import guru.springframework.domain.MessageLibraryWithRepo;

import org.springframework.data.repository.CrudRepository;

public interface MessageLibraryWithRepoRepository extends CrudRepository<MessageLibraryWithRepo, Integer>{
}
