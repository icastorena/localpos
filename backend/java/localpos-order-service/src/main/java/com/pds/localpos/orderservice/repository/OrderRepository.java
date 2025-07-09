package com.pds.localpos.orderservice.repository;

import com.pds.localpos.orderservice.model.Order;
import com.pds.localpos.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByStoreIdAndStatus(String storeId, OrderStatus status);

    List<Order> findByStoreIdAndCreatedAtBetween(String storeId, LocalDateTime from, LocalDateTime to);
}
