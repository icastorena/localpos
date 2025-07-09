package com.pds.localpos.orderservice.dto;

import java.util.List;

public record CreateOrderRequest(
        String storeId,
        String userId,
        List<CreateOrderItemRequest> items,
        String orderType,
        String paymentMethod
) {
}