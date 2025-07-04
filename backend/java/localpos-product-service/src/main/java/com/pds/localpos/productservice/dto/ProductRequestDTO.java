package com.pds.localpos.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "product.name.required")
        @Size(max = 255, message = "product.name.max_length")
        String name,

        @Size(max = 1000, message = "product.description.max_length") // opcional
        String description,

        @NotNull(message = "product.price.required")
        @DecimalMin(value = "0.00", inclusive = true, message = "product.price.positive")
        @Digits(integer = 10, fraction = 2, message = "product.price.format")
        BigDecimal price,

        @NotBlank(message = "product.category.required")
        String categoryId
) {
}
