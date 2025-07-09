package com.pds.localpos.orderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items", schema = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @Size(max = 36, message = "{orderItem.id.size}")
    private String id = UUID.randomUUID().toString();

    @NotBlank(message = "{orderItem.orderId.required}")
    @Size(max = 36, message = "{orderItem.orderId.size}")
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @NotBlank(message = "{orderItem.productId.required}")
    @Size(max = 36, message = "{orderItem.productId.size}")
    @Column(name = "product_id", nullable = false)
    private String productId;

    @NotNull(message = "{orderItem.quantity.required}")
    @Min(value = 1, message = "{orderItem.quantity.min}")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "{orderItem.price.required}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{orderItem.price.min}")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "{orderItem.total.required}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{orderItem.total.min}")
    @Column(nullable = false)
    private BigDecimal total;
}
