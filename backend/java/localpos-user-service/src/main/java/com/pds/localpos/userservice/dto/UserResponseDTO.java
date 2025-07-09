package com.pds.localpos.userservice.dto;

import java.time.Instant;
import java.util.Set;

public record UserResponseDTO(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        String phone,
        String address,
        boolean isActive,
        Set<StoreDTO> stores,
        Set<RoleDTO> roles,
        Instant createdAt,
        Instant updatedAt
) {
}
