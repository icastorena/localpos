package com.pds.localpos.common.exception

import org.springframework.http.HttpStatus

class ResourceNotFoundException(
    status: HttpStatus,
    message: String,
    vararg messageParams: Any
) : ApiException(status, message, messageParams)
