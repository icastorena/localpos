package com.pds.localpos.orderservice.service;

import com.pds.localpos.orderservice.dto.request.CreateClosureRequest;
import com.pds.localpos.orderservice.dto.request.FinalizeClosureRequest;
import com.pds.localpos.orderservice.dto.request.UpdateClosureStatusRequest;
import com.pds.localpos.orderservice.dto.response.ClosureResponse;

import java.util.List;

public interface ClosureService {

    ClosureResponse createClosure(CreateClosureRequest request);

    ClosureResponse getClosureById(String id);

    List<ClosureResponse> getClosures(String storeId, String userId, String status, String dateFrom, String dateTo);

    ClosureResponse updateClosureStatus(String closureId, UpdateClosureStatusRequest request);

    ClosureResponse finalizeClosure(String id, FinalizeClosureRequest request);
}
