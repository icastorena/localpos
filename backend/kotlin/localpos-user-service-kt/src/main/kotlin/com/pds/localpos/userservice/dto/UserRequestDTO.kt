package com.pds.localpos.userservice.dto

import jakarta.validation.constraints.*

data class UserRequestDTO(

    @field:NotBlank(message = "user.username.not_blank")
    val username: String,

    @field:Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        message = "user.email.invalid"
    )
    val email: String,

    val password: String?,

    @field:NotEmpty(message = "user.storeCodes.not_empty")
    val storeCodes: Set<@NotBlank(message = "storeCode.not_blank") String>,

    @field:NotEmpty(message = "user.roleNames.not_empty")
    val roleNames: Set<@NotBlank(message = "roleName.not_blank") String>,

    @field:NotBlank(message = "user.firstName.not_blank")
    @field:Size(max = 100, message = "user.firstName.size")
    val firstName: String,

    @field:NotBlank(message = "user.lastName.not_blank")
    @field:Size(max = 100, message = "user.lastName.size")
    val lastName: String,

    @field:Size(max = 20, message = "user.phone.size")
    val phone: String? = null,

    @field:Size(max = 255, message = "user.address.size")
    val address: String? = null,

    @field:NotNull(message = "user.isActive.not_null")
    val isActive: Boolean = true
)
