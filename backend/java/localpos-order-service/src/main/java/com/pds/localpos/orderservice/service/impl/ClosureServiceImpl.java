package com.pds.localpos.orderservice.service.impl;

import com.pds.localpos.common.exception.BusinessException;
import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.orderservice.dto.request.CreateClosureRequest;
import com.pds.localpos.orderservice.dto.request.FinalizeClosureRequest;
import com.pds.localpos.orderservice.dto.request.UpdateClosureStatusRequest;
import com.pds.localpos.orderservice.dto.response.ClosureResponse;
import com.pds.localpos.orderservice.mapper.ClosureMapper;
import com.pds.localpos.orderservice.model.Closure;
import com.pds.localpos.orderservice.model.ClosureStatus;
import com.pds.localpos.orderservice.model.Order;
import com.pds.localpos.orderservice.model.OrderStatus;
import com.pds.localpos.orderservice.repository.ClosureRepository;
import com.pds.localpos.orderservice.repository.OrderRepository;
import com.pds.localpos.orderservice.service.ClosureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClosureServiceImpl implements ClosureService {

    private final ClosureRepository closureRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public ClosureResponse createClosure(CreateClosureRequest request) {
        log.info("Creating closure for store={}, user={}, date={}", request.storeId(), request.userId(), request.closureDate());

        validateNoOverlap(request);

        List<Order> orders = orderRepository.findByStoreIdAndCreatedAtBetween(
                request.storeId(),
                request.startDatetime(),
                request.endDatetime()
        );

        if (orders.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "closure.no.orders.found", request.storeId());
        }

        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal totalCash = BigDecimal.ZERO;
        BigDecimal totalCard = BigDecimal.ZERO;
        BigDecimal totalOther = BigDecimal.ZERO;

        for (Order order : orders) {
            if (order.getStatus() != OrderStatus.COMPLETED) continue;

            BigDecimal amount = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
            totalSales = totalSales.add(amount);

            switch (order.getPaymentMethod()) {
                case CASH -> totalCash = totalCash.add(amount);
                case CARD -> totalCard = totalCard.add(amount);
                default -> totalOther = totalOther.add(amount);
            }
        }

        Closure closure = Closure.builder()
                .id(UUID.randomUUID().toString())
                .storeId(request.storeId())
                .userId(request.userId())
                .closureDate(request.closureDate())
                .startDatetime(request.startDatetime())
                .endDatetime(request.endDatetime())
                .totalSales(totalSales)
                .totalCash(totalCash)
                .totalCard(totalCard)
                .totalOther(totalOther)
                .totalOrders(orders.size())
                .status(ClosureStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        closureRepository.save(closure);
        return ClosureMapper.toResponse(closure);
    }

    @Override
    @Transactional(readOnly = true)
    public ClosureResponse getClosureById(String id) {
        log.info("Fetching closure by id={}", id);
        Closure closure = closureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "closure.not.found", id));
        return ClosureMapper.toResponse(closure);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClosureResponse> getClosures(String storeId, String userId, String status, String dateFrom, String dateTo) {
        log.info("Fetching closures for storeId={}, userId={}, status={}, dateFrom={}, dateTo={}", storeId, userId, status, dateFrom, dateTo);

        List<Closure> closures = closureRepository.findAll().stream()
                .filter(c -> isNull(storeId) || c.getStoreId().equals(storeId))
                .filter(c -> isNull(userId) || c.getUserId().equals(userId))
                .filter(c -> isNull(status) || c.getStatus().name().equalsIgnoreCase(status))
                .filter(c -> {
                    LocalDate from = dateFrom != null ? LocalDate.parse(dateFrom) : null;
                    LocalDate to = dateTo != null ? LocalDate.parse(dateTo) : null;
                    return (from == null || !c.getClosureDate().isBefore(from)) &&
                            (to == null || !c.getClosureDate().isAfter(to));
                })
                .toList();

        return closures.stream().map(ClosureMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ClosureResponse updateClosureStatus(String id, UpdateClosureStatusRequest request) {
        log.info("Updating status for closure id={} to {} by user={}", id, request.status(), request.updatedBy());

        Closure closure = closureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "closure.not.found", id));

        ClosureStatus newStatus;
        try {
            newStatus = ClosureStatus.valueOf(request.status().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "closure.invalid.status", request.status());
        }

        if (closure.getStatus() == newStatus) {
            throw new BusinessException(HttpStatus.CONFLICT, "closure.status.same", id);
        }

        if (closure.getStatus() == ClosureStatus.FINALIZED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "closure.already.finalized", id);
        }

        if (closure.getStatus() == ClosureStatus.CLOSED && newStatus == ClosureStatus.OPEN) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "closure.invalid.transition", closure.getStatus());
        }

        closure.setStatus(newStatus);
        closure.setUpdatedBy(request.updatedBy());
        closure.setUpdatedAt(LocalDateTime.now());

        closureRepository.save(closure);

        return ClosureMapper.toResponse(closure);
    }

    @Override
    @Transactional
    public ClosureResponse finalizeClosure(String id, FinalizeClosureRequest request) {
        Closure closure = closureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "closure.not.found", id));

        if (closure.getStatus() == ClosureStatus.FINALIZED) {
            throw new BusinessException(HttpStatus.CONFLICT, "closure.already.finalized", id);
        }

        if (closure.getStatus() != ClosureStatus.CLOSED) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "closure.invalid.transition", closure.getStatus());
        }

        closure.setStatus(ClosureStatus.FINALIZED);
        closure.setUpdatedAt(LocalDateTime.now());
        closure.setUpdatedBy(request.updatedBy());

        closureRepository.save(closure);
        return ClosureMapper.toResponse(closure);
    }

    private void validateNoOverlap(CreateClosureRequest request) {
        boolean exists = closureRepository.hasOverlappingClosure(
                request.storeId(),
                request.closureDate(),
                request.startDatetime(),
                request.endDatetime()
        );
        if (exists) {
            throw new BusinessException(HttpStatus.CONFLICT, "closure.overlap.exists", request.storeId());
        }
    }
}
