package com.pds.localpos.userservice.security.util

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    @Value("\${security.jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${security.jwt.expiration-ms}")
    private var jwtExpirationMs: Long = 0

    private lateinit var key: SecretKey

    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateToken(userDetails: UserDetails): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)

        val roles = userDetails.authorities.map(GrantedAuthority::getAuthority)

        return Jwts.builder()
            .subject(userDetails.username)
            .claim("roles", roles)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            true
        } catch (ex: JwtException) {
            log.warn("Invalid JWT token: ${ex.message}")
            false
        } catch (ex: IllegalArgumentException) {
            log.warn("Invalid JWT token: ${ex.message}")
            false
        }
    }

    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
        return claims.subject
    }
}
