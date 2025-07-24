package com.pds.localpos.orderservice.controller;

import com.pds.localpos.orderservice.dto.request.CreateClosureRequest;
import com.pds.localpos.orderservice.dto.request.FinalizeClosureRequest;
import com.pds.localpos.orderservice.dto.request.UpdateClosureStatusRequest;
import com.pds.localpos.orderservice.dto.response.ClosureResponse;
import com.pds.localpos.orderservice.service.ClosureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/closures")
@RequiredArgsConstructor
public class ClosureController {

    private final ClosureService closureService;

    @PostMapping
    public ResponseEntity<ClosureResponse> createClosure(@Valid @RequestBody CreateClosureRequest request) {
        log.info("POST /closures - Creating closure for storeId: {}, userId: {}", request.storeId(), request.userId());
        ClosureResponse response = closureService.createClosure(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClosureResponse> getClosureById(@PathVariable String id) {
        log.info("GET /closures/{} - Retrieving closure by ID", id);
        ClosureResponse response = closureService.getClosureById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClosureResponse>> getClosures(
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        log.info("GET /closures - Filtering closures: storeId={}, userId={}, status={}, dateFrom={}, dateTo={}",
                storeId, userId, status, dateFrom, dateTo);
        List<ClosureResponse> closures = closureService.getClosures(storeId, userId, status, dateFrom, dateTo);
        return ResponseEntity.ok(closures);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClosureResponse> updateClosureStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateClosureStatusRequest request
    ) {
        log.info("PATCH /closures/{}/status - Updating status to '{}' by user '{}'", id, request.status(), request.updatedBy());
        ClosureResponse response = closureService.updateClosureStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<ClosureResponse> finalizeClosure(
            @PathVariable String id,
            @Valid @RequestBody FinalizeClosureRequest request
    ) {
        log.info("POST /closures/{}/finalize - Finalizing closure by user '{}'", id, request.updatedBy());
        ClosureResponse response = closureService.finalizeClosure(id, request);
        return ResponseEntity.ok(response);
    }
}
