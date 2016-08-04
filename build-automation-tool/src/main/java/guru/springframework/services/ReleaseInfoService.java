package guru.springframework.services;


import guru.springframework.domain.ReleaseInfo;

public interface ReleaseInfoService {
    Iterable<ReleaseInfo> listAllReleaseInfos();

    ReleaseInfo getReleaseInfoById(Integer id);

    ReleaseInfo saveReleaseInfo(ReleaseInfo releaseinfo);

    void deleteReleaseInfo(Integer id);
}
