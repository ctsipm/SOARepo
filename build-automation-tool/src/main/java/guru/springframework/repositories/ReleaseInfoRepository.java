package guru.springframework.repositories;


import guru.springframework.domain.ReleaseInfo;

import org.springframework.data.repository.CrudRepository;

public interface ReleaseInfoRepository extends CrudRepository<ReleaseInfo, Integer>{
}
