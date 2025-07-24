package com.pds.localpos.orderservice.controller;

import com.pds.localpos.orderservice.dto.request.CreateOrderRequest;
import com.pds.localpos.orderservice.dto.request.PayOrderRequest;
import com.pds.localpos.orderservice.dto.response.OrderResponse;
import com.pds.localpos.orderservice.dto.response.OrderSummaryResponse;
import com.pds.localpos.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("POST /orders - Creating order for store: {}", request.storeId());
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        log.info("GET /orders/{} - Fetching order by ID", orderId);
        OrderResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderSummaryResponse>> listOrders(
            @RequestParam String storeId,
            @RequestParam(required = false) String status) {

        log.info("GET /orders?storeId={}&status={} - Listing orders", storeId, status);
        List<OrderSummaryResponse> orders = orderService.listOrders(storeId, status);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResponse> payOrder(
            @PathVariable String orderId,
            @Valid @RequestBody PayOrderRequest request) {

        log.info("POST /orders/{}/pay - Processing payment", orderId);
        OrderResponse response = orderService.payOrder(orderId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        log.warn("POST /orders/{}/cancel - Cancelling order", orderId);
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
