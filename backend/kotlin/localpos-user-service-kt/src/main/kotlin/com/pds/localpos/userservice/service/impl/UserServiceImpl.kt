package com.pds.localpos.userservice.service.impl

import com.pds.localpos.common.exception.BusinessException
import com.pds.localpos.common.exception.ResourceNotFoundException
import com.pds.localpos.userservice.dto.UserRequestDTO
import com.pds.localpos.userservice.dto.UserResponseDTO
import com.pds.localpos.userservice.mapper.toEntity
import com.pds.localpos.userservice.mapper.toResponseDTO
import com.pds.localpos.userservice.model.Role
import com.pds.localpos.userservice.model.RoleName
import com.pds.localpos.userservice.model.Store
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
        requireUsernameNotExists(dto.username)
        requireEmailNotExists(dto.email)
        validatePassword(dto.password ?: throw BusinessException(HttpStatus.BAD_REQUEST, "user.password.not_blank"))

        val stores = validateAndGetStores(dto.storeCodes)
        val roles = fetchAndValidateRoles(dto.roleNames)

        val user = dto.toEntity(stores, roles).apply {
            password = passwordEncoder.encode(dto.password)
        }

        return userRepository.save(user).toResponseDTO()
    }

    override fun getUserById(id: String): UserResponseDTO =
        userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id) }
            .toResponseDTO()

    override fun getAllUsers(): List<UserResponseDTO> =
        userRepository.findAll()
            .filter { user ->
                user.roles.none { RoleName.OWNER == it.name || RoleName.ADMIN == it.name }
            }
            .map { it.toResponseDTO() }

    override fun updateUser(id: String, dto: UserRequestDTO): UserResponseDTO {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id) }

        requireUsernameOrThrowIfExists(user.username, dto.username)
        requireEmailOrThrowIfExists(user.email, dto.email)

        user.apply {
            username = dto.username
            email = dto.email
            if (!dto.password.isNullOrBlank()) {
                validatePassword(dto.password)
                password = passwordEncoder.encode(dto.password)
            }
            roles = fetchAndValidateRoles(dto.roleNames).toMutableSet()
            stores = validateAndGetStores(dto.storeCodes).toMutableSet()
        }

        return userRepository.save(user).toResponseDTO()
    }

    override fun deleteUser(id: String) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id)
        }
        userRepository.deleteById(id)
    }

    override fun findByUsername(username: String): User? =
        userRepository.findByUsername(username)

    private fun requireUsernameNotExists(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.username.exists")
        }
    }

    private fun validatePassword(password: String?) {
        if (password.isNullOrBlank()) {
            throw BusinessException(HttpStatus.BAD_REQUEST, "user.password.not_blank", "Password is required")
        }
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        if (!passwordPattern.matches(password)) {
            throw BusinessException(HttpStatus.BAD_REQUEST, "user.password.strong", "Password is not strong enough")
        }
    }

    private fun requireEmailNotExists(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.email.exists")
        }
    }

    private fun requireUsernameOrThrowIfExists(current: String, new: String) {
        if (current != new && userRepository.existsByUsername(new)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.username.exists")
        }
    }

    private fun requireEmailOrThrowIfExists(current: String?, new: String?) {
        if (current != new && new != null && userRepository.existsByEmail(new)) {
            throw BusinessException(HttpStatus.CONFLICT, "user.email.exists")
        }
    }

    private fun fetchAndValidateRoles(roleNames: Set<String>): Set<Role> {
        val enumNames = try {
            roleNames.map { RoleName.valueOf(it) }.toSet()
        } catch (ex: IllegalArgumentException) {
            throw BusinessException(
                HttpStatus.BAD_REQUEST,
                "roles.invalid",
                ex.message ?: "The specified roles are invalid."
            )
        }

        val roles = roleRepository.findByNameIn(enumNames)
        val foundNames = roles.map { it.name }.toSet()
        val missing = enumNames - foundNames

        if (missing.isNotEmpty()) {
            throw BusinessException(HttpStatus.BAD_REQUEST, "roles.missing", missing.joinToString(", "))
        }

        return roles
    }


    private fun validateAndGetStores(codes: Set<String>): Set<Store> {
        if (codes.isEmpty()) return emptySet()

        val stores = storeRepository.findByCodeIn(codes)
        val foundCodes = stores.map { it.code }.toSet()
        val missing = codes - foundCodes

        if (missing.isNotEmpty()) {
            throw BusinessException(HttpStatus.BAD_REQUEST, "stores.missing", missing.joinToString(", "))
        }

        return stores.toSet()
    }
}
