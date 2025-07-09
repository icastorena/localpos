package com.pds.localpos.orderservice.dto;

public record CreateOrderItemRequest(
        String productId,
        Integer quantity
) {
}