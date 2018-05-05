package team.benchem.webapi.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import team.benchem.webapi.utils.MicroServiceException;

@ControllerAdvice
public class ResponseAdvisor implements ResponseBodyAdvice<Object> {

    private final Logger logger = LoggerFactory.getLogger(ResponseAdvisor.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        RequestContext.removeCurrentRequestContext();
        if (body instanceof Result) return body;
        return new Result(body);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleUnhandleException(Exception ex, WebRequest request) {
        logger.error(ex.getMessage());
        Result result;
        if (ex instanceof MicroServiceException) {
            MicroServiceException msEx = (MicroServiceException) ex;
            result = new Result(msEx.getStateCode().getCode(), msEx.getStateCode().getMessage());
        } else {
            result = new Result(-1, ex.getMessage());
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }
}

