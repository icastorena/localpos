package com.pds.localpos.orderservice.dto;

import java.math.BigDecimal;

public record PayOrderRequest(
        String paymentMethod,
        BigDecimal amountReceived
) {
}