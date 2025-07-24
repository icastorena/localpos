package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdjustStockRequest(
        @NotBlank(message = "{inventory.product_id.not_blank}")
        String productId,

        @NotBlank(message = "{inventory.store_id.not_blank}")
        String storeId,

        @NotNull(message = "{inventory.amount.not_null}")
        @Min(value = 1, message = "{inventory.amount.min}")
        Integer amount
) {
}
