package com.pds.localpos.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {

    public BusinessException(HttpStatus status, String message, Object... messageParams) {
        super(status, message, messageParams);
    }
}
