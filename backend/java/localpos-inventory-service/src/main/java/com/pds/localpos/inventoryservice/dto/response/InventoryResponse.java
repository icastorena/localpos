package com.pds.localpos.inventoryservice.dto.response;

public record InventoryResponse(
        String id,
        String productId,
        String storeId,
        Integer quantity,
        String status,
        String updatedAt
) {}
