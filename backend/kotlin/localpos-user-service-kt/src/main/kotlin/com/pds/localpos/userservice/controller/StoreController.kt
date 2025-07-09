package com.pds.localpos.userservice.controller

import com.pds.localpos.userservice.dto.StoreDTO
import com.pds.localpos.userservice.service.StoreService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stores")
class StoreController(
    private val storeService: StoreService
) {

    private val logger = LoggerFactory.getLogger(StoreController::class.java)

    @GetMapping
    fun getAllStores(): ResponseEntity<Set<StoreDTO>> {
        logger.info("Fetching all stores")
        val stores = storeService.getAllStores()
        logger.info("Found {} stores", stores.size)
        return ResponseEntity.ok(stores)
    }
}
