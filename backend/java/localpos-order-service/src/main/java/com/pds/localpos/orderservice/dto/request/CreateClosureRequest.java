package com.pds.localpos.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateClosureRequest(
        @NotBlank(message = "{closure.store_id.not_blank}")
        String storeId,

        @NotBlank(message = "{closure.user_id.not_blank}")
        String userId,

        @NotNull(message = "{closure.date.not_null}")
        LocalDate closureDate,

        @NotNull(message = "{closure.start_datetime.not_null}")
        LocalDateTime startDatetime,

        @NotNull(message = "{closure.end_datetime.not_null}")
        LocalDateTime endDatetime
) {
}
