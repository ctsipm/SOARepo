package guru.springframework.services;

import guru.springframework.domain.ManualBARCreationAppWiseInfo;
import guru.springframework.repositories.ManualBARCreationAppWiseInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManualBARCreationLibWiseInfoServiceImpl implements ManualBARCreationAppWiseInfoService {
    private ManualBARCreationAppWiseInfoRepository manualbarcreationappwiseinfoRepository;

    @Autowired
    public void setManualBARCreationAppWiseInfoRepository(ManualBARCreationAppWiseInfoRepository manualbarcreationappwiseinfoRepository) {
        this.manualbarcreationappwiseinfoRepository = manualbarcreationappwiseinfoRepository;
    }

    @Override
    public Iterable<ManualBARCreationAppWiseInfo> listAllManualBARCreationAppWiseInfos() {
        return manualbarcreationappwiseinfoRepository.findAll();
    }

    @Override
    public ManualBARCreationAppWiseInfo getManualBARCreationAppWiseInfoById(Integer id) {
        return manualbarcreationappwiseinfoRepository.findOne(id);
    }

    @Override
    public ManualBARCreationAppWiseInfo saveManualBARCreationAppWiseInfo(ManualBARCreationAppWiseInfo manualbarcreationappwiseinfo) {
        return manualbarcreationappwiseinfoRepository.save(manualbarcreationappwiseinfo);
    }

    @Override
    public void deleteManualBARCreationAppWiseInfo(Integer id) {
        manualbarcreationappwiseinfoRepository.delete(id);
    }
}
