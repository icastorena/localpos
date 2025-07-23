package com.pds.localpos.userservice.service;

import com.pds.localpos.userservice.dto.response.RoleResponse;

import java.util.Set;

public interface RoleService {

    Set<RoleResponse> getAllRoles();
}
