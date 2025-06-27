package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private UserController userController;

    private final String userId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userController = new UserController(userService, passwordEncoder);
    }

    @Test
    void getAllUsers_returnsList() {
        var user1 = new UserResponseDTO(
                UUID.randomUUID().toString(),
                "user1",
                "user1@mail.com",
                null,
                Set.of(),
                Instant.now(),
                Instant.now()
        );
        var user2 = new UserResponseDTO(
                UUID.randomUUID().toString(),
                "user2",
                "user2@mail.com",
                null,
                Set.of(),
                Instant.now(),
                Instant.now()
        );
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(userService).getAllUsers();
    }

    @Test
    void getUser_returnsUser() {
        var user = new UserResponseDTO(
                userId,
                "user1",
                "user1@mail.com",
                null,
                Set.of(),
                Instant.now(),
                Instant.now()
        );
        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<UserResponseDTO> response = userController.getUser(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("user1", response.getBody().username());
        verify(userService).getUserById(userId);
    }

    @Test
    void createUser_callsServiceWithDto() {
        UserRequestDTO dto = new UserRequestDTO(
                "newuser",
                "newuser@mail.com",
                "StrongP@ss1",
                Set.of("MAIN"),
                Set.of("ADMIN")
        );
        UserResponseDTO responseDTO = new UserResponseDTO(
                userId,
                "newuser",
                "newuser@mail.com",
                null,
                Set.of(),
                Instant.now(),
                Instant.now()
        );

        when(userService.createUser(dto)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.createUser(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("newuser", response.getBody().username());
        verify(userService).createUser(dto);
    }

    @Test
    void updateUser_callsServiceWithDtoAndId() {
        UserRequestDTO dto = new UserRequestDTO(
                "upduser",
                "upduser@mail.com",
                "StrongP@ss1",
                Set.of("MAIN"),
                Set.of("ADMIN")
        );
        UserResponseDTO responseDTO = new UserResponseDTO(
                userId,
                "upduser",
                "upduser@mail.com",
                null,
                Set.of(),
                Instant.now(),
                Instant.now()
        );

        when(userService.updateUser(userId, dto)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(userId, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("upduser", response.getBody().username());
        verify(userService).updateUser(userId, dto);
    }

    @Test
    void deleteUser_callsServiceAndReturnsNoContent() {
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(204, response.getStatusCode().value());
        verify(userService).deleteUser(userId);
    }
}
