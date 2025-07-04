package com.pds.localpos.productservice.mapper;

import com.pds.localpos.productservice.dto.CategoryDTO;
import com.pds.localpos.productservice.dto.ProductResponseDTO;
import com.pds.localpos.productservice.model.Category;
import com.pds.localpos.productservice.model.Product;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductMapper {

    public static ProductResponseDTO toDTO(Product product) {
        Category category = product.getCategory();
        CategoryDTO categoryDTO = new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                categoryDTO,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
