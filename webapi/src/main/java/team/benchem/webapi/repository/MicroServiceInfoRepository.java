package team.benchem.webapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.benchem.webapi.bean.MicroServiceInfo;

import java.util.List;

@Repository
public interface MicroServiceInfoRepository extends CrudRepository<MicroServiceInfo, String> {
    MicroServiceInfo findByServiceName(String serviceName);

}

