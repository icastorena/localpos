package com.pds.localpos.userservice.controller

import com.pds.localpos.userservice.dto.LoginRequestDTO
import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.mapper.toResponseDTO
import com.pds.localpos.userservice.service.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponseDTO>> {
        logger.info("Fetching all users")
        return ResponseEntity.ok(userService.getAllUsers())
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<UserResponseDTO> {
        logger.info("Fetching user with ID: {}", id)
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @PostMapping
    fun createUser(@Valid @RequestBody dto: UserRequestDTO): ResponseEntity<UserResponseDTO> {
        logger.info("Creating new user with username: {}", dto.username)
        return ResponseEntity.ok(userService.createUser(dto))
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: String,
        @Valid @RequestBody dto: UserRequestDTO
    ): ResponseEntity<UserResponseDTO> {
        logger.info("Updating user with ID: {}", id)
        return ResponseEntity.ok(userService.updateUser(id, dto))
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        logger.warn("Deleting user with ID: {}", id)
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/auth/validate")
    fun validateUser(@RequestBody request: LoginRequestDTO): ResponseEntity<UserResponseDTO> {
        logger.info("Authenticating user: {}", request.username)

        val user = userService.findByUsername(request.username)
        if (user == null || !passwordEncoder.matches(request.password, user.password)) {
            logger.warn("Authentication failed for user: {}", request.username)
            throw BadCredentialsException("Invalid credentials")
        }

        logger.info("Authentication successful for user: {}", request.username)
        return ResponseEntity.ok(user.toResponseDTO())
    }
}
