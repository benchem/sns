package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.benchem.webapi.entity.InvokeToken;
import team.benchem.webapi.entity.InvokeType;
import team.benchem.webapi.service.SNSService;

@CrossOrigin
@RestController
@RequestMapping("/")
public class InvokeController {

    @Autowired
    SNSService snsService;

    @RequestMapping("/invoke")
    public JSONObject invoke(
            @RequestHeader("Suf-MS-SourceServiceName") String sourceServiceName,
            @RequestHeader("Suf-MS-Token") String token,
            @RequestHeader("Suf-MS-TargetServiceName") String targetServiceName,
            @RequestHeader("Suf-MS-TargetServicePath") String targetServiceMethodPath,
            @RequestHeader("Suf-MS-InvokeType") InvokeType invokeType,
            @RequestBody JSONObject formData){
        InvokeToken invokeToken = snsService.serviceVerification(sourceServiceName, targetServiceName, token);

        return formData;
    }

}
