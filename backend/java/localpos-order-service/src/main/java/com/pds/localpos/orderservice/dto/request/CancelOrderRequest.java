package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CancelOrderRequest(
        @NotBlank(message = "{order.cancel.reason.not_blank}")
        String reason
) {
}
