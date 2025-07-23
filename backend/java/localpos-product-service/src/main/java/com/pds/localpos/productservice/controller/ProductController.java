package com.pds.localpos.productservice.controller;

import com.pds.localpos.productservice.dto.request.ProductRequest;
import com.pds.localpos.productservice.dto.response.ProductResponse;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        log.info("GET /products - Fetching all products");
        List<ProductResponse> products = service.findAll();
        log.info("Fetched {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        log.info("GET /products/{} - Fetching product by ID", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest product) {
        log.info("POST /products - Creating new product: {}", product.name());
        return ResponseEntity.ok(service.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable String id, @Valid @RequestBody ProductRequest product) {
        log.info("PUT /products/{} - Updating product: {}", id, product.name());
        return ResponseEntity.ok(service.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.warn("DELETE /products/{} - Deleting product", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/barcode/{code}")
    public ResponseEntity<ProductResponse> getByBarcode(@PathVariable String code) {
        log.info("GET /products/barcode/{} - Fetching product by barcode", code);
        return ResponseEntity.ok(service.findByBarcode(code));
    }
}
