package com.pds.localpos.inventoryservice.mapper;

import com.pds.localpos.inventoryservice.dto.response.InventoryResponse;
import com.pds.localpos.inventoryservice.model.Inventory;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class InventoryMapper {

    public static InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getStoreId(),
                inventory.getQuantity(),
                inventory.getStatus().name(),
                inventory.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}
