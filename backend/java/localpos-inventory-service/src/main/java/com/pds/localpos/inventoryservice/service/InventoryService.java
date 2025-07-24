package com.pds.localpos.inventoryservice.service;

import com.pds.localpos.inventoryservice.dto.request.AdjustStockRequest;
import com.pds.localpos.inventoryservice.dto.response.InventoryResponse;
import com.pds.localpos.inventoryservice.dto.request.UpdateStockRequest;

public interface InventoryService {

    InventoryResponse getByProductAndStore(String productId, String storeId);

    void increaseStock(AdjustStockRequest request);

    void decreaseStock(AdjustStockRequest request);

    void updateStock(UpdateStockRequest request);
}
