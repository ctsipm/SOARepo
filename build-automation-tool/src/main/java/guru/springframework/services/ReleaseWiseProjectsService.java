package guru.springframework.services;


import guru.springframework.domain.ReleaseWiseProjects;

public interface ReleaseWiseProjectsService {
    Iterable<ReleaseWiseProjects> listAllReleaseWiseProjectss();

    ReleaseWiseProjects getReleaseWiseProjectsById(Integer id);

    ReleaseWiseProjects saveReleaseWiseProjects(ReleaseWiseProjects releasewiseprojects);

    void deleteReleaseWiseProjects(Integer id);
}
