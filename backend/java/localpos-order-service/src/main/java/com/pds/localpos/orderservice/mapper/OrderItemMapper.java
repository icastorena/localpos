package com.pds.localpos.orderservice.mapper;

import com.pds.localpos.orderservice.dto.OrderItemResponse;
import com.pds.localpos.orderservice.model.OrderItem;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderItemMapper {

    public static OrderItemResponse toResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getOrderId(),
                item.getProductId(),
                item.getQuantity(),
                item.getPrice(),
                item.getTotal()
        );
    }
}