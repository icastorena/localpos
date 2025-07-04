package com.pds.localpos.userservice.mapper;

import com.pds.localpos.userservice.dto.RoleDTO;
import com.pds.localpos.userservice.dto.StoreDTO;
import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.Store;
import com.pds.localpos.userservice.model.User;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static User toEntity(UserRequestDTO dto, Set<Store> stores, Set<Role> roles) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        user.setStores(stores != null ? stores : Collections.emptySet());
        user.setRoles(roles != null ? roles : Collections.emptySet());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setActive(dto.isActive());

        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        Set<StoreDTO> storesDTO = mapStores(user.getStores());
        Set<RoleDTO> rolesDTO = mapRoles(user.getRoles());

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getAddress(),
                user.isActive(),
                storesDTO,
                rolesDTO,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private static Set<StoreDTO> mapStores(Set<Store> stores) {
        if (stores == null || stores.isEmpty()) {
            return Collections.emptySet();
        }

        return stores.stream()
                .map(StoreMapper::toDTO)
                .collect(Collectors.toSet());
    }

    private static Set<RoleDTO> mapRoles(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }

        return roles.stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
