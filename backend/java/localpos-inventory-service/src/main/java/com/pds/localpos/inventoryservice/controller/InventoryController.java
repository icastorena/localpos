package com.pds.localpos.inventoryservice.controller;

import com.pds.localpos.inventoryservice.dto.request.AdjustStockRequest;
import com.pds.localpos.inventoryservice.dto.request.UpdateStockRequest;
import com.pds.localpos.inventoryservice.dto.response.InventoryResponse;
import com.pds.localpos.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(
            @PathVariable String storeId,
            @PathVariable String productId
    ) {
        log.info("GET /inventory/{}/{} - Fetching inventory", storeId, productId);
        InventoryResponse response = inventoryService.getByProductAndStore(productId, storeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateStock(@Valid @RequestBody UpdateStockRequest request) {
        log.info("PUT /inventory/update - Updating stock for product={}, store={}, quantity={}",
                request.productId(), request.storeId(), request.quantity());
        inventoryService.updateStock(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/decrease")
    public ResponseEntity<Void> decreaseStock(@Valid @RequestBody AdjustStockRequest request) {
        log.info("POST /inventory/decrease - Decreasing stock for product={}, store={}, amount={}",
                request.productId(), request.storeId(), request.amount());
        inventoryService.decreaseStock(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/increase")
    public ResponseEntity<Void> increaseStock(@Valid @RequestBody AdjustStockRequest request) {
        log.info("POST /inventory/increase - Increasing stock for product={}, store={}, amount={}",
                request.productId(), request.storeId(), request.amount());
        inventoryService.increaseStock(request);
        return ResponseEntity.noContent().build();
    }
}
