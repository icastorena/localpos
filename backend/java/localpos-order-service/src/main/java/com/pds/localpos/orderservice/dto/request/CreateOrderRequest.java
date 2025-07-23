package com.pds.localpos.orderservice.dto.request;

import java.util.List;

public record CreateOrderRequest(
        String storeId,
        String userId,
        List<CreateOrderItemRequest> items,
        String orderType,
        String paymentMethod
) {
}