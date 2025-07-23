package com.pds.localpos.orderservice.service.impl;

import com.pds.localpos.common.exception.BusinessException;
import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.orderservice.client.InventoryClient;
import com.pds.localpos.orderservice.dto.request.AdjustStockRequest;
import com.pds.localpos.orderservice.dto.request.CreateOrderItemRequest;
import com.pds.localpos.orderservice.dto.request.CreateOrderRequest;
import com.pds.localpos.orderservice.dto.request.PayOrderRequest;
import com.pds.localpos.orderservice.dto.response.OrderResponse;
import com.pds.localpos.orderservice.dto.response.OrderSummaryResponse;
import com.pds.localpos.orderservice.mapper.OrderMapper;
import com.pds.localpos.orderservice.model.Order;
import com.pds.localpos.orderservice.model.OrderItem;
import com.pds.localpos.orderservice.model.OrderStatus;
import com.pds.localpos.orderservice.model.OrderType;
import com.pds.localpos.orderservice.model.PaymentMethod;
import com.pds.localpos.orderservice.repository.OrderItemRepository;
import com.pds.localpos.orderservice.repository.OrderRepository;
import com.pds.localpos.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryClient inventoryClient;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        String orderId = generateOrderId();
        log.info("Creating new order: id={}, user={}, store={}", orderId, request.userId(), request.storeId());

        Order order = buildOrder(orderId, request);
        orderRepository.save(order);

        List<OrderItem> items = buildOrderItems(orderId, request.items());
        orderItemRepository.saveAll(items);

        BigDecimal total = calculateTotalAmount(items);
        updateOrderTotal(order, total);

        adjustInventoryStock(request.storeId(), request.items());

        log.info("Order created successfully: id={}, total={}", orderId, total);
        return OrderMapper.toResponse(order, items);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        log.info("Fetching order by ID: {}", orderId);
        Order order = findOrderByIdOrThrow(orderId);
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return OrderMapper.toResponse(order, items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> listOrders(String storeId, String status) {
        log.info("Listing orders for store={} with status={}", storeId, status);
        OrderStatus orderStatus = parseOrderStatus(status);
        List<Order> orders = fetchOrders(storeId, orderStatus);
        return orders.stream().map(OrderMapper::toSummary).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse payOrder(String orderId, PayOrderRequest request) {
        log.info("Paying order: {}", orderId);
        Order order = findOrderByIdOrThrow(orderId);
        validateOrderPayable(order);

        order.setStatus(OrderStatus.COMPLETED);
        order.setPaymentMethod(parsePaymentMethod(request.paymentMethod()));
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // TODO: Integrate with payment-service and notify inventory-service
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return OrderMapper.toResponse(order, items);
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        log.warn("Cancelling order: {}", orderId);
        Order order = findOrderByIdOrThrow(orderId);
        validateOrderCancelable(order);

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        revertInventoryStock(order);
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString();
    }

    private Order buildOrder(String id, CreateOrderRequest req) {
        return Order.builder()
                .id(id)
                .storeId(req.storeId())
                .userId(req.userId())
                .status(OrderStatus.PENDING)
                .orderType(parseOrderType(req.orderType()))
                .paymentMethod(parsePaymentMethod(req.paymentMethod()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();
    }

    private List<OrderItem> buildOrderItems(String orderId, List<CreateOrderItemRequest> items) {
        return items.stream()
                .map(req -> buildOrderItem(orderId, req))
                .collect(Collectors.toList());
    }

    private OrderItem buildOrderItem(String orderId, CreateOrderItemRequest item) {
        BigDecimal price = fetchPriceFromProductService(item.productId());
        BigDecimal total = price.multiply(BigDecimal.valueOf(item.quantity()));
        return OrderItem.builder()
                .id(generateOrderId())
                .orderId(orderId)
                .productId(item.productId())
                .quantity(item.quantity())
                .price(price)
                .total(total)
                .build();
    }

    private void updateOrderTotal(Order order, BigDecimal total) {
        order.setTotalAmount(total);
        orderRepository.save(order);
    }

    private void adjustInventoryStock(String storeId, List<CreateOrderItemRequest> items) {
        items.forEach(item -> {
            AdjustStockRequest req = new AdjustStockRequest(item.productId(), storeId, item.quantity());
            try {
                inventoryClient.decreaseStock(req);
            } catch (Exception ex) {
                log.error("Failed to decrease stock for productId={} storeId={}: {}", item.productId(), storeId, ex.getMessage());
            }
        });
    }

    private void revertInventoryStock(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        items.forEach(item -> {
            AdjustStockRequest req = new AdjustStockRequest(item.getProductId(), order.getStoreId(), item.getQuantity());
            try {
                inventoryClient.increaseStock(req);
            } catch (Exception ex) {
                log.error("Failed to increase stock for productId={} storeId={}: {}", item.getProductId(), order.getStoreId(), ex.getMessage());
            }
        });
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        return items.stream().map(OrderItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order findOrderByIdOrThrow(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "order.not.found", id));
    }

    private OrderStatus parseOrderStatus(String status) {
        try {
            return status != null ? OrderStatus.valueOf(status) : null;
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid order status '{}', defaulting to all", status);
            return null;
        }
    }

    private List<Order> fetchOrders(String storeId, OrderStatus status) {
        return (status != null)
                ? orderRepository.findByStoreIdAndStatus(storeId, status)
                : orderRepository.findByStoreIdAndCreatedAtBetween(storeId, LocalDateTime.now().minusDays(7), LocalDateTime.now());
    }

    private void validateOrderPayable(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "order.already.paid", order.getId());
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "order.is.canceled", order.getId());
        }
    }

    private void validateOrderCancelable(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "order.cannot.cancel.paid", order.getId());
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "order.already.canceled", order.getId());
        }
    }

    private OrderType parseOrderType(String type) {
        try {
            return type != null ? OrderType.valueOf(type) : OrderType.SALE;
        } catch (IllegalArgumentException e) {
            return OrderType.SALE;
        }
    }

    private PaymentMethod parsePaymentMethod(String method) {
        try {
            return method != null ? PaymentMethod.valueOf(method) : PaymentMethod.CASH;
        } catch (IllegalArgumentException e) {
            return PaymentMethod.CASH;
        }
    }

    private BigDecimal fetchPriceFromProductService(String productId) {
        // TODO: Connect to product-service to fetch real price
        return BigDecimal.valueOf(100);
    }
}
