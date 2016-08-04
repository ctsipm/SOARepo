package guru.springframework.services;


import guru.springframework.domain.MessageFlowApplicationWise;

public interface MessageFlowApplicationWiseService {
	
    Iterable<MessageFlowApplicationWise> listAllMessageFlowApplicationWises();

    MessageFlowApplicationWise getMessageFlowApplicationWiseById(Integer id);

    MessageFlowApplicationWise saveMessageFlowApplicationWise(MessageFlowApplicationWise messageapplicationegwise);

    void deleteMessageFlowApplicationWise(Integer id);
}
