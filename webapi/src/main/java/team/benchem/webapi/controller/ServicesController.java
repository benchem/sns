package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import team.benchem.webapi.entity.AccessPermission;
import team.benchem.webapi.entity.MicroServiceInfo;
import team.benchem.webapi.entity.MicroServiceInstaceInfo;
import team.benchem.webapi.service.SNSService;
import team.benchem.webapi.utils.RSAUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.constraints.NotNull;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/svc")
public class ServicesController {

    @Autowired
    SNSService snsService;

    /**
     * 微服务注册
     * http://yapi.lonntec.cn/project/20/interface/api/62
     *
     * @param args
     * @return
     */
    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject args) {
        try {
            JSONObject rs = new JSONObject();
            if (args.isEmpty()) {
                throw new RuntimeException("参数为空");
            }
            String key = args.getString("key");
            String desc = args.getString("desc");
            // TODO 参数校验

            // 存在直接返回已有对象
            MicroServiceInfo microServiceInfo = snsService.MicroServiceFindByServiceKey(key);
            if (microServiceInfo != null) {
                rs.put("key", microServiceInfo.getServiceName());
                rs.put("pubkey", microServiceInfo.getRsa_pubKey());
                rs.put("desc", microServiceInfo.getDesc());
                return rs;
            }

            Map<String, String> rasKeys = RSAUtils.createKeys();
            MicroServiceInfo newMicroServiceInfo = new MicroServiceInfo();
            newMicroServiceInfo.setDesc(desc);
            newMicroServiceInfo.setServiceName(key);
            newMicroServiceInfo.setRsa_priKey(rasKeys.get(RSAUtils.PRIVATE_NAME));
            newMicroServiceInfo.setRsa_pubKey(rasKeys.get(RSAUtils.PUBLICKEY_NAME));
            snsService.MicroServiceSave(newMicroServiceInfo);
            rs.put("key", newMicroServiceInfo.getServiceName());
            rs.put("pubkey", newMicroServiceInfo.getRsa_pubKey());
            rs.put("desc", newMicroServiceInfo.getDesc());
            return rs;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 微服务注销
     * http://yapi.lonntec.cn/project/20/interface/api/65
     *
     * @param args
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    @PostMapping("/unregister")
    public JSONObject unregister(@RequestBody JSONObject args) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, InvalidKeyException {
        JSONObject rs = new JSONObject();
        String token = args.getString("token");
        String key = args.getString("key");
        MicroServiceInfo microServiceInfo = snsService.MicroServiceFindByServiceKey(key);
        if (microServiceInfo == null) {
            throw  new RuntimeException("key 无效");
        }
        String privateKey = microServiceInfo.getRsa_priKey();
        String decryptKey = RSAUtils.privateKeyDecrypt(token, privateKey);
        if (decryptKey != microServiceInfo.getServiceName()) {
            throw  new RuntimeException("token 错误");
        }
        snsService.MicroServiceDelete(microServiceInfo);
        return rs;
    }



    @PostMapping("/unregister2")
    public JSONObject unregister2(@RequestBody JSONObject args)  {
        JSONObject rs = new JSONObject();
        String key = args.getString("serviceKey");
        snsService.WebApiSvcUnregister(key);
        return rs;
    }


    /**
     * 获取微服务列表
     * http://yapi.lonntec.cn/project/20/interface/api/71
     *
     * @return
     */
    @GetMapping("/getlist")
    public JSONArray getList() {
        JSONArray ja = new JSONArray();
        for (MicroServiceInfo microServiceInfo : snsService.MicroServiceFindAll()) {
            JSONObject jo = new JSONObject();
            jo.put("key", microServiceInfo.getServiceName());
            jo.put("desc", microServiceInfo.getDesc());
            ja.add(jo);
        }
        return ja;
    }

    /**
     * 获取微服务详细信息
     * http://yapi.lonntec.cn/project/20/interface/api/80
     *
     * @param key
     * @return
     */
    @GetMapping("/getdetail")
    public JSONObject getDetail(@NotNull @RequestParam String key) {
        JSONObject rs = new JSONObject();
        MicroServiceInfo microServiceInfo = snsService.MicroServiceFindByServiceKey(key);
        if (microServiceInfo == null) {
            rs.put("msg", "微服务KEY无效");
            return rs;
        }
        rs.put("key", microServiceInfo.getServiceName());
        rs.put("pubkey", microServiceInfo.getRsa_pubKey());
        rs.put("desc", microServiceInfo.getDesc());

        // put instace info
        JSONArray ja = new JSONArray();
        rs.put("items", ja);
        for (MicroServiceInstaceInfo microServiceInstaceInfo : snsService.MicroServiceInstaceFindAllByServiceKey(key)) {
            JSONObject subObj = new JSONObject();
            subObj.put("url", microServiceInstaceInfo.getUrl());
            subObj.put("timeout", microServiceInstaceInfo.getTimeout());
            subObj.put("weight", microServiceInstaceInfo.getWeight());
            ja.add(subObj);
        }

        // put access info
        ja = new JSONArray();
        rs.put("access_type", microServiceInfo.getAccessType());
        rs.put("access_list", ja);
        ArrayList<AccessPermission> accessPermissionArrayList = snsService.AccessPermissionFindAllByServiceKey(key);
        for (AccessPermission accessPermission : accessPermissionArrayList) {
            JSONObject subObj = new JSONObject();
            subObj.put("caller_key", accessPermission.getCallerKey());
            ja.add(subObj);
        }
        return rs;

    }
}
