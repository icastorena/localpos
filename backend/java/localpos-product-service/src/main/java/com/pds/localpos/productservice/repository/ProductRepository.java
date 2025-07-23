package com.pds.localpos.productservice.repository;

import com.pds.localpos.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategory_Id(String categoryId);

    Optional<Product> findByBarcode(String barcode);
}
