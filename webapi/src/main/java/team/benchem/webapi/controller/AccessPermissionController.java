package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.benchem.webapi.bean.AccessPermission;
import team.benchem.webapi.bean.MicroServiceInfo;
import team.benchem.webapi.repository.AccessPermissionRepository;
import team.benchem.webapi.repository.MicroServiceInfoRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;

@RestController
@RequestMapping("/access")
public class AccessPermissionController {

    @Autowired
    MicroServiceInfoRepository microServiceInfoRepository;
    @Autowired
    AccessPermissionRepository accessPermissionRepository;

    @PostMapping("/setdetail")
    public JSONObject setDetail(@RequestBody JSONObject args) {
        JSONObject rs = new JSONObject();
        String serviceKey = args.getString("svc_key");
        int accessType = args.getInteger("access_type");
        JSONArray callerKeys = args.getJSONArray("list");
        // todo 参数校验

        MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(serviceKey);
        if (microServiceInfo == null) {
            rs.put("msg", "svc_key错误");
            return rs;
        }

        Iterable<AccessPermission> accessPermissions = accessPermissionRepository.findAllByServceKey(serviceKey);
        accessPermissionRepository.deleteAll(accessPermissions);


        if (callerKeys.isEmpty()) {
            AccessPermission newAccessPermission = new AccessPermission();
            newAccessPermission.setAccessType(accessType);
            newAccessPermission.setServceKey(serviceKey);
            newAccessPermission.setCallerKey("");
            accessPermissionRepository.save(newAccessPermission);
        } else {
            ArrayList<AccessPermission> newAllAccessPermission = new ArrayList<AccessPermission>();
            for (Object subObj : callerKeys) {
                String callerKey = ((JSONObject) subObj).getString("caller_key");
                AccessPermission newAccessPermission = new AccessPermission();
                newAccessPermission.setAccessType(accessType);
                newAccessPermission.setServceKey(serviceKey);
                newAccessPermission.setCallerKey(callerKey);
                newAllAccessPermission.add(newAccessPermission);
            }
            accessPermissionRepository.saveAll(newAllAccessPermission);
        }
        rs.put("msg", "success");
        return rs;
    }


    @GetMapping("/getdetail")
    public JSONObject getDetail(@NotNull @RequestParam String key) {
        JSONObject rs = new JSONObject();
        Iterable<AccessPermission> accessPermissions = accessPermissionRepository.findAllByServceKey(key);
        ArrayList<AccessPermission> accessPermissionArrayList = Lists.newArrayList(accessPermissions);

        if (accessPermissionArrayList.isEmpty()) {
            rs.put("msg", "key 错误");
            return rs;
        }
        rs.put("svc_key", key);
        rs.put("access_type", accessPermissionArrayList.get(0).getAccessType());
        JSONArray ja = new JSONArray();
        rs.put("list", ja);
        for (AccessPermission accessPermission : accessPermissionArrayList) {
            JSONObject subObj = new JSONObject();
            subObj.put("caller_key", accessPermission.getCallerKey());
            ja.add(subObj);
        }
        return rs;
    }
}
