package com.pds.localpos.userservice.controller

import com.pds.localpos.userservice.dto.LoginRequestDTO
import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.mapper.toResponseDTO
import com.pds.localpos.userservice.service.UserService
import jakarta.validation.Valid
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

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponseDTO>> =
        ResponseEntity.ok(userService.getAllUsers())

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserResponseDTO> =
        ResponseEntity.ok(userService.getUserById(id))

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @PostMapping
    fun createUser(@Valid @RequestBody dto: UserRequestDTO): ResponseEntity<UserResponseDTO> =
        ResponseEntity.ok(userService.createUser(dto))

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @Valid @RequestBody dto: UserRequestDTO): ResponseEntity<UserResponseDTO> =
        ResponseEntity.ok(userService.updateUser(id, dto))

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/auth/validate")
    fun validateUser(@RequestBody request: LoginRequestDTO): ResponseEntity<UserResponseDTO> {
        val user = userService.findByUsername(request.username)
            ?: throw BadCredentialsException("Invalid credentials")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("Invalid credentials")
        }

        return ResponseEntity.ok(user.toResponseDTO())
    }
}
