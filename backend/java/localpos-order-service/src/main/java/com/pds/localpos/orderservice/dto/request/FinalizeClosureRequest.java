package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FinalizeClosureRequest(
        @NotBlank(message = "{closure.user_id.not_blank}")
        String updatedBy
) {
}
