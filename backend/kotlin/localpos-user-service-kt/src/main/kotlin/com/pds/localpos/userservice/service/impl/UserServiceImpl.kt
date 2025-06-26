package com.pds.localpos.userservice.service.impl

import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.exception.BusinessException
import com.pds.localpos.userservice.exception.ResourceNotFoundException
import com.pds.localpos.userservice.mapper.toEntity
import com.pds.localpos.userservice.mapper.toResponseDTO
import com.pds.localpos.userservice.model.Role
import com.pds.localpos.userservice.model.User
import com.pds.localpos.userservice.repository.RoleRepository
import com.pds.localpos.userservice.repository.StoreRepository
import com.pds.localpos.userservice.repository.UserRepository
import com.pds.localpos.userservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val storeRepository: StoreRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createUser(dto: UserRequestDTO): UserResponseDTO {
        validateUsernameAndEmail(dto.username, dto.email)

        val store = storeRepository.findByCode(dto.storeCode)
            ?: throw ResourceNotFoundException(HttpStatus.NOT_FOUND, "store.not_found")

        val roles = fetchRolesByNames(dto.roleNames)

        val user = dto.toEntity().apply {
            this.store = store
            this.roles = roles.toMutableSet()
            this.password = passwordEncoder.encode(dto.password)
        }

        return userRepository.save(user).toResponseDTO()
    }

    @Transactional(readOnly = true)
    override fun getUserById(id: Long): UserResponseDTO =
        userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id) }
            .toResponseDTO()

    @Transactional(readOnly = true)
    override fun getAllUsers(): List<UserResponseDTO> =
        userRepository.findAll().map { it.toResponseDTO() }

    override fun updateUser(id: Long, dto: UserRequestDTO): UserResponseDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id) }

        if (user.username != dto.username && userRepository.existsByUsername(dto.username)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.username.exists")
        }

        if (user.email != dto.email && userRepository.existsByEmail(dto.email)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.email.exists")
        }

        user.apply {
            username = dto.username
            email = dto.email
            if (dto.password.isNotBlank()) {
                password = passwordEncoder.encode(dto.password)
            }
            roles = fetchRolesByNames(dto.roleNames).toMutableSet()
        }

        return userRepository.save(user).toResponseDTO()
    }

    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id)
        }
        userRepository.deleteById(id)
    }

    override fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)

    private fun fetchRolesByNames(roleNames: Set<String>): Set<Role> {
        val roles = roleRepository.findByNameIn(roleNames)

        if (roles.size != roleNames.size) {
            val foundNames = roles.map { it.name }.toSet()
            val missingNames = roleNames - foundNames
            throw BusinessException(HttpStatus.BAD_REQUEST, "roles.missing", missingNames.joinToString(", "))
        }

        return roles
    }

    private fun validateUsernameAndEmail(username: String, email: String) {
        if (userRepository.existsByUsername(username)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.username.exists")
        }
        if (userRepository.existsByEmail(email)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.email.exists")
        }
    }
}
