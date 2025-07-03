package com.pds.localpos.userservice.repository

import com.pds.localpos.userservice.model.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : JpaRepository<Store, String> {

    fun findByCodeIn(codes: Collection<String>): Set<Store>
}
