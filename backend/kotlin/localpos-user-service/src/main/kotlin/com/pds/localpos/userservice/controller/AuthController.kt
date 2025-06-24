package com.pds.localpos.userservice.controller

import com.pds.localpos.userservice.dto.AuthRequestDTO
import com.pds.localpos.userservice.dto.AuthResponseDTO
import com.pds.localpos.userservice.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequestDTO): ResponseEntity<AuthResponseDTO> {
        val token = authenticationService.authenticate(request.username, request.password)
        return ResponseEntity.ok(AuthResponseDTO(token))
    }
}
