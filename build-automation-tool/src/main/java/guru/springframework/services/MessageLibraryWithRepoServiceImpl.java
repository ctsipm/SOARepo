package guru.springframework.services;

import guru.springframework.domain.MessageLibraryWithRepo;
import guru.springframework.repositories.MessageLibraryWithRepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageLibraryWithRepoServiceImpl implements MessageLibraryWithRepoService {
    private MessageLibraryWithRepoRepository messagelibrarywithrepoRepository;

    @Autowired
    public void setMessageLibraryWithRepoRepository(MessageLibraryWithRepoRepository messagelibrarywithrepoRepository) {
        this.messagelibrarywithrepoRepository = messagelibrarywithrepoRepository;
    }

    @Override
    public Iterable<MessageLibraryWithRepo> listAllMessageLibraryWithRepos() {
        return messagelibrarywithrepoRepository.findAll();
    }

    @Override
    public MessageLibraryWithRepo getMessageLibraryWithRepoById(Integer id) {
        return messagelibrarywithrepoRepository.findOne(id);
    }

    @Override
    public MessageLibraryWithRepo saveMessageLibraryWithRepo(MessageLibraryWithRepo messagelibrarywithrepo) {
        return messagelibrarywithrepoRepository.save(messagelibrarywithrepo);
    }

    @Override
    public void deleteMessageLibraryWithRepo(Integer id) {
        messagelibrarywithrepoRepository.delete(id);
    }
}
