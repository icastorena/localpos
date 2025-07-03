package com.pds.localpos.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserRequestDTO(
        @NotBlank(message = "user.username.not_blank")
        String username,

        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "user.email.invalid"
        )
        String email,

        String password,

        @NotEmpty(message = "user.storeCodes.not_empty")
        Set<@NotBlank(message = "storeCode.not_blank") String> storeCodes,

        @NotEmpty(message = "user.roleNames.not_empty")
        Set<@NotBlank(message = "roleName.not_blank") String> roleNames,

        @NotBlank(message = "user.firstName.not_blank")
        @Size(max = 100, message = "user.firstName.size")
        String firstName,

        @NotBlank(message = "user.lastName.not_blank")
        @Size(max = 100, message = "user.lastName.size")
        String lastName,

        @Size(max = 20, message = "user.phone.size")
        String phone,

        @Size(max = 255, message = "user.address.size")
        String address,

        @NotNull(message = "user.isActive.not_null")
        Boolean isActive
) {
}
