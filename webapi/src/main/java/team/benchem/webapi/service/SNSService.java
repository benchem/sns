package team.benchem.webapi.service;

import team.benchem.webapi.entity.AccessPermission;
import team.benchem.webapi.entity.InvokeToken;
import team.benchem.webapi.entity.MicroServiceInfo;
import team.benchem.webapi.entity.MicroServiceInstaceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public interface SNSService {
    // 访问权限
    AccessPermission AccessPermissionSave(AccessPermission accessPermission);

    ArrayList<AccessPermission> AccessPermissionSaveAll(ArrayList<AccessPermission> accessPermissionArrayList);

    void AccessPermissionDelete(AccessPermission accessPermission);

    void AccessPermissionDeleteAll(ArrayList<AccessPermission> accessPermissionArrayList);

    AccessPermission AccessPerssionFindByServiceKey(String serviceKey);

    ArrayList<AccessPermission> AccessPermissionFindAll();

    ArrayList<AccessPermission> AccessPermissionFindAllByServiceKey(String serviceKey);

    // 微服务相关
    MicroServiceInfo MicroServiceSave(MicroServiceInfo microServiceInfo);

    ArrayList<MicroServiceInfo> MicroServiceSaveAll(ArrayList<MicroServiceInfo> microServiceInfoArrayList);

    void MicroServiceDelete(MicroServiceInfo microServiceInfo);

    void MicroServiceDeleteAll(ArrayList<MicroServiceInfo> microServiceInfoArrayList);

    MicroServiceInfo MicroServiceFindByServiceKey(String serviceKey);

    ArrayList<MicroServiceInfo> MicroServiceFindAll();

    // 实例相关
    MicroServiceInstaceInfo MicroServiceInstaceSave(MicroServiceInstaceInfo microServiceInstaceInfo);

    ArrayList<MicroServiceInstaceInfo> MicroServiceInstaceSaveAll(ArrayList<MicroServiceInstaceInfo> microServiceInstaceInfoArrayList);

    void MicroServiceInstaceDelete(MicroServiceInstaceInfo microServiceInstaceInfo);

    void MicroServiceInstaceDeleteAll(ArrayList<MicroServiceInstaceInfo> microServiceInstaceInfoArrayList);


    ArrayList<MicroServiceInstaceInfo> MicroServiceInstaceFindAllByServiceKey(String serviceKey);

    MicroServiceInstaceInfo MicroServiceInstaceFindByUrl(String url);

    ArrayList<MicroServiceInstaceInfo> MicroServiceInstaceFindAll();


    // webapi
    ArrayList<AccessPermission> WebApiAccessSetDetail(MicroServiceInfo microServiceInfo, ArrayList<AccessPermission> accessPermissionArrayList);

    void WebApiSvcUnregister(String key);

    //服务验权
    //@param sourceServiceName 发起远程调用的微服务名称
    //@param targetServiceName 被请求调用的微服务名称
    //@param requestToken 发起远程调用的微服务凭证
    //@return 远程服务调用令牌
    InvokeToken serviceVerification(String sourceServiceName, String targetServiceName, String requestToken);
}
