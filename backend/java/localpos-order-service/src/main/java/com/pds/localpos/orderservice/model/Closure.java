package com.pds.localpos.orderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "closures", schema = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Closure {

    @Id
    @Size(max = 36, message = "{closure.id.size}")
    private String id = UUID.randomUUID().toString();

    @NotBlank(message = "{closure.storeId.required}")
    @Size(max = 36, message = "{closure.storeId.size}")
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @NotBlank(message = "{closure.userId.required}")
    @Size(max = 36, message = "{closure.userId.size}")
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NotNull(message = "{closure.closureDate.required}")
    @Column(name = "closure_date", nullable = false)
    private LocalDate closureDate;

    @NotNull(message = "{closure.startDatetime.required}")
    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @NotNull(message = "{closure.endDatetime.required}")
    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @NotNull(message = "{closure.totalSales.required}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{closure.totalSales.min}")
    @Column(name = "total_sales", nullable = false)
    private BigDecimal totalSales;

    @NotNull(message = "{closure.totalCash.required}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{closure.totalCash.min}")
    @Column(name = "total_cash", nullable = false)
    private BigDecimal totalCash;

    @NotNull(message = "{closure.totalCard.required}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{closure.totalCard.min}")
    @Column(name = "total_card", nullable = false)
    private BigDecimal totalCard;

    @NotNull(message = "{closure.totalOther.required}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{closure.totalOther.min}")
    @Column(name = "total_other", nullable = false)
    private BigDecimal totalOther;

    @NotNull(message = "{closure.totalOrders.required}")
    @Min(value = 0, message = "{closure.totalOrders.min}")
    @Column(name = "total_orders", nullable = false)
    private Integer totalOrders;

    @NotNull(message = "{closure.status.required}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClosureStatus status;

    @NotNull(message = "{closure.createdAt.required}")
    @PastOrPresent(message = "{closure.createdAt.pastOrPresent}")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull(message = "{closure.updatedAt.required}")
    @PastOrPresent(message = "{closure.updatedAt.pastOrPresent}")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
