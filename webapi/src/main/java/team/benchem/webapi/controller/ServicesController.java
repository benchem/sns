package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONArray;
import org.apache.tomcat.util.codec.binary.Base64;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@RestController
@RequestMapping("/svc")
public class ServicesController {

    @Autowired
    MicroServiceInfoRepository microServiceInfoRepository;
    RSAUtils rsaUtils;

    /**
     * 微服务注册
     * http://yapi.lonntec.cn/project/20/interface/api/62
     *
     * @param args
     * @return
     */
    @RequestMapping("/register")
    public JSONObject register(@RequestBody JSONObject args) {
        try {

            String key = args.getString("key");
            String desc = args.getString("desc");
            JSONObject jso = new JSONObject();
            MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(key);
            if (microServiceInfo != null) {
                jso.put("key", microServiceInfo.getServiceName());
                jso.put("pubkey", microServiceInfo.getRas_pubKey());
                jso.put("desc", microServiceInfo.getDesc());
                return jso;
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
    @RequestMapping("/unregister")
    public JSONObject unregister(@RequestBody JSONObject args) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        JSONObject rs = new JSONObject();
        String token = args.getString("token");
        String key = args.getString("key");
        MicroServiceInfo microServiceInfo = microServiceInfoRepository.findByServiceName(key);
        if (microServiceInfo == null) {
            rs.put("msg", "无法找到微服务");
            return rs;
        }
        String privateKey = microServiceInfo.getRas_priKey();
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
    @RequestMapping("/getlist")
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
}
