package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.LoginRequestDTO;
import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.mapper.UserMapper;
import com.pds.localpos.userservice.model.User;
import com.pds.localpos.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String id) {
        log.info("Fetching user with ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        log.info("Creating new user with username: {}", dto.username());
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN','MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @Valid @RequestBody UserRequestDTO dto) {
        log.info("Updating user with ID: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.warn("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/validate")
    public ResponseEntity<UserResponseDTO> validateUser(@RequestBody LoginRequestDTO request) {
        log.info("Authenticating user: {}", request.username());

        User user = userService.findByUsername(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed for user: {}", request.username());
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("Authentication successful for user: {}", request.username());
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }
}