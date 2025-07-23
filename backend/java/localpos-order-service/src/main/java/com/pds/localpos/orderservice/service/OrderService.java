package com.pds.localpos.orderservice.service;

import com.pds.localpos.orderservice.dto.request.CreateOrderRequest;
import com.pds.localpos.orderservice.dto.response.OrderResponse;
import com.pds.localpos.orderservice.dto.response.OrderSummaryResponse;
import com.pds.localpos.orderservice.dto.request.PayOrderRequest;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(String orderId);

    List<OrderSummaryResponse> listOrders(String storeId, String status);

    OrderResponse payOrder(String orderId, PayOrderRequest payRequest);

    void cancelOrder(String orderId);
}
