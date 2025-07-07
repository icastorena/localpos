package com.pds.localpos.common.model

data class InternalApplicationError(
    val correlationId: String?,
    val timestamp: String?,
    val errorCode: String?,
    val message: String?
)
