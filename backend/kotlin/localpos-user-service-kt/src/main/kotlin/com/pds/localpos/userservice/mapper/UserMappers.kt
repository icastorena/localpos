package com.pds.localpos.userservice.mapper

import com.pds.localpos.userservice.dto.RoleDTO
import com.pds.localpos.userservice.dto.StoreDTO
import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.model.Role
import com.pds.localpos.userservice.model.Store
import com.pds.localpos.userservice.model.User

fun Role.toDTO() = RoleDTO(
    id = this.id,
    name = this.name,
    description = this.description
)

fun Store.toDTO() = StoreDTO(
    id = this.id,
    code = this.code,
    name = this.name,
    address = this.address,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun UserRequestDTO.toEntity(): User = User(
    username = this.username,
    password = this.password,
    email = this.email
)

fun User.toResponseDTO(): UserResponseDTO = UserResponseDTO(
    id = this.id ?: 0,
    username = this.username,
    email = this.email,
    store = this.store?.toDTO(),
    roles = this.roles.map { it.toDTO() }.toSet(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
