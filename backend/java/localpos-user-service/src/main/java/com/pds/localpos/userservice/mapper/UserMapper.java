package com.pds.localpos.userservice.mapper;

import com.pds.localpos.userservice.dto.RoleDTO;
import com.pds.localpos.userservice.dto.StoreDTO;
import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.Store;
import com.pds.localpos.userservice.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static User toEntity(UserRequestDTO dto, Set<Store> stores, Set<Role> roles) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());

        user.setStores(stores != null ? stores : Collections.emptySet());
        user.setRoles(roles != null ? roles : Collections.emptySet());

        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        StoreDTO storeDTO = null;
        if (user.getStores() != null && !user.getStores().isEmpty()) {
            Store s = user.getStores().iterator().next();
            storeDTO = new StoreDTO(
                    s.getId(),
                    s.getCode(),
                    s.getName(),
                    s.getAddress(),
                    s.getCreatedAt(),
                    s.getUpdatedAt()
            );
        }

        Set<RoleDTO> roleDTOs = Collections.emptySet();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            roleDTOs = user.getRoles().stream()
                    .map(r -> new RoleDTO(r.getId(), r.getName(), r.getDescription()))
                    .collect(Collectors.toSet());
        }

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                storeDTO,
                roleDTOs,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
