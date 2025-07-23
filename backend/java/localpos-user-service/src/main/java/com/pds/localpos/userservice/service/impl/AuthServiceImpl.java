package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.userservice.dto.request.LoginRequest;
import com.pds.localpos.userservice.dto.response.UserResponse;
import com.pds.localpos.userservice.mapper.UserMapper;
import com.pds.localpos.userservice.model.User;
import com.pds.localpos.userservice.service.AuthService;
import com.pds.localpos.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse validateCredentials(LoginRequest request) {
        log.info("Authenticating user: {}", request.username());

        User user = userService.findByUsername(request.username());
        if (user == null) {
            log.warn("Authentication failed: user not found - {}", request.username());
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Authentication failed: invalid password for user {}", request.username());
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("Authentication successful for user: {}", request.username());
        return UserMapper.toDTO(user);
    }
}
