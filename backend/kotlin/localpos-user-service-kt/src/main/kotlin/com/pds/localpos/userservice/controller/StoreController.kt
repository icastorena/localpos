package com.pds.localpos.userservice.controller

import com.pds.localpos.userservice.dto.StoreDTO
import com.pds.localpos.userservice.service.StoreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(
    private val storeService: StoreService
) {

    @GetMapping
    fun getAllStores(): ResponseEntity<Set<StoreDTO>> {
        val stores = storeService.getAllStores()
        return ResponseEntity.ok(stores)
    }
}