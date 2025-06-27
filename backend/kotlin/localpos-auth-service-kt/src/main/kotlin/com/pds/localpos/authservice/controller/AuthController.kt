package com.pds.localpos.authservice.controller

import com.pds.localpos.authservice.config.JwtProperties
import com.pds.localpos.authservice.dto.AuthRequestDTO
import com.pds.localpos.authservice.dto.AuthResponseDTO
import com.pds.localpos.authservice.service.TokenService
import com.pds.localpos.authservice.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties
) {

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequestDTO): ResponseEntity<AuthResponseDTO> {
        val user = userService.getUser(request.username, request.password)

        val expiration = Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)

        val claims = mapOf(
            "roles" to user.roles.map { it.name },
            "userId" to user.id
        )

        val token = tokenService.generateToken(user, expiration, claims)

        return ResponseEntity.ok(AuthResponseDTO(token))
    }

}
