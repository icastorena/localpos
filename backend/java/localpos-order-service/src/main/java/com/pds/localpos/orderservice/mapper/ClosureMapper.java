package com.pds.localpos.orderservice.mapper;

import com.pds.localpos.orderservice.dto.response.ClosureResponse;
import com.pds.localpos.orderservice.model.Closure;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClosureMapper {

    public static ClosureResponse toResponse(Closure closure) {
        return new ClosureResponse(
                closure.getId(),
                closure.getStoreId(),
                closure.getUserId(),
                closure.getClosureDate(),
                closure.getStartDatetime(),
                closure.getEndDatetime(),
                closure.getTotalSales(),
                closure.getTotalCash(),
                closure.getTotalCard(),
                closure.getTotalOther(),
                closure.getTotalOrders(),
                closure.getStatus().name(),
                closure.getCreatedAt(),
                closure.getUpdatedAt()
        );
    }
}
