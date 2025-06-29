package com.pds.localpos.userservice.dto;

import java.time.Instant;
import java.util.Set;

public record UserResponseDTO(
        String id,
        String username,
        String email,
        Set<StoreDTO> stores,
        Set<RoleDTO> roles,
        Instant createdAt,
        Instant updatedAt
) {
}
