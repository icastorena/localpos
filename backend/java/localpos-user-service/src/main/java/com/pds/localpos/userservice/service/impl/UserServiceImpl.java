package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.common.exception.BusinessException;
import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.userservice.dto.request.UserRequest;
import com.pds.localpos.userservice.dto.response.UserResponse;
import com.pds.localpos.userservice.mapper.UserMapper;
import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.RoleName;
import com.pds.localpos.userservice.model.Store;
import com.pds.localpos.userservice.model.User;
import com.pds.localpos.userservice.repository.RoleRepository;
import com.pds.localpos.userservice.repository.StoreRepository;
import com.pds.localpos.userservice.repository.UserRepository;
import com.pds.localpos.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Pattern STRONG_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest dto) {
        log.info("Creating new user: {}", dto.username());

        requireFieldNotExists(dto.username(), userRepository::existsByUsername, "user.username.exists");
        requireFieldNotExists(dto.email(), userRepository::existsByEmail, "user.email.exists");

        String password = Optional.ofNullable(dto.password())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "user.password.not_blank"));
        validatePassword(password);

        Set<Store> stores = validateAndGetStores(dto.storeCodes());
        Set<Role> roles = fetchAndValidateRoles(dto.roleNames());

        User user = UserMapper.toEntity(dto, stores, roles);
        user.setPassword(passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);

        log.info("User created successfully: {} (id: {})", savedUser.getUsername(), savedUser.getId());
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public UserResponse getUserById(String id) {
        log.info("Retrieving user by ID: {}", id);
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", id);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id);
                });
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Fetched {} users", users.size());
        return users;
    }

    @Override
    public UserResponse updateUser(String id, UserRequest dto) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", id);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id);
                });

        requireFieldUnique(user.getUsername(), dto.username(), userRepository::existsByUsername, "user.username.exists");
        requireFieldUnique(user.getEmail(), dto.email(), userRepository::existsByEmail, "user.email.exists");

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setActive(dto.isActive());

        if (dto.password() != null && !dto.password().isBlank()) {
            log.debug("Password update requested for user ID: {}", id);
            validatePassword(dto.password());
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        user.setRoles(new HashSet<>(fetchAndValidateRoles(dto.roleNames())));
        user.setStores(new HashSet<>(validateAndGetStores(dto.storeCodes())));

        User updated = userRepository.save(user);
        log.info("User updated successfully: {} (id: {})", updated.getUsername(), updated.getId());

        return UserMapper.toDTO(updated);
    }

    @Override
    public void deleteUser(String id) {
        log.info("Attempting to delete user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("User not found for deletion: {}", id);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, "user.not_found", id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    @Override
    public User findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    private void requireFieldNotExists(String value, Predicate<String> existsFn, String messageKey) {
        if (existsFn.test(value)) {
            log.warn("Field already exists: {}", value);
            throw new BusinessException(HttpStatus.CONFLICT, messageKey);
        }
    }

    private void requireFieldUnique(String current, String updated, Predicate<String> existsFn, String messageKey) {
        if (!Objects.equals(current, updated) && existsFn.test(updated)) {
            log.warn("Field already exists: {}", updated);
            throw new BusinessException(HttpStatus.CONFLICT, messageKey);
        }
    }

    private void validatePassword(String password) {
        log.debug("Validating password strength");

        if (password == null || password.isBlank()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "user.password.not_blank");
        }

        if (!STRONG_PASSWORD_PATTERN.matcher(password).matches()) {
            log.warn("Weak password rejected");
            throw new BusinessException(HttpStatus.BAD_REQUEST, "user.password.strong");
        }
    }

    private Set<Role> fetchAndValidateRoles(Set<String> roleNames) {
        log.debug("Validating roles: {}", roleNames);

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
            log.warn("Missing roles: {}", missing);
            throw new BusinessException(HttpStatus.BAD_REQUEST, "roles.missing",
                    missing.stream().map(Enum::name).collect(Collectors.joining(", ")));
        }

        return roles;
    }

    private Set<Store> validateAndGetStores(Set<String> codes) {
        log.debug("Validating store codes: {}", codes);

        if (codes == null || codes.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "user.storeCodes.not_empty");
        }

        Set<Store> stores = storeRepository.findByCodeIn(codes);
        Set<String> foundCodes = stores.stream().map(Store::getCode).collect(Collectors.toSet());

        Set<String> missing = new HashSet<>(codes);
        missing.removeAll(foundCodes);

        if (!missing.isEmpty()) {
            log.warn("Missing stores: {}", missing);
            throw new BusinessException(HttpStatus.BAD_REQUEST, "stores.missing", String.join(", ", missing));
        }

        return stores;
    }
}
