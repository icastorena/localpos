package com.pds.localpos.orderservice.repository;

import com.pds.localpos.orderservice.model.Closure;
import com.pds.localpos.orderservice.model.ClosureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ClosureRepository extends JpaRepository<Closure, String> {

    Optional<Closure> findByStoreIdAndClosureDate(String storeId, LocalDate closureDate);

    Optional<Closure> findByStoreIdAndStatus(String storeId, ClosureStatus status);
}
