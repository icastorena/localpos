package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotBlank(message = "{order.item.product_id.not_blank}")
        String productId,

        @NotNull(message = "{order.item.quantity.not_null}")
        @Min(value = 1, message = "{order.item.quantity.min}")
        Integer quantity
) {
}
