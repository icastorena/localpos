package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.userservice.dto.RoleDTO;
import com.pds.localpos.userservice.mapper.RoleMapper;
import com.pds.localpos.userservice.repository.RoleRepository;
import com.pds.localpos.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Set<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
