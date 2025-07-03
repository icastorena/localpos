package com.pds.localpos.userservice.service.impl

import com.pds.localpos.userservice.dto.RoleDTO
import com.pds.localpos.userservice.mapper.toDTO
import com.pds.localpos.userservice.repository.RoleRepository
import com.pds.localpos.userservice.service.RoleService
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl(private val roleRepository: RoleRepository) : RoleService {

    override fun getAllRoles(): Set<RoleDTO> {
        return roleRepository.findAll()
            .map { it.toDTO() }
            .toSet()
    }
}