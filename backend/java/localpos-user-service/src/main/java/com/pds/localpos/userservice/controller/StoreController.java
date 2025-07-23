package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.response.StoreResponse;
import com.pds.localpos.userservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<Set<StoreResponse>> getAllStores() {
        log.info("GET /stores - Fetching all stores");
        Set<StoreResponse> stores = storeService.getAllStores();
        log.info("Fetched {} stores", stores.size());
        return ResponseEntity.ok(stores);
    }
}
