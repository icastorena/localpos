package com.pds.localpos.productservice.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponseDTO(
        String id,
        String name,
        String description,
        BigDecimal price,
        CategoryDTO category,
        Instant createdAt,
        Instant updatedAt
) {
}
