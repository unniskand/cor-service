package com.unni.corservice.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConditionalOnBean({LoggingService.class})
public class MaskedLoggingService implements LoggingService {

    private static final Logger log = LoggerFactory.getLogger(MaskedLoggingService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper(); //TODO

    @Value("${corservice.logger.log-parsing-errors: false}")
    private boolean logParsingErrors;

    @Override
    public void logRequest(HttpServletRequest request, Object body) {
        StringBuilder output = new StringBuilder("REQUEST >> ");
        Map<String, List<String>> headers = this.buildHeadersMap(request);
        output.append("method=").append(request.getMethod()).append(" ");
        output.append("path=").append(request.getRequestURI()).append(" ");
        output.append("apiOperation=").append(request.getMethod()).append(" "); //TODO
        output.append("headers=").append(this.safeStringify(headers)).append(" ");

        Map<String, String[]> parameters = request.getParameterMap();
        if (!parameters.isEmpty()) {
            output.append("parameters=").append(this.safeStringify(parameters)).append(" ");
        }

        if (body != null) {
            output.append("body=").append(this.safeStringify(body));
        }
        log.info(output.toString().trim());
    }

    @Override
    public void logResponse(HttpServletResponse httpServletResponse, Object body) {

    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {

    }

    private Map<String, List<String>> buildHeadersMap(final HttpServletRequest request) {
        return (Map)Collections.list(request.getHeaderNames()).stream().distinct()
                .collect(Collectors.toMap(Function.identity(), (header) -> {
                    return (List)Collections.list(request.getHeaders(header)).stream().map((value) -> {
                        return this.isSensitiveHeader(header) ? "**********" : value;
                    }).collect(Collectors.toList());
                }));
    }

    private boolean isSensitiveHeader(String header) {
        return "Authorization".equalsIgnoreCase(header);
    }

    private Object safeStringify(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException var) {
            return this.logParsingErrors ? data : "<[REDACTED] PARSING ERRORS: JsonProcessingException]>";
        }
    }
}
