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

        User user = UserMapper.toEntity(dto);

        Store store = storeRepository.findByCode(dto.storeCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        HttpStatus.NOT_FOUND,
                        "store.not_found"
                ));

        Set<Role> roles = roleRepository.findByNameIn(dto.roleNames());
        if (roles == null || roles.isEmpty()) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "roles.invalid"
            );
        }

        user.setStore(store);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);

        return UserMapper.toDTO(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
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
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
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
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    HttpStatus.NOT_FOUND,
                    "user.not_found",
                    id
            );
        }
        userRepository.deleteById(id);
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
