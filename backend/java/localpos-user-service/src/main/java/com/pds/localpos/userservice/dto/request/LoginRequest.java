package com.pds.localpos.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "user.username.not_blank")
        String username,

        @NotBlank(message = "user.password.not_blank")
        String password

) {
}
