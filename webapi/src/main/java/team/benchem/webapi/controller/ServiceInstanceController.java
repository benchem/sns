package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.benchem.webapi.entity.MicroServiceInfo;
import team.benchem.webapi.entity.MicroServiceInstaceInfo;
import team.benchem.webapi.service.SNSService;
import team.benchem.webapi.utils.RSAUtils;

@CrossOrigin
@RestController
@RequestMapping("/inst")
public class ServiceInstanceController {

    @Autowired
    SNSService snsService;


    /**
     * 实例注册
     * http://yapi.lonntec.cn/project/20/interface/api/89
     *
     * @param args
     * @return
     */
    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject args) {
        JSONObject rs = new JSONObject();

        String svcKey = args.getString("svc_key");
        String desc = args.getString("desc");
        String url = args.getString("url");
        int timeout = args.getInteger("timeout");
        int weight = args.getInteger("weight");
        // todo 参数校验
        MicroServiceInfo microServiceInfo = snsService.MicroServiceFindByServiceKey(svcKey);
        if (microServiceInfo == null) {
            throw new RuntimeException("无效的svc_key");
        }
        MicroServiceInstaceInfo microServiceInstaceInfo = new MicroServiceInstaceInfo();
        microServiceInstaceInfo.setDesc(desc);
        microServiceInstaceInfo.setServiceKey(svcKey);
        microServiceInstaceInfo.setUrl(url);
        microServiceInstaceInfo.setTimeout(timeout);
        microServiceInstaceInfo.setWeight(weight);
        snsService.MicroServiceInstaceSave(microServiceInstaceInfo);

        rs.put("id", microServiceInstaceInfo.getId());
        rs.put("svc_key", microServiceInstaceInfo.getServiceKey());
        rs.put("desc", microServiceInstaceInfo.getDesc());
        rs.put("url", microServiceInstaceInfo.getUrl());
        rs.put("timeout", microServiceInstaceInfo.getTimeout());
        rs.put("weight", microServiceInstaceInfo.getWeight());
        return rs;
    }


    /**
     * 实例注销
     * http://yapi.lonntec.cn/project/20/interface/api/92
     *
     * @param args
     * @return
     */
    @PostMapping("/unregister")
    public JSONObject unregister(@RequestBody JSONObject args) {
        try {

            // todo 参数校验
            JSONObject rs = new JSONObject();
            String serviceKey = args.getString("svc_key");
            String url = args.getString("url");
            String token = args.getString("token");

            MicroServiceInfo microServiceInfo = snsService.MicroServiceFindByServiceKey(serviceKey);
            if (microServiceInfo == null) {
                throw new RuntimeException("无效的svc_key");
            }

            MicroServiceInstaceInfo microServiceInstaceInfo = snsService.MicroServiceInstaceFindByUrl(url);
            if (microServiceInstaceInfo == null) {
                throw new RuntimeException("无效的URL");
            }

            String privateKeyStr = microServiceInfo.getRsa_priKey();
            String decryptUrl = RSAUtils.privateKeyDecrypt(token, privateKeyStr);
            if (decryptUrl != microServiceInstaceInfo.getUrl()) {
                throw new RuntimeException("token错误");
            }
            snsService.MicroServiceInstaceDelete(microServiceInstaceInfo);
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
