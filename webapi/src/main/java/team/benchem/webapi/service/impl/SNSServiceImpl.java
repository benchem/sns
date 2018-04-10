package team.benchem.webapi.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.benchem.webapi.entity.AccessPermission;
import team.benchem.webapi.entity.MicroServiceInfo;
import team.benchem.webapi.entity.MicroServiceInstaceInfo;
import team.benchem.webapi.repository.AccessPermissionRepository;
import team.benchem.webapi.repository.MicroServiceInfoRepository;
import team.benchem.webapi.repository.MicroServiceInstaceInfoRepository;
import team.benchem.webapi.service.SNSService;

import java.util.ArrayList;

@Service
@Transactional
public class SNSServiceImpl implements SNSService {

    @Autowired
    AccessPermissionRepository accessPermissionRepository;

    @Autowired
    MicroServiceInfoRepository microServiceInfoRepository;

    @Autowired
    MicroServiceInstaceInfoRepository microServiceInstaceInfoRepository;

    @Override
    public AccessPermission AccessPermissionSave(AccessPermission accessPermission) {
        accessPermissionRepository.save(accessPermission);
        return accessPermission;
    }

    @Override
    public ArrayList<AccessPermission> AccessPermissionSaveAll(ArrayList<AccessPermission> accessPermissionArrayList) {
        accessPermissionRepository.saveAll(accessPermissionArrayList);
        return accessPermissionArrayList;
    }

    @Override
    public void AccessPermissionDelete(AccessPermission accessPermission) {
        accessPermissionRepository.delete(accessPermission);
    }

    @Override
    public void AccessPermissionDeleteAll(ArrayList<AccessPermission> accessPermissionArrayList) {
        accessPermissionRepository.deleteAll(accessPermissionArrayList);
    }

    @Override
    public AccessPermission AccessPerssionFindByServiceKey(String serviceKey) {
        return accessPermissionRepository.findByServceKey(serviceKey);
    }

    @Override
    public ArrayList<AccessPermission> AccessPermissionFindAll() {
        return Lists.newArrayList(accessPermissionRepository.findAll());
    }

    @Override
    public ArrayList<AccessPermission> AccessPermissionFindAllByServiceKey(String serviceKey) {
        return Lists.newArrayList(accessPermissionRepository.findAllByServceKey(serviceKey));
    }

    @Override
    public ArrayList<AccessPermission> WebApiAccessSetDetail(MicroServiceInfo microServiceInfo, ArrayList<AccessPermission> accessPermissionArrayList) {
        accessPermissionRepository.deleteByServceKey(microServiceInfo.getServiceName());
        accessPermissionRepository.saveAll(accessPermissionArrayList);
        return accessPermissionArrayList;
    }


    @Override
    public MicroServiceInfo MicroServiceSave(MicroServiceInfo microServiceInfo) {
        microServiceInfoRepository.save(microServiceInfo);
        return microServiceInfo;
    }

    @Override
    public ArrayList<MicroServiceInfo> MicroServiceSaveAll(ArrayList<MicroServiceInfo> microServiceInfoArrayList) {
        microServiceInfoRepository.saveAll(microServiceInfoArrayList);
        return microServiceInfoArrayList;
    }

    @Override
    public void MicroServiceDelete(MicroServiceInfo microServiceInfo) {
        microServiceInfoRepository.delete(microServiceInfo);
    }

    @Override
    public void MicroServiceDeleteAll(ArrayList<MicroServiceInfo> microServiceInfoArrayList) {
        microServiceInfoRepository.deleteAll(microServiceInfoArrayList);
    }

    @Override
    public MicroServiceInfo MicroServiceFindByServiceKey(String serviceKey) {
        return microServiceInfoRepository.findByServiceName(serviceKey);
    }

    @Override
    public ArrayList<MicroServiceInfo> MicroServiceFindAll() {
        return Lists.newArrayList(microServiceInfoRepository.findAll());
    }

    @Override
    public MicroServiceInstaceInfo MicroServiceInstaceSave(MicroServiceInstaceInfo microServiceInstaceInfo) {
        microServiceInstaceInfoRepository.save(microServiceInstaceInfo);
        return microServiceInstaceInfo;
    }

    @Override
    public ArrayList<MicroServiceInstaceInfo> MicroServiceInstaceSaveAll(ArrayList<MicroServiceInstaceInfo> microServiceInstaceInfoArrayList) {
        microServiceInstaceInfoRepository.saveAll(microServiceInstaceInfoArrayList);
        return microServiceInstaceInfoArrayList;
    }

    @Override
    public void MicroServiceInstaceDelete(MicroServiceInstaceInfo microServiceInstaceInfo) {
        microServiceInstaceInfoRepository.delete(microServiceInstaceInfo);
    }

    @Override
    public void MicroServiceInstaceDeleteAll(ArrayList<MicroServiceInstaceInfo> microServiceInstaceInfoArrayList) {
        microServiceInstaceInfoRepository.deleteAll(microServiceInstaceInfoArrayList);
    }

    @Override
    public ArrayList<MicroServiceInstaceInfo> MicroServiceInstaceFindAllByServiceKey(String serviceKey) {
        return Lists.newArrayList(microServiceInstaceInfoRepository.findAllByServiceKey(serviceKey));
    }

    @Override
    public MicroServiceInstaceInfo MicroServiceInstaceFindByUrl(String url) {
        return microServiceInstaceInfoRepository.findByUrl(url);
    }

    @Override
    public ArrayList<MicroServiceInstaceInfo> MicroServiceInstaceFindAll() {
        return Lists.newArrayList(microServiceInstaceInfoRepository.findAll());
    }


    //
}
