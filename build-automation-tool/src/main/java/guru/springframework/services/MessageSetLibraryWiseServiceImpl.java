package guru.springframework.services;


import guru.springframework.domain.MessageSetLibraryWise;
import guru.springframework.repositories.MessageSetLibraryWiseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSetLibraryWiseServiceImpl implements MessageSetLibraryWiseService {
    private MessageSetLibraryWiseRepository messagesetlibrarywiseRepository;

    @Autowired
    public void setMessageSetLibraryWiseRepository(MessageSetLibraryWiseRepository messagesetlibrarywiseRepository) {
        this.messagesetlibrarywiseRepository = messagesetlibrarywiseRepository;
    }

    @Override
    public Iterable<MessageSetLibraryWise> listAllMessageSetLibraryWises() {
    	
    	return messagesetlibrarywiseRepository.findAll();
    }

    @Override
    public MessageSetLibraryWise getMessageSetLibraryWiseById(Integer id) {
        return messagesetlibrarywiseRepository.findOne(id);
    }

    @Override
    public MessageSetLibraryWise saveMessageSetLibraryWise(MessageSetLibraryWise messagesetlibrarywise) {
        return messagesetlibrarywiseRepository.save(messagesetlibrarywise);
    }

    @Override
    public void deleteMessageSetLibraryWise(Integer id) {
        messagesetlibrarywiseRepository.delete(id);
    }
}
