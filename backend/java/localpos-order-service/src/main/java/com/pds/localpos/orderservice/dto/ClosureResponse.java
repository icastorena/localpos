package com.pds.localpos.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClosureResponse(
        String id,
        String storeId,
        String userId,
        LocalDate closureDate,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        BigDecimal totalSales,
        BigDecimal totalCash,
        BigDecimal totalCard,
        BigDecimal totalOther,
        Integer totalOrders,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}