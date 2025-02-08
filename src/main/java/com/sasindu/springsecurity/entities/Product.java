package com.sasindu.springsecurity.entities;

import com.sasindu.springsecurity.abstractions.dto.response.product.ProductResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "products")
public class Product {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 200)
    private String name;

    @ManyToOne
    @JoinColumn (name = "category_id", nullable = false)
    private Category category;


    /**
     * Converts the product entity to a product response dto
     * @return ProductResponseDto
     */
    public ProductResponseDto toProductResponse() {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(this.id);
        productResponseDto.setName(this.name);
        productResponseDto.setCategory(this.category.toCategoryResponse());
        return productResponseDto;
    }
}
