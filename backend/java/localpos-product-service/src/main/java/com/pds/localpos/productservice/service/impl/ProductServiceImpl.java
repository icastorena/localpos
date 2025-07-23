package com.pds.localpos.productservice.service.impl;

import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.productservice.dto.request.ProductRequest;
import com.pds.localpos.productservice.dto.response.ProductResponse;
import com.pds.localpos.productservice.mapper.ProductMapper;
import com.pds.localpos.productservice.model.Category;
import com.pds.localpos.productservice.model.Product;
import com.pds.localpos.productservice.repository.CategoryRepository;
import com.pds.localpos.productservice.repository.ProductRepository;
import com.pds.localpos.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductResponse> findAll() {
        log.info("Service: Fetching all products");
        List<ProductResponse> products = productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Service: {} products fetched", products.size());
        return products;
    }

    @Override
    public ProductResponse findById(String id) {
        log.info("Service: Fetching product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Service: Product not found with ID: {}", id);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.not_found", id);
                });
        log.info("Service: Product found - {}", product.getName());
        return ProductMapper.toDTO(product);
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        log.info("Service: Saving new product: {}", request.name());

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(getCategoryOrThrow(request.categoryId()));

        Product saved = productRepository.save(product);
        log.info("Service: Product saved with ID: {}", saved.getId());
        return ProductMapper.toDTO(saved);
    }

    @Override
    public ProductResponse update(String id, ProductRequest request) {
        log.info("Service: Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Service: Product not found with ID: {}", id);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.not_found", id);
                });

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(getCategoryOrThrow(request.categoryId()));

        Product updated = productRepository.save(product);
        log.info("Service: Product updated with ID: {}", updated.getId());
        return ProductMapper.toDTO(updated);
    }

    @Override
    public void delete(String id) {
        log.info("Service: Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            log.warn("Service: Product not found for deletion: {}", id);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.not_found", id);
        }
        productRepository.deleteById(id);
        log.info("Service: Product deleted with ID: {}", id);
    }

    private Category getCategoryOrThrow(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Service: Category not found with ID: {}", categoryId);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "category.not_found", categoryId);
                });
    }

    @Override
    public ProductResponse findByBarcode(String barcode) {
        log.info("Service: Fetching product by barcode: {}", barcode);
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> {
                    log.warn("Service: Product not found with barcode: {}", barcode);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.barcode.not_found", barcode);
                });
        return ProductMapper.toDTO(product);
    }
}
