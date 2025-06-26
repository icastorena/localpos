package com.pds.localpos.authservice.service.impl

import com.pds.localpos.authservice.config.JwtProperties
import com.pds.localpos.authservice.dto.UserResponseDTO
import com.pds.localpos.authservice.service.TokenService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenServiceImpl(
    jwtProperties: JwtProperties
) : TokenService {

    private val secretKey = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())

    override fun generateToken(
        user: UserResponseDTO,
        expirationDate: Date,
        additionalClaims: Map<String, Any>
    ): String =
        Jwts.builder()
            .claims()
            .subject(user.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(secretKey)
            .compact()

    override fun extractUsername(token: String): String? =
        getAllClaims(token)
            .subject

    override fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    override fun isValid(token: String, user: UserResponseDTO): Boolean {
        val username = extractUsername(token)

        return user.username == username && !isExpired(token)
    }

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }
}