package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.userservice.dto.response.RoleResponse;
import com.pds.localpos.userservice.mapper.RoleMapper;
import com.pds.localpos.userservice.repository.RoleRepository;
import com.pds.localpos.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Set<RoleResponse> getAllRoles() {
        log.info("Fetching all roles");
        Set<RoleResponse> roles = roleRepository.findAll().stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toSet());
        log.info("Fetched {} roles", roles.size());
        return roles;
    }
}
