package com.pds.localpos.userservice.exception

import com.pds.localpos.common.exception.ApiException
import org.springframework.http.HttpStatus

class BusinessException(
    status: HttpStatus,
    message: String,
    vararg messageParams: Any
) : ApiException(status, message, messageParams)
