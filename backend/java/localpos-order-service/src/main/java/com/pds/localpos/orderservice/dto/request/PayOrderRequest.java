package com.pds.localpos.orderservice.dto.request;

import java.math.BigDecimal;

public record PayOrderRequest(
        String paymentMethod,
        BigDecimal amountReceived
) {
}