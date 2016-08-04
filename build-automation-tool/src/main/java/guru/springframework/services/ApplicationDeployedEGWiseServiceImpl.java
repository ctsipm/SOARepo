package guru.springframework.services;

import guru.springframework.domain.ApplicationDeployedEGWise;
import guru.springframework.repositories.ApplicationDeployedEGWiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationDeployedEGWiseServiceImpl implements ApplicationDeployedEGWiseService {
    private ApplicationDeployedEGWiseRepository applicationdeployedegwiseRepository;

    @Autowired
    public void setApplicationDeployedEGWiseRepository(ApplicationDeployedEGWiseRepository applicationdeployedegwiseRepository) {
        this.applicationdeployedegwiseRepository = applicationdeployedegwiseRepository;
    }

    @Override
    public Iterable<ApplicationDeployedEGWise> listAllApplicationDeployedEGWises() {
        return applicationdeployedegwiseRepository.findAll();
    }

    @Override
    public ApplicationDeployedEGWise getApplicationDeployedEGWiseById(Integer id) {
        return applicationdeployedegwiseRepository.findOne(id);
    }

    @Override
    public ApplicationDeployedEGWise saveApplicationDeployedEGWise(ApplicationDeployedEGWise applicationdeployedegwise) {
        return applicationdeployedegwiseRepository.save(applicationdeployedegwise);
    }

    @Override
    public void deleteApplicationDeployedEGWise(Integer id) {
        applicationdeployedegwiseRepository.delete(id);
    }
}
