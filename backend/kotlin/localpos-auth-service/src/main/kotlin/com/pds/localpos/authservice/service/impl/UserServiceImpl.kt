package com.pds.localpos.authservice.service.impl

import com.pds.localpos.authservice.dto.UserResponseDTO
import com.pds.localpos.authservice.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UserServiceImpl(
    private val restTemplate: RestTemplate,
    @Value("\${user-service.url}") private val userServiceAuthUrl: String
) : UserService {

    override fun getUser(username: String, password: String): UserResponseDTO {
        val request = mapOf(
            "username" to username,
            "password" to password
        )

        return try {
            restTemplate.postForObject(userServiceAuthUrl, request, UserResponseDTO::class.java)
                ?: throw UsernameNotFoundException("Invalid credentials")
        } catch (ex: Exception) {
            throw UsernameNotFoundException("Invalid credentials")
        }
    }
}