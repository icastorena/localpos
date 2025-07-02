package com.pds.localpos.userservice.service

import com.pds.localpos.userservice.dto.StoreDTO

interface StoreService {

    fun getAllStores(): List<StoreDTO>
}