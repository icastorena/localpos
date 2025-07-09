package com.pds.localpos.productservice.service;

import com.pds.localpos.productservice.dto.ProductRequestDTO;
import com.pds.localpos.productservice.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    List<ProductResponseDTO> findAll();

    ProductResponseDTO findById(String id);

    ProductResponseDTO save(ProductRequestDTO productRequest);

    ProductResponseDTO update(String id, ProductRequestDTO productRequest);

    void delete(String id);
}
