package com.pds.localpos.orderservice.service;

import com.pds.localpos.orderservice.dto.CreateOrderRequest;
import com.pds.localpos.orderservice.dto.OrderResponse;
import com.pds.localpos.orderservice.dto.OrderSummaryResponse;
import com.pds.localpos.orderservice.dto.PayOrderRequest;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(String orderId);

    List<OrderSummaryResponse> listOrders(String storeId, String status);

    OrderResponse payOrder(String orderId, PayOrderRequest payRequest);

    void cancelOrder(String orderId);
}
