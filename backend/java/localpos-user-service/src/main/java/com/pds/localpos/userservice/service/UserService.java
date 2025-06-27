package com.pds.localpos.userservice.service;

import com.pds.localpos.userservice.dto.UserRequestDTO;
import com.pds.localpos.userservice.dto.UserResponseDTO;
import com.pds.localpos.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO getUserById(String id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(String id, UserRequestDTO dto);

    void deleteUser(String id);

    User findByUsername(String username);
}
