package com.pds.localpos.orderservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateClosureRequest(
        String storeId,
        String userId,
        LocalDate closureDate,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime
) {
}