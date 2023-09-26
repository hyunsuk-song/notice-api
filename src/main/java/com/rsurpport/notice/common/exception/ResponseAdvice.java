package com.rsurpport.notice.common.exception;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author songhyunsuk
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return Boolean.TRUE;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        if (body instanceof String) {
            return body;
        }

        if (MediaType.APPLICATION_JSON.equals(selectedContentType) && !(body instanceof ResponseWrap)) {
            return new ResponseWrap(body);
        }

        return body;
    }

    @ResponseBody
    @ExceptionHandler(CommonException.class)
    public ResponseWrap CommonException(CommonException commonException) {
        return new ResponseWrap(commonException);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseWrap exception(Exception exception) {
        return new ResponseWrap(exception);
    }
}
