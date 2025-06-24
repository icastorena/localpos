package com.pds.localpos.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final Object[] messageParams;

    public ApiException(HttpStatus status, String message, Object... messageParams) {
        super(message);
        this.status = status;
        this.messageParams = messageParams;
    }
}
