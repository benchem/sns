package team.benchem.webapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import team.benchem.webapi.bean.MicroServiceInfo;
import team.benchem.webapi.repository.MicroServiceInfoRepository;
import team.benchem.webapi.utils.RSAUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/svc")
public class ServicesController {

    @Autowired
    MicroServiceInfoRepository microServiceInfoRepository;
    RSAUtils rsaUtils;

    @RequestMapping("/register")
    public JSONObject register(@RequestBody JSONObject params) {
        try {

            String key = params.getString("key");
            String desc = params.getString("desc");
            JSONObject jso = new JSONObject();
            MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(key);
            if(microServiceInfo != null){
                jso.put("key", microServiceInfo.getServiceName());
                jso.put("pubkey", microServiceInfo.getRas_pubKey());
                jso.put("desc", microServiceInfo.getDesc());
                return  jso;
            }
            MicroServiceInfo newMicroServiceInfo = new MicroServiceInfo();
            Map<String, String> rasKeys = RSAUtils.createKeys();
            jso.put("key", newMicroServiceInfo.getServiceName());
            jso.put("pubkey", newMicroServiceInfo.getRas_pubKey());
            jso.put("desc", newMicroServiceInfo.getDesc());

            newMicroServiceInfo.setDesc(desc);
            newMicroServiceInfo.setServiceName(key);
            newMicroServiceInfo.setRas_priKey(rasKeys.get(RSAUtils.PRIVATE_NAME));
            newMicroServiceInfo.setRas_pubKey(rasKeys.get(RSAUtils.PUBLICKEY_NAME));
            microServiceInfoRepository.save(newMicroServiceInfo);
            jso.put("key", newMicroServiceInfo.getServiceName());
            jso.put("pubkey", newMicroServiceInfo.getRas_pubKey());
            jso.put("desc", newMicroServiceInfo.getDesc());
            return  jso;

        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
