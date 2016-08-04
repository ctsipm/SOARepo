package guru.springframework.services;


import guru.springframework.domain.LibraryDeployedEGWise;

public interface LibraryDeployedEGWiseService {
    Iterable<LibraryDeployedEGWise> listAllLibraryDeployedEGWises();

    LibraryDeployedEGWise getLibraryDeployedEGWiseById(Integer id);

    LibraryDeployedEGWise saveLibraryDeployedEGWise(LibraryDeployedEGWise librarydeployedegwise);

    void deleteLibraryDeployedEGWise(Integer id);
}
