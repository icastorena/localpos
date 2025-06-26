package com.pds.localpos.userservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

data class UserRequestDTO(
    @field:NotBlank(message = "user.username.not_blank")
    val username: String,

    @field:Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "user.email.invalid"
    )
    val email: String,

    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "user.password.strong"
    )
    val password: String,

    @field:NotBlank(message = "user.storeCode.not_blank")
    val storeCode: String,

    @field:NotEmpty(message = "user.roleNames.not_empty")
    val roleNames: Set<String>
)
