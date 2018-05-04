package team.benchem.webapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.benchem.webapi.entity.MicroServiceInfo;

@Repository
public interface MicroServiceInfoRepository extends CrudRepository<MicroServiceInfo, String> {
    MicroServiceInfo findByServiceName(String serviceName);
}

