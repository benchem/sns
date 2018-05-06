package team.benchem.webapi.advisor;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import team.benchem.webapi.annotation.MicroServiceTokenValidate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@ControllerAdvice
public class RequestAdvisor implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        RequestContext currentCtx = RequestContext.getCurrentRequestContext();

        Method executeMethod = parameter.getMethod();
        if(executeMethod.isAnnotationPresent(MicroServiceTokenValidate.class)){
            HttpHeaders headers = inputMessage.getHeaders();
            Object tokenObj = headers.get("Suf-MS-Token");
            if(tokenObj == null){
                throw new RuntimeException("微服务验权出错:缺失Token");
            }
            String token = tokenObj.toString();
            if(token == "" || token.length() == 0){
                throw new RuntimeException("微服务验权出错:缺失Token");
            }
            currentCtx.properties.put("Suf-MS-Token", token);
        }

        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Nullable
    @Override
    public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
