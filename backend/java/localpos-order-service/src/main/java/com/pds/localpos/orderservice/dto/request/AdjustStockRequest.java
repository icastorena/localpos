package com.pds.localpos.orderservice.dto.request;

public record AdjustStockRequest(
        String productId,
        String storeId,
        Integer amount
) {
}
