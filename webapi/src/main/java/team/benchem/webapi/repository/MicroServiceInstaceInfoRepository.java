package team.benchem.webapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.benchem.webapi.entity.MicroServiceInstaceInfo;

import java.util.List;

@Repository
public interface MicroServiceInstaceInfoRepository extends CrudRepository<MicroServiceInstaceInfo,String> {
    List<MicroServiceInstaceInfo> findAllByServiceKey(String serviceKey);
    MicroServiceInstaceInfo findByUrl(String url);
}
