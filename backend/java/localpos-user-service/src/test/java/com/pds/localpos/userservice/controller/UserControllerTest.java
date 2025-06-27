package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void getAllUsers_returnsList() {
        var user1 = new UserResponseDTO(1L, "user1", "user1@mail.com", null, Set.of(), Instant.now(), Instant.now());
        var user2 = new UserResponseDTO(2L, "user2", "user2@mail.com", null, Set.of(), Instant.now(), Instant.now());
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserResponseDTO>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(userService).getAllUsers();
    }

    @Test
    void getUser_returnsUser() {
        var user = new UserResponseDTO(1L, "user1", "user1@mail.com", null, Set.of(), Instant.now(), Instant.now());
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<UserResponseDTO> response = userController.getUser(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("user1", response.getBody().username());
        verify(userService).getUserById(1L);
    }

    @Test
    void createUser_callsServiceWithDto() {
        UserRequestDTO dto = new UserRequestDTO("newuser", "newuser@mail.com", "StrongP@ss1", "MAIN", Set.of("ADMIN"));
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "newuser", "newuser@mail.com", null, Set.of(), Instant.now(), Instant.now());

        when(userService.createUser(dto)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.createUser(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("newuser", response.getBody().username());
        verify(userService).createUser(dto);
    }

    @Test
    void updateUser_callsServiceWithDtoAndId() {
        UserRequestDTO dto = new UserRequestDTO("upduser", "upduser@mail.com", "StrongP@ss1", "MAIN", Set.of("ADMIN"));
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "upduser", "upduser@mail.com", null, Set.of(), Instant.now(), Instant.now());

        when(userService.updateUser(1L, dto)).thenReturn(responseDTO);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("upduser", response.getBody().username());
        verify(userService).updateUser(1L, dto);
    }

    @Test
    void deleteUser_callsServiceAndReturnsNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(userService).deleteUser(1L);
    }
}
