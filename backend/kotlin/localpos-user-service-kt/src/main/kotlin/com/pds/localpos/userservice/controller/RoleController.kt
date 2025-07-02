package com.pds.localpos.userservice.controller

import com.pds.localpos.userservice.dto.RoleDTO
import com.pds.localpos.userservice.service.RoleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/roles")
class RoleController(
    private val roleService: RoleService
) {

    @GetMapping
    fun getAllRoles(): ResponseEntity<List<RoleDTO>> {
        val roles = roleService.getAllRoles()
        return ResponseEntity.ok(roles)
    }
}