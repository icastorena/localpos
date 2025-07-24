package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.request.LoginRequest;
import com.pds.localpos.userservice.dto.response.UserResponse;
import com.pds.localpos.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<UserResponse> validateUser(@Valid @RequestBody LoginRequest request) {
        log.info("POST /auth/validate - Authenticating user: {}", request.username());
        UserResponse user = authService.validateCredentials(request);
        log.info("Authentication successful for user: {}", request.username());
        return ResponseEntity.ok(user);
    }
}
