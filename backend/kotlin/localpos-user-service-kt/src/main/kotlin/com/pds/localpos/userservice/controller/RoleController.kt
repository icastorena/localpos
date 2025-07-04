package com.pds.localpos.userservice.controller

import com.pds.localpos.userservice.dto.RoleDTO
import com.pds.localpos.userservice.service.RoleService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/roles")
class RoleController(
    private val roleService: RoleService
) {

    private val logger = LoggerFactory.getLogger(RoleController::class.java)

    @GetMapping
    fun getAllRoles(): ResponseEntity<Set<RoleDTO>> {
        logger.info("Fetching all roles")
        val roles = roleService.getAllRoles()
        logger.info("Fetched {} roles", roles.size)
        return ResponseEntity.ok(roles)
    }
}
