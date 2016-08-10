package guru.springframework.services;


import guru.springframework.domain.BuildManually;

public interface BuildManuallyService {
    Iterable<BuildManually> listAllBuildManuallys();

    BuildManually getBuildManuallyById(Integer id);

    BuildManually saveBuildManually(BuildManually BuildManually);

    void deleteBuildManually(Integer id);
}
