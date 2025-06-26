package com.pds.localpos.authservice.service

import com.pds.localpos.authservice.dto.UserResponseDTO

interface UserService {

    fun getUser(username: String, password: String): UserResponseDTO
}