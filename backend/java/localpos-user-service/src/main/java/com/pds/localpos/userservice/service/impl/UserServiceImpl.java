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
        requireUsernameNotExists(dto.username());
        requireEmailNotExists(dto.email());

        Set<Store> stores = validateAndGetStores(dto.storeCodes());
        Set<Role> roles = fetchRolesByNames(dto.roleNames());

        if (roles.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "roles.invalid");
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
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                HttpStatus.NOT_FOUND,
                "user.not_found",
                id
        ));

        requireUsernameOrThrowIfExists(user.getUsername(), dto.username());
        requireEmailOrThrowIfExists(user.getEmail(), dto.email());

        user.setUsername(dto.username());
        user.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        Set<Role> roles = fetchRolesByNames(dto.roleNames());
        user.setRoles(roles);

        Set<Store> stores = validateAndGetStores(dto.storeCodes());
        user.setStores(stores);

        User updated = userRepository.save(user);
        return UserMapper.toDTO(updated);
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
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // --- Private helper validation methods ---

    private void requireUsernameNotExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.username.exists");
        }
    }

    private void requireEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.email.exists");
        }
    }

    private void requireUsernameOrThrowIfExists(String current, String newUsername) {
        if (!current.equals(newUsername) && userRepository.existsByUsername(newUsername)) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.username.exists");
        }
    }

    private void requireEmailOrThrowIfExists(String current, String newEmail) {
        if (newEmail != null && !newEmail.equals(current) && userRepository.existsByEmail(newEmail)) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.email.exists");
        }
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

    private Set<Store> validateAndGetStores(Set<String> codes) {
        if (codes == null || codes.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Store> stores = storeRepository.findByCodeIn(codes);
        Set<String> foundCodes = stores.stream().map(Store::getCode).collect(Collectors.toSet());

        Set<String> missing = new HashSet<>(codes);
        missing.removeAll(foundCodes);

        if (!missing.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "stores.missing", String.join(", ", missing));
        }

        return stores;
    }
}
