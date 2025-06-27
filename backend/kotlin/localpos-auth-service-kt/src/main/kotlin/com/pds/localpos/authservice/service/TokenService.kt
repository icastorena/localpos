package com.pds.localpos.authservice.service

import com.pds.localpos.authservice.dto.UserResponseDTO
import java.util.*

interface TokenService {

    fun generateToken(
        user: UserResponseDTO,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String

    fun extractUsername(token: String): String?

    fun isExpired(token: String): Boolean

    fun isValid(token: String, user: UserResponseDTO): Boolean
}