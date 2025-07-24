package com.pds.localpos.productservice.service;

import com.pds.localpos.productservice.dto.request.ProductRequest;
import com.pds.localpos.productservice.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> findAll();

    ProductResponse findById(String id);

    ProductResponse save(ProductRequest productRequest);

    ProductResponse update(String id, ProductRequest productRequest);

    void delete(String id);

    ProductResponse findByBarcode(String barcode);
}
