package com.pds.localpos.orderservice.dto.request;

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