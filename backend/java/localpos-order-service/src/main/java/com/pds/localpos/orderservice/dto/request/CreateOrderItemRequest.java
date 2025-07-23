package com.pds.localpos.orderservice.dto.request;

public record CreateOrderItemRequest(
        String productId,
        Integer quantity
) {
}