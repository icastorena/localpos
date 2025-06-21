package com.pds.localpos.common.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InternalApplicationError {

    private String correlationId;
    private String timestamp;
    private String errorCode;
    private String message;
    private String details;
}
