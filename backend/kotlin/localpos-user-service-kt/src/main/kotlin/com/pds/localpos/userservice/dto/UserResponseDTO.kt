package com.pds.localpos.userservice.dto

import java.time.Instant

data class UserResponseDTO(
    val id: Long,
    val username: String,
    val email: String?,
    val store: StoreDTO?,
    val roles: Set<RoleDTO>,
    val createdAt: Instant?,
    val updatedAt: Instant?
)
