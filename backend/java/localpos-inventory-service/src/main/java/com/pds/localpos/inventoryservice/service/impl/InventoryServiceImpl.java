package com.pds.localpos.inventoryservice.service.impl;

import com.pds.localpos.common.exception.BusinessException;
import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.inventoryservice.dto.request.AdjustStockRequest;
import com.pds.localpos.inventoryservice.dto.request.UpdateStockRequest;
import com.pds.localpos.inventoryservice.dto.response.InventoryResponse;
import com.pds.localpos.inventoryservice.mapper.InventoryMapper;
import com.pds.localpos.inventoryservice.model.Inventory;
import com.pds.localpos.inventoryservice.repository.InventoryRepository;
import com.pds.localpos.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProductAndStore(String productId, String storeId) {
        log.info("Fetching inventory for product={} and store={}", productId, storeId);
        Inventory inventory = getInventoryOrThrow(productId, storeId);
        return InventoryMapper.toResponse(inventory);
    }

    @Override
    @Transactional
    public void increaseStock(AdjustStockRequest request) {
        log.info("Increasing stock: product={}, store={}, amount={}", request.productId(), request.storeId(), request.amount());
        Inventory inventory = getInventoryOrThrow(request.productId(), request.storeId());

        inventory.setQuantity(inventory.getQuantity() + request.amount());
        inventory.setUpdatedAt(LocalDateTime.now());

        inventoryRepository.save(inventory);
        log.info("Stock increased successfully");
    }

    @Override
    @Transactional
    public void decreaseStock(AdjustStockRequest request) {
        log.info("Decreasing stock: product={}, store={}, amount={}", request.productId(), request.storeId(), request.amount());
        Inventory inventory = getInventoryOrThrow(request.productId(), request.storeId());

        if (inventory.getQuantity() < request.amount()) {
            log.warn("Insufficient stock: available={}, requested={}", inventory.getQuantity(), request.amount());
            throw new BusinessException(HttpStatus.BAD_REQUEST, "inventory.insufficient_stock", inventory.getProductId(), inventory.getStoreId());
        }

        inventory.setQuantity(inventory.getQuantity() - request.amount());
        inventory.setUpdatedAt(LocalDateTime.now());

        inventoryRepository.save(inventory);
        log.info("Stock decreased successfully");
    }

    @Override
    @Transactional
    public void updateStock(UpdateStockRequest request) {
        log.info("Updating stock: product={}, store={}, new quantity={}", request.productId(), request.storeId(), request.quantity());
        Inventory inventory = getInventoryOrThrow(request.productId(), request.storeId());

        inventory.setQuantity(request.quantity());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventory.setLastCountedAt(LocalDateTime.now());

        inventoryRepository.save(inventory);
        log.info("Stock updated successfully");
    }

    private Inventory getInventoryOrThrow(String productId, String storeId) {
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId)
                .orElseThrow(() -> {
                    log.warn("Inventory not found: product={}, store={}", productId, storeId);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "inventory.not_found", productId, storeId);
                });
    }
}
