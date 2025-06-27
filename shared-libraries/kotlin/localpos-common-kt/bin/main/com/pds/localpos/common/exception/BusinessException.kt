package com.pds.localpos.common.exception

import org.springframework.http.HttpStatus

class BusinessException(
    status: HttpStatus,
    message: String,
    vararg messageParams: Any
) : ApiException(status, message, messageParams)
