package com.pds.localpos.orderservice.repository;

import com.pds.localpos.orderservice.model.Order;
import com.pds.localpos.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByStoreIdAndStatus(String storeId, OrderStatus status);

    List<Order> findByStoreIdAndCreatedAtBetween(String storeId, LocalDateTime from, LocalDateTime to);
}
