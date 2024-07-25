package com.unni.corservice.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public interface LoggingService {

    void logRequest(HttpServletRequest httpServletRequest, Object body);

    void logResponse(HttpServletResponse httpServletResponse, Object body);

    void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body);
}
