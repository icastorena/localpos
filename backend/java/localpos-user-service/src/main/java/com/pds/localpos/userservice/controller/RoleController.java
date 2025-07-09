package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.RoleDTO;
import com.pds.localpos.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Set<RoleDTO>> getAllRoles() {
        log.info("Fetching all roles");
        Set<RoleDTO> roles = roleService.getAllRoles();
        log.info("Fetched {} roles", roles.size());
        return ResponseEntity.ok(roles);
    }
}
