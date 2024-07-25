package com.unni.corservice.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

@ControllerAdvice
@ConditionalOnProperty(
        prefix = "cor-service.logger",
        name = {"active"},
        havingValue = "true",
        matchIfMissing = true
)
@AllArgsConstructor
public class LogRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    private final HttpServletRequest httpServletRequest;
    private final LoggingService loggingService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        this.loggingService.logRequest(this.httpServletRequest, body);
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
