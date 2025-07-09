package com.pds.localpos.orderservice.mapper;

import com.pds.localpos.orderservice.dto.OrderItemResponse;
import com.pds.localpos.orderservice.dto.OrderResponse;
import com.pds.localpos.orderservice.dto.OrderSummaryResponse;
import com.pds.localpos.orderservice.model.Order;
import com.pds.localpos.orderservice.model.OrderItem;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class OrderMapper {

    public static OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(OrderItemMapper::toResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getStoreId(),
                order.getUserId(),
                order.getStatus().name(),
                order.getOrderType().name(),
                order.getPaymentMethod().name(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                itemResponses
        );
    }

    public static OrderSummaryResponse toSummary(Order order) {
        return new OrderSummaryResponse(
                order.getId(),
                order.getStoreId(),
                order.getUserId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }
}