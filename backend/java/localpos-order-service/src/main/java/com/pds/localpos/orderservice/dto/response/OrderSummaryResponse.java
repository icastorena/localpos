package com.pds.localpos.orderservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponse(
        String id,
        String storeId,
        String userId,
        String status,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}