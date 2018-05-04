package team.benchem.webapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.benchem.webapi.entity.AccessPermission;

@Repository
public interface AccessPermissionRepository extends CrudRepository<AccessPermission, String> {
    Iterable<AccessPermission> findAllByServceKey(String serviceKey);
    AccessPermission findByServceKey(String serviceKey);
    void deleteByServceKey(String serviceKey);
}
