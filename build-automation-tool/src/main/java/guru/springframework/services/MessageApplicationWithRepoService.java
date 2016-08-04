package guru.springframework.services;


import guru.springframework.domain.MessageApplicationWithRepo;

public interface MessageApplicationWithRepoService {
    Iterable<MessageApplicationWithRepo> listAllMessageApplicationWithRepos();

    MessageApplicationWithRepo getMessageApplicationWithRepoById(Integer id);

    MessageApplicationWithRepo saveMessageApplicationWithRepo(MessageApplicationWithRepo messageapplicationwithrepo);

    void deleteMessageApplicationWithRepo(Integer id);
}
