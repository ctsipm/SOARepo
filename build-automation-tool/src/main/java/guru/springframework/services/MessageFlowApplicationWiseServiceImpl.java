package guru.springframework.services;


import guru.springframework.domain.MessageFlowApplicationWise;
import guru.springframework.repositories.MessageFlowApplicationWiseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageFlowApplicationWiseServiceImpl implements MessageFlowApplicationWiseService {
    private MessageFlowApplicationWiseRepository messageapplicationegwiseRepository;

    @Autowired
    public void setMessageFlowApplicationWiseRepository(MessageFlowApplicationWiseRepository messageapplicationegwiseRepository) {
        this.messageapplicationegwiseRepository = messageapplicationegwiseRepository;
    }

    @Override
    public Iterable<MessageFlowApplicationWise> listAllMessageFlowApplicationWises() {
    	
    	return messageapplicationegwiseRepository.findAll();
    }

    @Override
    public MessageFlowApplicationWise getMessageFlowApplicationWiseById(Integer id) {
        return messageapplicationegwiseRepository.findOne(id);
    }

    @Override
    public MessageFlowApplicationWise saveMessageFlowApplicationWise(MessageFlowApplicationWise messageapplicationegwise) {
        return messageapplicationegwiseRepository.save(messageapplicationegwise);
    }

    @Override
    public void deleteMessageFlowApplicationWise(Integer id) {
        messageapplicationegwiseRepository.delete(id);
    }
}
