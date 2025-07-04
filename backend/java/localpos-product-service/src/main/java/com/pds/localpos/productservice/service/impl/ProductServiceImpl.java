package com.pds.localpos.productservice.service.impl;

import com.pds.localpos.common.exception.ResourceNotFoundException;
import com.pds.localpos.productservice.dto.ProductRequestDTO;
import com.pds.localpos.productservice.dto.ProductResponseDTO;
import com.pds.localpos.productservice.mapper.ProductMapper;
import com.pds.localpos.productservice.model.Category;
import com.pds.localpos.productservice.model.Product;
import com.pds.localpos.productservice.repository.CategoryRepository;
import com.pds.localpos.productservice.repository.ProductRepository;
import com.pds.localpos.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductResponseDTO> findAll() {
        log.info("Fetching all products");
        List<ProductResponseDTO> products = productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Found {} products", products.size());
        return products;
    }

    @Override
    public ProductResponseDTO findById(String id) {
        log.info("Fetching product by ID: {}", id);
        return productRepository.findById(id)
                .map(product -> {
                    log.info("Product found: {}", product.getName());
                    return ProductMapper.toDTO(product);
                })
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.not_found", id);
                });
    }

    @Override
    public ProductResponseDTO save(ProductRequestDTO productRequest) {
        log.info("Saving new product: {}", productRequest.name());
        Category category = getCategoryOrThrow(productRequest.categoryId());

        Product product = new Product();
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        log.info("Product saved with ID: {}", saved.getId());
        return ProductMapper.toDTO(saved);
    }

    @Override
    public ProductResponseDTO update(String id, ProductRequestDTO productRequest) {
        log.info("Updating product with ID: {}", id);
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(productRequest.name());
                    existing.setDescription(productRequest.description());
                    existing.setPrice(productRequest.price());
                    existing.setCategory(getCategoryOrThrow(productRequest.categoryId()));

                    Product updated = productRepository.save(existing);
                    log.info("Product updated: {}", updated.getId());
                    return ProductMapper.toDTO(updated);
                })
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.not_found", id);
                });
    }

    @Override
    public void delete(String id) {
        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        log.info("Product deleted with ID: {}", id);
    }

    private Category getCategoryOrThrow(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("Category not found with ID: {}", categoryId);
                    return new ResourceNotFoundException(HttpStatus.NOT_FOUND, "category.not_found", categoryId);
                });
    }
}
