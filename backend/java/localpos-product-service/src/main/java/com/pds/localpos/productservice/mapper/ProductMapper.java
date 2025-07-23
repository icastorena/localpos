package com.pds.localpos.productservice.mapper;

import com.pds.localpos.productservice.dto.response.CategoryResponse;
import com.pds.localpos.productservice.dto.response.ProductResponse;
import com.pds.localpos.productservice.model.Category;
import com.pds.localpos.productservice.model.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductMapper {

    public static ProductResponse toDTO(Product product) {
        Category category = product.getCategory();
        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );

        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getBarcode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                categoryResponse,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
