package guru.springframework.services;


import guru.springframework.domain.ApplicationDeployedEGWise;

public interface ApplicationDeployedEGWiseService {
    Iterable<ApplicationDeployedEGWise> listAllApplicationDeployedEGWises();

    ApplicationDeployedEGWise getApplicationDeployedEGWiseById(Integer id);

    ApplicationDeployedEGWise saveApplicationDeployedEGWise(ApplicationDeployedEGWise applicationdeployedegwise);

    void deleteApplicationDeployedEGWise(Integer id);
}
