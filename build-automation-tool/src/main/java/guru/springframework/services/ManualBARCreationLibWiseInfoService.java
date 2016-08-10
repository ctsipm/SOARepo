package guru.springframework.services;


import guru.springframework.domain.ManualBARCreationAppWiseInfo;

public interface ManualBARCreationLibWiseInfoService {
    Iterable<ManualBARCreationAppWiseInfo> listAllManualBARCreationAppWiseInfos();

    ManualBARCreationAppWiseInfo getManualBARCreationAppWiseInfoById(Integer id);

    ManualBARCreationAppWiseInfo saveManualBARCreationAppWiseInfo(ManualBARCreationAppWiseInfo manualbarcreationappwiseinfo);

    void deleteManualBARCreationAppWiseInfo(Integer id);
}
