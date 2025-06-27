package com.pds.localpos.userservice.dto

import java.time.Instant

data class UserResponseDTO(
    val id: String,
    val username: String,
    val email: String?,
    val stores: Set<StoreDTO> = emptySet(),
    val roles: Set<RoleDTO> = emptySet(),
    val createdAt: Instant,
    val updatedAt: Instant
)
