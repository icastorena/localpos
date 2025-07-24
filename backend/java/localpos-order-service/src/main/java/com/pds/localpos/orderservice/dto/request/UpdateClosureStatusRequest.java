package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateClosureStatusRequest(

        @NotNull(message = "{closure.status.required}")
        String status,

        @NotBlank(message = "{closure.user_id.not_blank}")
        @Size(max = 36, message = "{closure.updatedBy.size}")
        String updatedBy
) {
}
