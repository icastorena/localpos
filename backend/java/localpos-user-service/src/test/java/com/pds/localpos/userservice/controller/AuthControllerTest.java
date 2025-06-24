package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.AuthRequestDTO;
import com.pds.localpos.userservice.dto.AuthResponseDTO;
import com.pds.localpos.userservice.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationService);
    }

    @Test
    void login_ReturnsTokenInResponse() {
        String username = "user1";
        String password = "pass123";
        String expectedToken = "jwt.token.example";

        when(authenticationService.authenticate(username, password)).thenReturn(expectedToken);

        AuthRequestDTO requestDTO = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = authController.login(requestDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().token());

        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(authenticationService).authenticate(usernameCaptor.capture(), passwordCaptor.capture());

        assertEquals(username, usernameCaptor.getValue());
        assertEquals(password, passwordCaptor.getValue());
    }
}
