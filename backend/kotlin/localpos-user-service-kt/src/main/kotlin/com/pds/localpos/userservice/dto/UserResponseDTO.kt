package com.pds.localpos.userservice.dto

import java.time.Instant

data class UserResponseDTO(
    val id: String,
    val username: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val address: String?,
    val isActive: Boolean,
    val stores: Set<StoreDTO> = emptySet(),
    val roles: Set<RoleDTO> = emptySet(),
    val createdAt: Instant,
    val updatedAt: Instant
)
