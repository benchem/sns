package team.benchem.webapi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.benchem.webapi.entity.AccessPermission;
import team.benchem.webapi.entity.InvokeToken;
import team.benchem.webapi.entity.MicroServiceInfo;
import team.benchem.webapi.entity.MicroServiceInstaceInfo;
import team.benchem.webapi.service.SNSService;
import team.benchem.webapi.utils.MicroServiceException;
import team.benchem.webapi.utils.RSAUtils;

@DataJpaTest
@RunWith(SpringRunner.class)
@SpringBootTest
public class SNSServiceInvokeTest {

    @Autowired
    SNSService snsService;


    @Before
    public void beforeTest(){

        MicroServiceInfo paySerivce = new MicroServiceInfo();
        paySerivce.setServiceName("payService");
        paySerivce.setAccessType(1);
        snsService.MicroServiceSave(paySerivce);

        MicroServiceInstaceInfo paySvrInstance = new MicroServiceInstaceInfo();
        paySvrInstance.setServiceKey(paySerivce.getServiceName());
        paySvrInstance.setUrl("http://127.0.0.1:9105");
        snsService.MicroServiceInstaceSave(paySvrInstance);

        AccessPermission payPermission = new AccessPermission();
        payPermission.setServceKey(paySerivce.getServiceName());
        payPermission.setCallerKey("billService");
        snsService.AccessPermissionSave(payPermission);

    }

    @Test
    public void test_serviceVerification_case1(){

        MicroServiceInfo billService = new MicroServiceInfo();
        billService.setServiceName("billService");
        billService.setAccessType(0);
        snsService.MicroServiceSave(billService);

        String requestToken;
        try{
            requestToken = RSAUtils.publicKeyEncrypt(
                    "billService",
                    billService.getRsa_pubKey()
            );
        }catch (Exception ex){
            requestToken = "";
        }

        InvokeToken token = snsService.serviceVerification(
                "billService",
                "payService",
                requestToken);

        Assert.assertEquals(token.getInstanceUrl(), "http://127.0.0.1:9105");

    }

    @Test(expected = MicroServiceException.class)
    public void test_serviceVerification_case2(){

        MicroServiceInfo stockService = new MicroServiceInfo();
        stockService.setServiceName("stockService");
        stockService.setAccessType(0);
        snsService.MicroServiceSave(stockService);

        String requestToken;
        try{
            requestToken = RSAUtils.publicKeyEncrypt(
                    "stockService",
                    stockService.getRsa_pubKey()
            );
        }catch (Exception ex){
            requestToken = "";
        }

        snsService.serviceVerification(
                "stockService",
                "payService",
                requestToken);

    }
}
