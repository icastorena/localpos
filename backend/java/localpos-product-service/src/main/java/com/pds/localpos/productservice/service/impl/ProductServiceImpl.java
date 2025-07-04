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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO findById(String id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.not_found", id));
    }

    @Override
    public ProductResponseDTO save(ProductRequestDTO productRequest) {
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "category.not_found", productRequest.categoryId()));

        Product product = new Product();
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setPrice(productRequest.price());
        product.setCategory(category);

        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    @Override
    public ProductResponseDTO update(String id, ProductRequestDTO productRequest) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(productRequest.name());
                    existing.setDescription(productRequest.description());
                    existing.setPrice(productRequest.price());

                    Category category = categoryRepository.findById(productRequest.categoryId())
                            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "category.not_found", productRequest.categoryId()));
                    existing.setCategory(category);

                    Product updated = productRepository.save(existing);
                    return ProductMapper.toDTO(updated);
                })
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "product.not_found", id));
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
