package guru.springframework.services;

import guru.springframework.domain.ReleaseInfo;
import guru.springframework.repositories.ReleaseInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReleaseInfoServiceImpl implements ReleaseInfoService {
    private ReleaseInfoRepository releaseinfoRepository;

    @Autowired
    public void setReleaseInfoRepository(ReleaseInfoRepository releaseinfoRepository) {
        this.releaseinfoRepository = releaseinfoRepository;
    }

    @Override
    public Iterable<ReleaseInfo> listAllReleaseInfos() {
        return releaseinfoRepository.findAll();
    }

    @Override
    public ReleaseInfo getReleaseInfoById(Integer id) {
        return releaseinfoRepository.findOne(id);
    }

    @Override
    public ReleaseInfo saveReleaseInfo(ReleaseInfo releaseinfo) {
        return releaseinfoRepository.save(releaseinfo);
    }

    @Override
    public void deleteReleaseInfo(Integer id) {
        releaseinfoRepository.delete(id);
    }
}
