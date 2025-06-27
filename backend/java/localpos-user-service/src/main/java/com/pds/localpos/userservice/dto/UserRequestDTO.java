package com.pds.localpos.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public record UserRequestDTO(
        @NotBlank(message = "user.username.not_blank")
        String username,

        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "user.email.invalid"
        )
        String email,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "user.password.strong"
        )
        String password,

        @NotEmpty(message = "user.storeCodes.not_empty")
        Set<@NotBlank(message = "storeCode.not_blank") String> storeCodes,

        @NotEmpty(message = "user.roleNames.not_empty")
        Set<@NotBlank(message = "roleName.not_blank") String> roleNames
) {}
