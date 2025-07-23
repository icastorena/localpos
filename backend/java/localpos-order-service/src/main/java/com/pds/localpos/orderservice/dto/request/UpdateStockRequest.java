package com.pds.localpos.orderservice.dto.request;

public record UpdateStockRequest(
        String productId,
        String storeId,
        Integer quantity
) {
}
