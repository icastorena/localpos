package com.pds.localpos.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(HttpStatus status, String message, Object... messageParams) {
        super(status, message, messageParams);
    }
}
