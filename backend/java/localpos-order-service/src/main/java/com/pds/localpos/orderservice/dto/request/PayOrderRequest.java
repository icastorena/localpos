package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PayOrderRequest(
        @NotBlank(message = "{order.payment_method.not_blank}")
        String paymentMethod,

        @NotNull(message = "{order.amount_received.not_null}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{order.amount_received.min}")
        BigDecimal amountReceived
) {
}
