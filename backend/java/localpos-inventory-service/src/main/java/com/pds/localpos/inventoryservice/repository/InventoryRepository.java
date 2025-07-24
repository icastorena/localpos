package com.pds.localpos.inventoryservice.repository;

import com.pds.localpos.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    Optional<Inventory> findByProductIdAndStoreId(String productId, String storeId);
}
