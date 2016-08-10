package guru.springframework.services;

import guru.springframework.domain.BuildManually;
import guru.springframework.repositories.BuildManuallyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildManuallyServiceImpl implements BuildManuallyService {
    private BuildManuallyRepository buildmanuallyRepository;

    @Autowired
    public void setBuildManuallyRepository(BuildManuallyRepository buildmanuallyRepository) {
        this.buildmanuallyRepository = buildmanuallyRepository;
    }

    @Override
    public Iterable<BuildManually> listAllBuildManuallys() {
        return buildmanuallyRepository.findAll();
    }

    @Override
    public BuildManually getBuildManuallyById(Integer id) {
        return buildmanuallyRepository.findOne(id);
    }

    @Override
    public BuildManually saveBuildManually(BuildManually buildmanually) {
        return buildmanuallyRepository.save(buildmanually);
    }

    @Override
    public void deleteBuildManually(Integer id) {
        buildmanuallyRepository.delete(id);
    }
}
