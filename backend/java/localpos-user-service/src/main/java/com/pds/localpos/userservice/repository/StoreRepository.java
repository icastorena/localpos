package com.pds.localpos.userservice.repository;

import com.pds.localpos.userservice.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface StoreRepository extends JpaRepository<Store, String> {

    Set<Store> findByCodeIn(Set<String> codes);
    
    Optional<Store> findByCode(String code);
}
