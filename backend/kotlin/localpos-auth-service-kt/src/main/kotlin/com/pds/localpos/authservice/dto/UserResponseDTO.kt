package com.pds.localpos.authservice.dto

import java.time.Instant

data class UserResponseDTO(
    val id: String,
    val username: String,
    val email: String,
    val store: StoreDTO?,
    val roles: List<RoleDTO>,
    val createdAt: Instant,
    val updatedAt: Instant
)
