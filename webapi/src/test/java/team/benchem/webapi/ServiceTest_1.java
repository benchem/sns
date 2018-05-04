package team.benchem.webapi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import team.benchem.webapi.entity.MicroServiceInfo;
import team.benchem.webapi.service.SNSService;

import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest_1 {

    @MockBean
    private SNSService mockService;

    @Autowired
    private SNSService snsService;

    @Before
    public void setUp(){

    }

    @Test
    public void test1(){
        String key = "dd";
        MicroServiceInfo a = new MicroServiceInfo();
        given(this.mockService.MicroServiceFindByServiceKey(key)).willReturn(a);


    }
}
