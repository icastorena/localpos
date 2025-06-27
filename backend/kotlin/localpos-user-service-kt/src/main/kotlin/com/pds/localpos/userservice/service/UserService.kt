package com.pds.localpos.userservice.service

import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.model.User

interface UserService {

    fun createUser(dto: UserRequestDTO): UserResponseDTO

    fun getUserById(id: String): UserResponseDTO

    fun getAllUsers(): List<UserResponseDTO>

    fun updateUser(id: String, dto: UserRequestDTO): UserResponseDTO

    fun deleteUser(id: String)

    fun findByUsername(username: String): User?
}
