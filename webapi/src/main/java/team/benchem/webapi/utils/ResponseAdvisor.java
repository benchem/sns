package team.benchem.webapi.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import team.benchem.webapi.entity.Result;

@ControllerAdvice
public class ResponseAdvisor implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleUnhandleException(Exception ex, WebRequest request) {
        String errMsg = ex.getMessage();
        if(errMsg != null && !errMsg.isEmpty()){
            ex.printStackTrace();
        }
        Result result = new Result(-1, ex.getMessage());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result) return body;
        return new Result(body);
    }
}

