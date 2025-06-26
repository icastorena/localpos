package com.pds.localpos.userservice.service

import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.model.User

interface UserService {

    fun createUser(dto: UserRequestDTO): UserResponseDTO

    fun getUserById(id: Long): UserResponseDTO

    fun getAllUsers(): List<UserResponseDTO>

    fun updateUser(id: Long, dto: UserRequestDTO): UserResponseDTO

    fun deleteUser(id: Long)

    fun findByUsername(username: String): User?
}
