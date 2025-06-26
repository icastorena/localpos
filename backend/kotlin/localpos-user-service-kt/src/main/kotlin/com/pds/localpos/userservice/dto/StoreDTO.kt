package com.pds.localpos.userservice.dto

import java.time.Instant

data class StoreDTO(
    val id: Long? = null,
    val code: String,
    val name: String,
    val address: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
