package com.pds.localpos.userservice.controller;

import com.pds.localpos.userservice.dto.StoreDTO;
import com.pds.localpos.userservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<Set<StoreDTO>> getAllStores() {
        Set<StoreDTO> storeDTOSet = storeService.getAllStores();
        return ResponseEntity.ok(storeDTOSet);
    }
}
