package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.AuthRequestDTO;
import com.pds.localpos.userservice.dto.AuthResponseDTO;
import com.pds.localpos.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        String token = authenticationService.authenticate(request.username(), request.password());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
