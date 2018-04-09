package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONArray;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import team.benchem.webapi.bean.MicroServiceInfo;
import team.benchem.webapi.bean.MicroServiceInstaceInfo;
import team.benchem.webapi.repository.MicroServiceInfoRepository;
import team.benchem.webapi.repository.MicroServiceInstaceInfoRepository;
import team.benchem.webapi.utils.RSAUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.constraints.NotNull;
import java.security.InvalidKeyException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@RestController
@RequestMapping("/svc")
public class ServicesController {

    @Autowired
    MicroServiceInfoRepository microServiceInfoRepository;

    @Autowired
    MicroServiceInstaceInfoRepository microServiceInstaceInfoRepository;

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
            JSONObject jso = new JSONObject();
            if (args.isEmpty()) {
                throw new RuntimeException("参数为空");
            }
            String key = args.containsKey("key") ? args.getString("key") : "";
            String desc = args.containsKey("key") ? args.getString("desc") : "";
            if (key.isEmpty() || desc.isEmpty()) {
                throw new RuntimeException("参数为空");
            }
            MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(key);
            if (microServiceInfo != null) {
                jso.put("key", microServiceInfo.getServiceName());
                jso.put("pubkey", microServiceInfo.getRsa_pubKey());
                jso.put("desc", microServiceInfo.getDesc());
                return jso;
            }

            Map<String, String> rasKeys = RSAUtils.createKeys();
            //MicroServiceInfo newMicroServiceInfo = new MicroServiceInfo(key,rasKeys.get(RSAUtils.PRIVATE_NAME),rasKeys.get(RSAUtils.PUBLICKEY_NAME),desc);
            MicroServiceInfo newMicroServiceInfo = new MicroServiceInfo();
            newMicroServiceInfo.setDesc(desc);
            newMicroServiceInfo.setServiceName(key);
            newMicroServiceInfo.setRsa_priKey(rasKeys.get(RSAUtils.PRIVATE_NAME));
            newMicroServiceInfo.setRsa_pubKey(rasKeys.get(RSAUtils.PUBLICKEY_NAME));
            microServiceInfoRepository.save(newMicroServiceInfo);
            jso.put("key", newMicroServiceInfo.getServiceName());
            jso.put("pubkey", newMicroServiceInfo.getRsa_pubKey());
            jso.put("desc", newMicroServiceInfo.getDesc());
            return jso;

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
        MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(key);
        if (microServiceInfo == null) {
            rs.put("msg", "无法找到微服务");
            return rs;
        }
        String privateKey = microServiceInfo.getRsa_priKey();
        String decryptKey = RSAUtils.privateKeyDecrypt(token, privateKey);
        if (decryptKey == microServiceInfo.getServiceName()) {
            microServiceInfoRepository.delete(microServiceInfo);
            rs.put("msg", "删除成功");
            return rs;
        }
        rs.put("msg", "token错误");
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
        for (MicroServiceInfo microServiceInfo : microServiceInfoRepository.findAll()) {
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
        MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(key);
        if (microServiceInfo == null) {
            rs.put("msg", "微服务KEY无效");
            return rs;
        }
        rs.put("key",microServiceInfo.getServiceName());
        rs.put("pubkey",microServiceInfo.getRsa_pubKey());
        rs.put("desc",microServiceInfo.getDesc());
        JSONArray ja = new JSONArray();
        rs.put("items",ja);
        for (MicroServiceInstaceInfo microServiceInstaceInfo :microServiceInstaceInfoRepository.findAllByServiceKey(key)){
            JSONObject subObj = new JSONObject();
            subObj.put("url",microServiceInstaceInfo.getUrl());
            subObj.put("timeout",microServiceInstaceInfo.getTimeout());
            subObj.put("weight",microServiceInstaceInfo.getWeight());
            ja.add(subObj);
        }

        return rs;
    }
}
