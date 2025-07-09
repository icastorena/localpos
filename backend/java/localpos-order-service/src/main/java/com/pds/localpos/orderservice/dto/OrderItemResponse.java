package com.pds.localpos.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String id,
        String orderId,
        String productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal total
) {
}
