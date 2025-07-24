package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotBlank(message = "{order.store_id.not_blank}")
        String storeId,

        @NotBlank(message = "{order.user_id.not_blank}")
        String userId,

        @NotEmpty(message = "{order.items.not_empty}")
        List<@Valid CreateOrderItemRequest> items,

        @NotBlank(message = "{order.type.not_blank}")
        String orderType,

        @NotBlank(message = "{order.payment_method.not_blank}")
        String paymentMethod
) {
}
