package com.pds.localpos.authservice.dto

import java.time.Instant

data class StoreDTO(
    val id: String,
    val code: String,
    val name: String,
    val address: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
