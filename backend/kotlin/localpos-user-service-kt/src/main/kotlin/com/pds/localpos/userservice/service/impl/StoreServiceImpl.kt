package com.pds.localpos.userservice.service.impl

import com.pds.localpos.userservice.dto.StoreDTO
import com.pds.localpos.userservice.mapper.toDTO
import com.pds.localpos.userservice.repository.StoreRepository
import com.pds.localpos.userservice.service.StoreService
import org.springframework.stereotype.Service

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository
) : StoreService {

    override fun getAllStores(): Set<StoreDTO> {
        return storeRepository.findAll()
            .map { it.toDTO() }
            .toSet()
    }
}