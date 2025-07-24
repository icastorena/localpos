package com.pds.localpos.userservice.dto.response;

import java.time.Instant;
import java.util.Set;

public record UserResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        String address,
        boolean isActive,
        Set<StoreResponse> stores,
        Set<RoleResponse> roles,
        Instant createdAt,
        Instant updatedAt
) {
}
