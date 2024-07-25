package com.unni.corservice.logging;

import com.unni.corservice.logging.LoggingService;
import com.unni.corservice.logging.RequestWrapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(
        prefix = "cor-service.logger",
        name = {"active"},
        havingValue = "true",
        matchIfMissing = true
)
public class LoggingInterceptor implements HandlerInterceptor {

    private final LoggingService loggingService;
    public static final ThreadLocal<Map<String, String>> headerMap = new InheritableThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestWrapper requestWrapper = new RequestWrapper(request);
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        if (this.isLoggable(request.getDispatcherType().name(), request.getMethod())) {
            this.loggingService.logRequest(request, null);
        }

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        headerMap.set(headers);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        var clientId = null == request.getHeader("x-client-id") ? Strings.EMPTY : request.getHeader("x-client-id");
        var userAgent = null == request.getHeader("user-agent") ? Strings.EMPTY : request.getHeader("user-agent");
        String formattedUrl = formatUriForLogging(request.getRequestURI());
        log.info("requestId={} xClientId={} userAgent={} httpMethod={} uri={} apiName={} statusCode={} responseTimeInMs={}",
                null, clientId, userAgent, request.getMethod(), formattedUrl,
                getHandlerMethodName(handler.toString()), response.getStatus(),
                System.currentTimeMillis() - startTime);

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private boolean isLoggable(String dispatcherName, String requestMethod) {
        if (DispatcherType.ERROR.name().equals(dispatcherName)) {
            return true;
        } else {
            return DispatcherType.REQUEST.name().equals(dispatcherName) && HttpMethod.GET.name().equals(requestMethod);
        }
    }

    private String formatUriForLogging(String url) {
        return url.replaceAll("/(\\d+-)*(\\d+)$", "/{id}")
                .replaceAll("/(\\d+-)*(\\d+)/", "/{id}/");
    }

    private static String getHandlerMethodName(String handler) {
        int hashIndex = handler.indexOf('#');
        if (hashIndex >= 0) {
            int openParenIndex = handler.indexOf('(', hashIndex);
            if (openParenIndex >= 0) {
                return handler.substring(hashIndex + 1, openParenIndex);
            }
        }
        return null;
    }
}
