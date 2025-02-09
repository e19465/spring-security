package com.sasindu.springsecurity.abstractions.dto.request.product;


import lombok.Data;

@Data
public class UpdateProductRequestDto {
    private String name;
    private String categoryName;
}
