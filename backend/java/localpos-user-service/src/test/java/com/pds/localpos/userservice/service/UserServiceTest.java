package com.pds.localpos.userservice.service;

import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.exception.BusinessException;
import com.pds.localpos.userservice.exception.ResourceNotFoundException;
import com.pds.localpos.userservice.model.Role;
import com.pds.localpos.userservice.model.Store;
import com.pds.localpos.userservice.model.User;
import com.pds.localpos.userservice.repository.RoleRepository;
import com.pds.localpos.userservice.repository.StoreRepository;
import com.pds.localpos.userservice.repository.UserRepository;
import com.pds.localpos.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private StoreRepository storeRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        storeRepository = mock(StoreRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, roleRepository, storeRepository, passwordEncoder);
    }

    @Test
    void createUser_shouldThrow_whenUsernameExists() {
        when(userRepository.existsByUsername("username")).thenReturn(true);
        UserRequestDTO dto = newUserRequestDTO();
        BusinessException ex = assertThrows(BusinessException.class, () -> userService.createUser(dto));
        assertEquals("user.username.exists", ex.getMessage());
    }

    @Test
    void createUser_shouldThrow_whenEmailExists() {
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(true);
        UserRequestDTO dto = newUserRequestDTO();
        BusinessException ex = assertThrows(BusinessException.class, () -> userService.createUser(dto));
        assertEquals("user.email.exists", ex.getMessage());
    }

    @Test
    void createUser_shouldThrow_whenStoreNotFound() {
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        when(storeRepository.findByCode("storeCode")).thenReturn(Optional.empty());
        UserRequestDTO dto = newUserRequestDTO();
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> userService.createUser(dto));
        assertEquals("store.not_found", ex.getMessage());
    }

    @Test
    void createUser_shouldThrow_whenRolesInvalid() {
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        when(storeRepository.findByCode("storeCode")).thenReturn(Optional.of(new Store()));
        when(roleRepository.findByNameIn(Set.of("ROLE_USER"))).thenReturn(Set.of());
        UserRequestDTO dto = newUserRequestDTO();
        BusinessException ex = assertThrows(BusinessException.class, () -> userService.createUser(dto));
        assertEquals("roles.invalid", ex.getMessage());
    }

    @Test
    void createUser_shouldCreate_whenValid() {
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        Store store = new Store();
        store.setId(1L);
        when(storeRepository.findByCode("storeCode")).thenReturn(Optional.of(store));
        when(roleRepository.findByNameIn(Set.of("ROLE_USER"))).thenReturn(Set.of(newRole()));
        when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        UserRequestDTO dto = newUserRequestDTO();
        UserResponseDTO result = userService.createUser(dto);
        assertNotNull(result);
        assertEquals("username", result.username());
    }

    @Test
    void updateUser_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1L, newUserRequestDTO()));
    }

    @Test
    void updateUser_shouldThrow_whenUsernameExistsAndDifferent() {
        User user = new User();
        user.setId(1L);
        user.setUsername("old");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("new")).thenReturn(true);
        UserRequestDTO dto = new UserRequestDTO("new", "email@example.com", "Password1!", "storeCode", Set.of("ROLE_USER"));
        assertThrows(BusinessException.class, () -> userService.updateUser(1L, dto));
    }

    @Test
    void updateUser_shouldThrow_whenEmailExistsAndDifferent() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("old@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);
        UserRequestDTO dto = new UserRequestDTO("username", "new@example.com", "Password1!", "storeCode", Set.of("ROLE_USER"));
        assertThrows(BusinessException.class, () -> userService.updateUser(1L, dto));
    }

    @Test
    void updateUser_shouldThrow_whenRolesMissing() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(roleRepository.findByNameIn(Set.of("ROLE_USER", "ROLE_MISSING"))).thenReturn(Set.of(newRole()));
        UserRequestDTO dto = new UserRequestDTO("user", "user@example.com", "Password1!", "storeCode", Set.of("ROLE_USER", "ROLE_MISSING"));
        BusinessException ex = assertThrows(BusinessException.class, () -> userService.updateUser(1L, dto));
        assertEquals("roles.missing", ex.getMessage());
    }

    @Test
    void updateUser_shouldAccept_whenRoleNamesIsNull() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        UserRequestDTO dto = new UserRequestDTO("user", "user@example.com", "Password1!", "storeCode", null);
        UserResponseDTO response = userService.updateUser(1L, dto);
        assertNotNull(response);
    }

    @Test
    void updateUser_shouldAccept_whenRoleNamesIsEmpty() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        UserRequestDTO dto = new UserRequestDTO("user", "user@example.com", "Password1!", "storeCode", Set.of());
        UserResponseDTO response = userService.updateUser(1L, dto);
        assertNotNull(response);
    }

    @Test
    void updateUser_shouldUpdate_whenValid() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setEmail("user@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(roleRepository.findByNameIn(Set.of("ROLE_USER"))).thenReturn(Set.of(newRole()));
        when(passwordEncoder.encode("Password1!")).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        UserRequestDTO dto = newUserRequestDTO();
        UserResponseDTO response = userService.updateUser(1L, dto);
        assertNotNull(response);
    }

    @Test
    void getUserById_shouldReturn_whenExists() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserResponseDTO result = userService.getUserById(1L);
        assertEquals("test", result.username());
    }

    @Test
    void getUserById_shouldThrow_whenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getAllUsers_shouldReturnList() {
        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("u1");
        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("u2");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));
        List<UserResponseDTO> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void deleteUser_shouldThrow_whenNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void deleteUser_shouldDelete_whenExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    private UserRequestDTO newUserRequestDTO() {
        return new UserRequestDTO("username", "email@example.com", "Password1!", "storeCode", Set.of("ROLE_USER"));
    }

    private Role newRole() {
        return Role.builder().id(1L).name("ROLE_USER").description("User").build();
    }
}
