package com.pds.localpos.userservice.mapper

import com.pds.localpos.userservice.dto.RoleDTO
import com.pds.localpos.userservice.dto.StoreDTO
import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.model.Role
import com.pds.localpos.userservice.model.Store
import com.pds.localpos.userservice.model.User
import java.time.Instant

fun Role.toDTO() = RoleDTO(
    id = id,
    name = name.name,
    description = description
)

fun Store.toDTO() = StoreDTO(
    id = id,
    code = code,
    name = name,
    address = address,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun UserRequestDTO.toEntity(
    stores: Set<Store>,
    roles: Set<Role>
): User = User(
    username = username,
    password = "",
    email = email,
    stores = stores.toMutableSet(),
    roles = roles.toMutableSet(),
    createdAt = Instant.now(),
    updatedAt = Instant.now()
)

fun User.toResponseDTO(): UserResponseDTO = UserResponseDTO(
    id = id,
    username = username,
    email = email.orEmpty(),
    stores = stores.map { it.toDTO() }.toSet(),
    roles = roles.map { it.toDTO() }.toSet(),
    createdAt = createdAt,
    updatedAt = updatedAt
)
