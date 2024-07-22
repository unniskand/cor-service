package com.unni.corservice.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode implements Serializable {

    INVALID_PAYLOAD_IN_REQUEST(HttpStatus.BAD_REQUEST, 2000, HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Invalid payload in request."),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, -1000, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            "Unknown error code.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
    private final String info;

    ErrorCode(HttpStatus httpStatus, int code, String message, String info) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.info = info;
    }

    @JsonCreator
    static ErrorCode findValue(ErrorCodeHelper errorCodeHelper) {
        return Arrays.stream(ErrorCode.values())
                .filter(value -> value.code == errorCodeHelper.getCode())
                .findFirst()
                .orElse(ErrorCode.UNKNOWN);
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ErrorCodeHelper {
        private int code;
    }

    @Override
    public String toString() {
        return "{\"httpStatusCode\":\"" + this.httpStatus.value() + "\", \"code\":\""+ this.code + "\", "
                + "\"message\": \"" + this.message + "\", \"info\":\"" + this.info + "\"}";
    }

}
