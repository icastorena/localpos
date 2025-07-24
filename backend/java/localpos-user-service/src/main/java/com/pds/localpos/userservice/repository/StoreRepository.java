package com.pds.localpos.userservice.repository;

import com.pds.localpos.userservice.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

    Set<Store> findByCodeIn(Set<String> codes);

    Optional<Store> findByCode(String code);
}
