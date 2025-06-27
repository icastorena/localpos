package com.pds.localpos.userservice.repository

import com.pds.localpos.userservice.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, String> {

    fun findByNameIn(names: Collection<String>): Set<Role>
}
