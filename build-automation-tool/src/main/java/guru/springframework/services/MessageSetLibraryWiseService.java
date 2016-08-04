package guru.springframework.services;


import guru.springframework.domain.MessageSetLibraryWise;

public interface MessageSetLibraryWiseService {
	
    Iterable<MessageSetLibraryWise> listAllMessageSetLibraryWises();

    MessageSetLibraryWise getMessageSetLibraryWiseById(Integer id);

    MessageSetLibraryWise saveMessageSetLibraryWise(MessageSetLibraryWise messagesetlibrarywise);

    void deleteMessageSetLibraryWise(Integer id);
}
