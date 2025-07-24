package com.pds.localpos.orderservice.repository;

import com.pds.localpos.orderservice.model.Closure;
import com.pds.localpos.orderservice.model.ClosureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClosureRepository extends JpaRepository<Closure, String> {

    List<Closure> findByStoreIdAndClosureDateBetween(String storeId, LocalDate from, LocalDate to);

    Optional<Closure> findByStoreIdAndClosureDateAndStatus(String storeId, LocalDate date, ClosureStatus status);

    List<Closure> findByStoreIdAndStatus(String storeId, ClosureStatus status);

    @Query("""
                SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
                FROM Closure c
                WHERE c.storeId = :storeId
                  AND c.closureDate = :closureDate
                  AND c.status = :status
            """)
    boolean isClosureAlreadyCreated(
            @Param("storeId") String storeId,
            @Param("closureDate") LocalDate closureDate,
            @Param("status") ClosureStatus status
    );

    @Query("""
                SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
                FROM Closure c
                WHERE c.storeId = :storeId
                  AND c.closureDate = :closureDate
                  AND (
                        (:start BETWEEN c.startDatetime AND c.endDatetime)
                        OR
                        (:end BETWEEN c.startDatetime AND c.endDatetime)
                        OR
                        (c.startDatetime BETWEEN :start AND :end)
                      )
            """)
    boolean hasOverlappingClosure(
            @Param("storeId") String storeId,
            @Param("closureDate") LocalDate closureDate,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
