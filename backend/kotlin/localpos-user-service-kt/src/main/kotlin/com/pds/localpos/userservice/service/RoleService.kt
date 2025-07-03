package com.pds.localpos.userservice.service

import com.pds.localpos.userservice.dto.RoleDTO

interface RoleService {

    fun getAllRoles(): Set<RoleDTO>
}