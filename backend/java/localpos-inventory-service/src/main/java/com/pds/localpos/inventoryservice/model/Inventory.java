package com.pds.localpos.inventoryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory", schema = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotBlank(message = "{inventory.product_id.not_blank}")
    @Column(name = "product_id", nullable = false)
    private String productId;

    @NotBlank(message = "{inventory.store_id.not_blank}")
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @NotNull(message = "{inventory.quantity.not_null}")
    @Min(value = 0, message = "{inventory.quantity.min}")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "{inventory.status.not_null}")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status = InventoryStatus.ACTIVE;

    @NotNull(message = "{inventory.created_at.not_null}")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull(message = "{inventory.updated_at.not_null}")
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_counted_at")
    private LocalDateTime lastCountedAt;
}
