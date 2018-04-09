package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.benchem.webapi.bean.MicroServiceInfo;
import team.benchem.webapi.bean.MicroServiceInstaceInfo;
import team.benchem.webapi.repository.MicroServiceInfoRepository;
import team.benchem.webapi.repository.MicroServiceInstaceInfoRepository;
import team.benchem.webapi.utils.RSAUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/inst")
public class ServiceInstanceController {

    @Autowired
    MicroServiceInstaceInfoRepository microServiceInstaceInfoRepository;
    @Autowired
    MicroServiceInfoRepository microServiceInfoRepository;


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
        try {
            String svcKey = args.getString("svc_key");
            String desc = args.getString("desc");
            String url = args.getString("url");
            int timeout = args.getInteger("timeout");
            int weight = args.getInteger("weight");
            // todo 参数校验
            MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(svcKey);
            if (microServiceInfo == null) {
                rs.put("msg", "无效的svc_key");
                return rs;
            }
            MicroServiceInstaceInfo microServiceInstaceInfo = microServiceInstaceInfoRepository.findByUrl(url);
            if (microServiceInstaceInfo != null) {
                rs.put("id", microServiceInstaceInfo.getId());
                rs.put("svc_key", microServiceInstaceInfo.getServiceKey());
                rs.put("desc", microServiceInstaceInfo.getDesc());
                rs.put("url", microServiceInstaceInfo.getUrl());
                rs.put("timeout", microServiceInstaceInfo.getTimeout());
                rs.put("weight", microServiceInstaceInfo.getWeight());
                return rs;
            }
            MicroServiceInstaceInfo newMicroServiceInstaceInfo = new MicroServiceInstaceInfo();
            newMicroServiceInstaceInfo.setServiceKey(svcKey);
            newMicroServiceInstaceInfo.setDesc(desc);
            newMicroServiceInstaceInfo.setUrl(url);
            newMicroServiceInstaceInfo.setTimeout(timeout);
            newMicroServiceInstaceInfo.setWeight(weight);
            microServiceInstaceInfoRepository.save(newMicroServiceInstaceInfo);
            rs.put("id", newMicroServiceInstaceInfo.getId());
            rs.put("svc_key", newMicroServiceInstaceInfo.getServiceKey());
            rs.put("desc", newMicroServiceInstaceInfo.getDesc());
            rs.put("url", newMicroServiceInstaceInfo.getUrl());
            rs.put("timeout", newMicroServiceInstaceInfo.getTimeout());
            rs.put("weight", newMicroServiceInstaceInfo.getWeight());
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * 实例注销
     * http://yapi.lonntec.cn/project/20/interface/api/92
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

            MicroServiceInstaceInfo microServiceInstaceInfo = microServiceInstaceInfoRepository.findByUrl(url);
            if (microServiceInstaceInfo == null) {
                rs.put("msg", "该URL尚未注册");
                return rs;
            }

            MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(serviceKey);
            if (microServiceInfo == null) {
                rs.put("msg", "无效的svc_key");
                return rs;
            }
            String privateKeyStr = microServiceInfo.getRsa_priKey();
            String decryptUrl = RSAUtils.privateKeyDecrypt(token, privateKeyStr);
            if (decryptUrl != microServiceInstaceInfo.getUrl()) {
                rs.put("msg", "token错误");
                return rs;
            }
            microServiceInstaceInfoRepository.delete(microServiceInstaceInfo);
            rs.put("msg", "delete success");
            return rs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
