package com.pds.localpos.common.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final String errorCode;
    private final String messageCode;

    public ApiException(String errorCode, String messageCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.messageCode = messageCode;
    }
}
