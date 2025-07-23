package com.pds.localpos.userservice.dto.response;

import java.time.Instant;

public record StoreResponse(
        String id,
        String code,
        String name,
        String address,
        Instant createdAt,
        Instant updatedAt
) {
}
