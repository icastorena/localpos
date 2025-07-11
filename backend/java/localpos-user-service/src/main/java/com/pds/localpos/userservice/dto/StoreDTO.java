package com.pds.localpos.userservice.dto;

import java.time.Instant;

public record StoreDTO(
        String id,
        String code,
        String name,
        String address,
        Instant createdAt,
        Instant updatedAt
) {
}
