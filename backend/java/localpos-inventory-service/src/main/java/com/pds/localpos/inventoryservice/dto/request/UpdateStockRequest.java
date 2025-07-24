package com.pds.localpos.inventoryservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateStockRequest(

        @NotBlank(message = "{inventory.product_id.not_blank}")
        String productId,

        @NotBlank(message = "{inventory.store_id.not_blank}")
        String storeId,

        @NotNull(message = "{inventory.quantity.not_null}")
        @Min(value = 0, message = "{inventory.quantity.min}")
        Integer quantity

) {
}
