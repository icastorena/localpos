package com.pds.localpos.userservice.service;

import com.pds.localpos.userservice.dto.request.LoginRequest;
import com.pds.localpos.userservice.dto.response.UserResponse;

public interface AuthService {

    UserResponse validateCredentials(LoginRequest  request);
}
