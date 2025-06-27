package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.exception.BusinessException;
import com.pds.localpos.userservice.exception.ResourceNotFoundException;
import com.pds.localpos.userservice.mapper.UserMapper;
import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.Store;
import com.pds.localpos.userservice.model.User;
import com.pds.localpos.userservice.repository.RoleRepository;
import com.pds.localpos.userservice.repository.StoreRepository;
import com.pds.localpos.userservice.repository.UserRepository;
import com.pds.localpos.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.username.exists");
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.email.exists");
        }

        Set<Store> stores = new HashSet<>();
        if (dto.storeCodes() != null && !dto.storeCodes().isEmpty()) {
            stores = storeRepository.findByCodeIn(dto.storeCodes());
            if (stores.size() != dto.storeCodes().size()) {
                Set<String> foundCodes = stores.stream().map(Store::getCode).collect(Collectors.toSet());
                Set<String> missingCodes = new HashSet<>(dto.storeCodes());
                missingCodes.removeAll(foundCodes);
                throw new BusinessException(
                        HttpStatus.BAD_REQUEST,
                        "stores.missing",
                        String.join(", ", missingCodes)
                );
            }
        }

        Set<Role> roles = fetchRolesByNames(dto.roleNames());
        if (roles.isEmpty()) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "roles.invalid"
            );
        }

        User user = UserMapper.toEntity(dto, stores, roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);

        return UserMapper.toDTO(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                HttpStatus.NOT_FOUND,
                "user.not_found",
                id
        ));
        return UserMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                HttpStatus.NOT_FOUND,
                "user.not_found",
                id
        ));

        if (!user.getUsername().equals(dto.username()) && userRepository.existsByUsername(dto.username())) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.username.exists");
        }

        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.email.exists");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        Set<Role> roles = fetchRolesByNames(dto.roleNames());
        user.setRoles(roles);
        return UserMapper.toDTO(userRepository.save(user));
    }


    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    HttpStatus.NOT_FOUND,
                    "user.not_found",
                    id
            );
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Set<Role> fetchRolesByNames(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Role> roles = roleRepository.findByNameIn(roleNames);

        if (roles.size() != roleNames.size()) {
            Set<String> foundNames = roles.stream().map(Role::getName).collect(Collectors.toSet());
            Set<String> missingNames = new HashSet<>(roleNames);
            missingNames.removeAll(foundNames);
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "roles.missing",
                    String.join(", ", missingNames)
            );
        }

        return roles;
    }
}
