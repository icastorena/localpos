package com.pds.localpos.userservice.mapper;

import com.pds.localpos.userservice.dto.RoleDTO;
import com.pds.localpos.userservice.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setId(dto.id());
        role.setName(dto.name());
        role.setDescription(dto.description());
        return role;
    }
}
