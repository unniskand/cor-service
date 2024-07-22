package com.unni.corservice.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;

import static feign.Logger.Level.HEADERS;

@Slf4j
public class CustomFeignRequestLogging extends Logger {

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (logLevel.ordinal() >= HEADERS.ordinal()) {
            super.logRequest(configKey, logLevel, request);
        } else {
            int bodyLength = 0;
            if (request.body() != null) {
                bodyLength = request.body().length;
            }
            var url = URI.create(request.url()).getRawPath();
            String formatterUrl = formatUriForLogging(url);
            log(configKey, "httpMethod=%s url=%s bodyLength=%s",
                    request.httpMethod().name(), formatterUrl, bodyLength);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        if (logLevel.ordinal() >= HEADERS.ordinal()) {
            super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
        } else {
            int status = response.status();
            Request request = response.request();
            var url = URI.create(request.url()).getRawPath();
            String formatterUrl = formatUriForLogging(url);
            log(configKey, "httpMethod=%s url=%s httpStatus=%s reponseTimeInMs=%s",
                    request.httpMethod().name(), formatterUrl, status, elapsedTime);
        }
        return response;
    }

    private String formatUriForLogging(String url) {
        return url.replaceAll("/(\\d+-)*(\\d+)$", "/{id}")
                .replaceAll("/(\\d+-)*(\\d+)/", "/{id}/");
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        log.info(format(configKey, format, args));
    }

    protected String format(String configKey, String format, Object... args) {
        return String.format(methodTag(configKey) + format, args);
    }
}
