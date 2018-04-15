package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.benchem.webapi.entity.AccessPermission;
import team.benchem.webapi.entity.MicroServiceInfo;
import team.benchem.webapi.service.SNSService;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/access")
public class AccessPermissionController {

    @Autowired
    SNSService snsService;

    @PostMapping("/setdetail")
    public JSONObject setDetail(@RequestBody JSONObject args) {
        JSONObject rs = new JSONObject();
        String serviceKey = args.getString("svc_key");
        int accessType = args.getInteger("access_type");
        JSONArray callerKeys = args.getJSONArray("list");
        // todo 参数校验

        MicroServiceInfo microServiceInfo = snsService.MicroServiceFindByServiceKey(serviceKey);
        if (microServiceInfo == null) {
            throw new RuntimeException("svc_key错误");
        }

        ArrayList<AccessPermission> accessPermissionArrayList = new ArrayList<>();
        microServiceInfo.setAccessType(accessType);
        for(int i=0; i<callerKeys.size(); i++){
            String callerKey = (String)callerKeys.get(i);
            AccessPermission accessPermission = new AccessPermission();
            accessPermission.setServceKey(serviceKey);
            accessPermission.setCallerKey(callerKey);
            accessPermissionArrayList.add(accessPermission);
        }
        snsService.WebApiAccessSetDetail(microServiceInfo, accessPermissionArrayList);
        return rs;

    }


    @GetMapping("/getdetail")
    public JSONObject getDetail(@Length(min = 3) @RequestParam String key) {
        JSONObject rs = new JSONObject();
        MicroServiceInfo microServiceInfo = snsService.MicroServiceFindByServiceKey(key);
        if (microServiceInfo == null) {
            throw new RuntimeException("Key 错误");
        }
        ArrayList<AccessPermission> accessPermissionArrayList = snsService.AccessPermissionFindAllByServiceKey(key);
        rs.put("svc_key", key);
        rs.put("access_type", microServiceInfo.getAccessType());
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
