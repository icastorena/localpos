package com.pds.localpos.productservice.controller;

import com.pds.localpos.productservice.dto.ProductRequestDTO;
import com.pds.localpos.productservice.dto.ProductResponseDTO;
import com.pds.localpos.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;

    @GetMapping
    public List<ProductResponseDTO> getAll() {
        log.info("Fetching all products");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable String id) {
        log.info("Fetching product by ID: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO product) {
        log.info("Creating new product: {}", product.name());
        return ResponseEntity.ok(service.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable String id, @Valid @RequestBody ProductRequestDTO product) {
        log.info("Updating product ID {} with data: {}", id, product.name());
        return ResponseEntity.ok(service.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Deleting product by ID: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
