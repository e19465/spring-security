package com.sasindu.springsecurity.abstractions.dto.response.product;


import com.sasindu.springsecurity.abstractions.dto.response.category.CategoryResponseDto;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    @Column(nullable = false, length = 200)
    private String name;
    private CategoryResponseDto category;
}
