package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.common.exception.BusinessException;
import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.mapper.UserMapper;
import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.RoleName;
import com.pds.localpos.userservice.model.Store;
import com.pds.localpos.userservice.model.User;
import com.pds.localpos.userservice.repository.RoleRepository;
import com.pds.localpos.userservice.repository.StoreRepository;
import com.pds.localpos.userservice.repository.UserRepository;
import com.pds.localpos.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            StoreRepository storeRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.storeRepository = storeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        requireUsernameNotExists(dto.username());
        requireEmailNotExists(dto.email());
        validatePassword(Optional.ofNullable(dto.password())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "user.password.not_blank")));

        Set<Store> stores = validateAndGetStores(dto.storeCodes());
        Set<Role> roles = fetchAndValidateRoles(dto.roleNames());

        User user = UserMapper.toEntity(dto, stores, roles);
        user.setPassword(passwordEncoder.encode(dto.password()));

        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id));

        requireUsernameOrThrowIfExists(user.getUsername(), dto.username());
        requireEmailOrThrowIfExists(user.getEmail(), dto.email());

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setActive(dto.isActive());

        if (dto.password() != null && !dto.password().isBlank()) {
            validatePassword(dto.password());
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        user.setRoles(new HashSet<>(fetchAndValidateRoles(dto.roleNames())));
        user.setStores(new HashSet<>(validateAndGetStores(dto.storeCodes())));

        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

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

    private void requireUsernameOrThrowIfExists(String current, String updated) {
        if (!current.equals(updated) && userRepository.existsByUsername(updated)) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.username.exists");
        }
    }

    private void requireEmailOrThrowIfExists(String current, String updated) {
        if (!Objects.equals(current, updated) && userRepository.existsByEmail(updated)) {
            throw new BusinessException(HttpStatus.CONFLICT, "user.email.exists");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "user.password.not_blank");
        }

        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        if (!pattern.matcher(password).matches()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "user.password.strong");
        }
    }

    private Set<Role> fetchAndValidateRoles(Set<String> roleNames) {
        Set<RoleName> enumNames = roleNames.stream().map(name -> {
            try {
                return RoleName.valueOf(name);
            } catch (IllegalArgumentException ex) {
                throw new BusinessException(HttpStatus.BAD_REQUEST, "roles.invalid", ex.getMessage());
            }
        }).collect(Collectors.toSet());

        Set<Role> roles = roleRepository.findByNameIn(enumNames);
        Set<RoleName> foundNames = roles.stream().map(Role::getName).collect(Collectors.toSet());

        Set<RoleName> missing = new HashSet<>(enumNames);
        missing.removeAll(foundNames);

        if (!missing.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "roles.missing",
                    missing.stream().map(Enum::name).collect(Collectors.joining(", ")));
        }

        return roles;
    }

    private Set<Store> validateAndGetStores(Set<String> codes) {
        if (codes.isEmpty()) {
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
