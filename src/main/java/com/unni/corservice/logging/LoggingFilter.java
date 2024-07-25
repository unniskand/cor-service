package com.unni.corservice.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log request details
        logRequestDetails(requestWrapper);

        // Proceed with the filter chain
        chain.doFilter(requestWrapper, response);

        // Log response details
        logResponseDetails(requestWrapper, httpResponse);
    }

    private void logRequestDetails(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        var clientId = null == request.getHeader("x-client-id") ? Strings.EMPTY : request.getHeader("x-client-id");
        var userAgent = null == request.getHeader("user-agent") ? Strings.EMPTY : request.getHeader("user-agent");
        String formattedUrl = formatUriForLogging(request.getRequestURI());

        log.info("xClientId={} userAgent={} httpMethod={} uri={} bodyLength={} headers={} payload={}",
                clientId, userAgent, request.getMethod(), formattedUrl, request.getContentLength(),
                getHeaders(request), null);
    }

    private void logResponseDetails(HttpServletRequest request, HttpServletResponse response) {
        long startTime = (long) request.getAttribute("startTime");
        var clientId = null == request.getHeader("x-client-id") ? Strings.EMPTY : request.getHeader("x-client-id");
        var userAgent = null == request.getHeader("user-agent") ? Strings.EMPTY : request.getHeader("user-agent");
        String formattedUrl = formatUriForLogging(request.getRequestURI());
        log.info("requestId={} xClientId={} userAgent={} httpMethod={} uri={} statusCode={} responseTimeInMs={}",
                null, clientId, userAgent, request.getMethod(), formattedUrl,
                response.getStatus(), System.currentTimeMillis() - startTime);
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, maskSensitiveHeader(headerName, headerValue));
        }
        return headers;
    }

    private String getPayload(HttpServletRequest request) {
        if (request instanceof RequestWrapper) {
            return ((RequestWrapper) request).getBody();
        }
        return "";
    }

    private String maskSensitiveHeader(String headerName, String headerValue) {
        // Define sensitive headers
        if (headerName.equalsIgnoreCase("Authorization") || headerName.equalsIgnoreCase("Cookie")) {
            return "****";
        }
        return headerValue;
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

    @Override
    public void destroy() {
    }
}
