package team.benchem.webapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.benchem.webapi.entity.InvokeToken;
import team.benchem.webapi.entity.InvokeType;
import team.benchem.webapi.entity.Result;
import team.benchem.webapi.service.SNSService;
import team.benchem.webapi.utils.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/")
public class InvokeController {

    @Autowired
    SNSService snsService;

    @RequestMapping("/invoke")
    public Object invoke(
            @RequestHeader("Suf-MS-SourceServiceName") String sourceServiceName,
            @RequestHeader("Suf-MS-Token") String token,
            @RequestHeader("Suf-MS-TargetServiceName") String targetServiceName,
            @RequestHeader("Suf-MS-TargetServicePath") String targetServiceMethodPath,
            @RequestHeader("Suf-MS-InvokeType") InvokeType invokeType,
            @RequestBody JSONObject formData){
        InvokeToken invokeToken = snsService.serviceVerification(sourceServiceName, targetServiceName, token);

        HashMap<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Suf-MS-RequestService", sourceServiceName);
        requestHeaders.put("Suf-MS-ServiceToken", invokeToken.getRequestToken());
        HystrixCommand<String> requestCmd ;
        switch (invokeType){

            case POST:
                requestCmd = new HttpPostHystrixCommand(
                        invokeToken.getInstanceUrl(),
                        targetServiceMethodPath,
                        requestHeaders,
                        formData.toJSONString());
                break;
            case GET:
                HashMap<String, Object> pararms = new HashMap<>();
                for(Map.Entry<String, Object> item : formData.entrySet()){
                    pararms.put(item.getKey(), item.getValue());
                }
                requestCmd = new HttpGetHystrixCommand(
                        invokeToken.getInstanceUrl(),
                        targetServiceMethodPath,
                        requestHeaders,
                        pararms
                );
                break;
            default:
                throw new MicroServiceException(InvokeStateCode.Illegal_RequestType);

        }

        String responseStr = requestCmd.execute();
        Result result = JSONObject.parseObject(responseStr).toJavaObject(Result.class);
        if(result.getStatecode() == 0){
            Object resultObj = result.getResult();
            return resultObj;
        }else{
            throw new MicroServiceException(new StateCodeImpl(result.getStatecode(), result.getErrmsg()));
        }
    }

}
