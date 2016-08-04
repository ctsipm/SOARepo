package guru.springframework.services;

import guru.springframework.domain.MessageApplicationWithRepo;
import guru.springframework.repositories.MessageApplicationWithRepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageApplicationWithRepoServiceImpl implements MessageApplicationWithRepoService {
    private MessageApplicationWithRepoRepository messageapplicationwithrepoRepository;

    @Autowired
    public void setMessageApplicationWithRepoRepository(MessageApplicationWithRepoRepository messageapplicationwithrepoRepository) {
        this.messageapplicationwithrepoRepository = messageapplicationwithrepoRepository;
    }

    @Override
    public Iterable<MessageApplicationWithRepo> listAllMessageApplicationWithRepos() {
        return messageapplicationwithrepoRepository.findAll();
    }

    @Override
    public MessageApplicationWithRepo getMessageApplicationWithRepoById(Integer id) {
        return messageapplicationwithrepoRepository.findOne(id);
    }

    @Override
    public MessageApplicationWithRepo saveMessageApplicationWithRepo(MessageApplicationWithRepo messageapplicationwithrepo) {
        return messageapplicationwithrepoRepository.save(messageapplicationwithrepo);
    }

    @Override
    public void deleteMessageApplicationWithRepo(Integer id) {
        messageapplicationwithrepoRepository.delete(id);
    }
}
