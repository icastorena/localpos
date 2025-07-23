package com.pds.localpos.productservice.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        String id,
        String sku,
        String barcode,
        String name,
        String description,
        BigDecimal price,
        CategoryResponse category,
        Instant createdAt,
        Instant updatedAt
) {
}
