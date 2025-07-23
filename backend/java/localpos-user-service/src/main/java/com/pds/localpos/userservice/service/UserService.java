package com.pds.localpos.userservice.service;

import com.pds.localpos.userservice.dto.request.UserRequest;
import com.pds.localpos.userservice.dto.response.UserResponse;
import com.pds.localpos.userservice.model.User;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest dto);

    UserResponse getUserById(String id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(String id, UserRequest dto);

    void deleteUser(String id);

    User findByUsername(String username);
}
