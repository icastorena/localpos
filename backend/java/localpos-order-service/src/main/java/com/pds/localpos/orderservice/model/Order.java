package com.pds.localpos.orderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders", schema = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @Size(max = 36, message = "{order.id.size}")
    private String id = UUID.randomUUID().toString();

    @NotBlank(message = "{order.userId.required}")
    @Size(max = 36, message = "{order.userId.size}")
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NotBlank(message = "{order.storeId.required}")
    @Size(max = 36, message = "{order.storeId.size}")
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @NotNull(message = "{order.totalAmount.required}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{order.totalAmount.min}")
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @NotNull(message = "{order.status.required}")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    @NotNull(message = "{order.paymentMethod.required}")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 50)
    private OrderType orderType;

    @NotNull(message = "{order.createdAt.required}")
    @PastOrPresent(message = "{order.createdAt.pastOrPresent}")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "{order.updatedAt.required}")
    @PastOrPresent(message = "{order.updatedAt.pastOrPresent}")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
