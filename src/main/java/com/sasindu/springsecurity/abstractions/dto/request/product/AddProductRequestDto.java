package com.sasindu.springsecurity.abstractions.dto.request.product;


import lombok.Data;

@Data
public class AddProductRequestDto {
    private String name;
    private Long categoryId;
}
