package com.pds.localpos.common.exception

import org.springframework.http.HttpStatus

open class ApiException(
    val status: HttpStatus,
    message: String,
    val messageParams: Array<out Any> = emptyArray()
) : RuntimeException(message)
