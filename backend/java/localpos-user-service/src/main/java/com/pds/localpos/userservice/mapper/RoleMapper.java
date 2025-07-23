package com.pds.localpos.userservice.mapper;

import com.pds.localpos.userservice.dto.response.RoleResponse;
import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.RoleName;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleMapper {

    public static RoleResponse toDTO(Role role) {
        if (role == null) {
            return null;
        }

        return new RoleResponse(
                role.getId(),
                role.getName().name(),
                role.getDescription()
        );
    }

    public static Role toEntity(RoleResponse dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        role.setId(dto.id());
        role.setName(fromString(dto.name()));
        role.setDescription(dto.description());
        return role;
    }

    private static RoleName fromString(String roleName) {
        if (roleName == null) {
            return null;
        }

        for (RoleName role : RoleName.values()) {
            if (role.name().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        return null;
    }
}
