package com.pds.localpos.orderservice.service.impl;

import com.pds.localpos.common.exception.BusinessException;
import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.orderservice.dto.CreateOrderItemRequest;
import com.pds.localpos.orderservice.dto.CreateOrderRequest;
import com.pds.localpos.orderservice.dto.OrderResponse;
import com.pds.localpos.orderservice.dto.OrderSummaryResponse;
import com.pds.localpos.orderservice.dto.PayOrderRequest;
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

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        String orderId = UUID.randomUUID().toString();
        log.info("Creating new order with id={} for user={} store={}", orderId, request.userId(), request.storeId());

        Order order = Order.builder()
                .id(orderId)
                .storeId(request.storeId())
                .userId(request.userId())
                .status(OrderStatus.PENDING)
                .orderType(parseOrderType(request.orderType()))
                .paymentMethod(parsePaymentMethod(request.paymentMethod()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        orderRepository.save(order);

        List<OrderItem> items = request.items().stream()
                .map(itemReq -> buildOrderItem(orderId, itemReq))
                .collect(Collectors.toList());

        orderItemRepository.saveAll(items);

        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // TODO: llamar a inventory-service para descontar stock
        // inventoryClient.decreaseStock(items);

        log.info("Order created successfully with id={} totalAmount={}", orderId, totalAmount);

        return OrderMapper.toResponse(order, items);
    }

    private OrderItem buildOrderItem(String orderId, CreateOrderItemRequest itemReq) {
        BigDecimal price = fetchPriceFromProductService(itemReq.productId());
        BigDecimal total = price.multiply(BigDecimal.valueOf(itemReq.quantity()));

        return OrderItem.builder()
                .id(UUID.randomUUID().toString())
                .orderId(orderId)
                .productId(itemReq.productId())
                .quantity(itemReq.quantity())
                .price(price)
                .total(total)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        log.info("Fetching order by id={}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "order.not.found", orderId));

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        return OrderMapper.toResponse(order, items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> listOrders(String storeId, String status) {
        log.info("Listing orders for storeId={} with status={}", storeId, status);
        OrderStatus orderStatus = null;
        if (status != null) {
            try {
                orderStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid order status filter '{}', ignoring filter", status);
            }
        }

        List<Order> orders = (orderStatus != null)
                ? orderRepository.findByStoreIdAndStatus(storeId, orderStatus)
                : orderRepository.findByStoreIdAndCreatedAtBetween(
                storeId,
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now()
        );

        return orders.stream()
                .map(OrderMapper::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse payOrder(String orderId, PayOrderRequest payRequest) {
        log.info("Processing payment for orderId={} with paymentMethod={}", orderId, payRequest.paymentMethod());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "order.not.found", orderId));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "order.already.paid", orderId);
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "order.is.canceled", orderId);
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setPaymentMethod(parsePaymentMethod(payRequest.paymentMethod()));
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // TODO: Integración con payment-service y actualización inventory-service

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return OrderMapper.toResponse(order, items);
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        log.info("Canceling order with id={}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "order.not.found", orderId));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST, "order.cannot.cancel.paid", orderId);
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST, "order.already.canceled", orderId);
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        // TODO: Revertir stock en inventory-service
    }

    private OrderType parseOrderType(String orderType) {
        try {
            return orderType != null ? OrderType.valueOf(orderType) : OrderType.SALE;
        } catch (IllegalArgumentException e) {
            return OrderType.SALE;
        }
    }

    private PaymentMethod parsePaymentMethod(String paymentMethod) {
        try {
            return paymentMethod != null ? PaymentMethod.valueOf(paymentMethod) : PaymentMethod.CASH;
        } catch (IllegalArgumentException e) {
            return PaymentMethod.CASH;
        }
    }

    private BigDecimal fetchPriceFromProductService(String productId) {
        // TODO: Implementar llamada a product-service para obtener precio actual
        return BigDecimal.valueOf(100);
    }
}
