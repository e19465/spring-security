package com.sasindu.springsecurity.entities;


import com.sasindu.springsecurity.abstractions.dto.response.category.CategoryResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "categories")
public class Category {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column (nullable = false, unique = true)
    private String name;


    @OneToMany (mappedBy = "category" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();


    /**
     * Converts the category entity to a category response dto
     * @return CategoryResponseDto
     */
    public CategoryResponseDto toCategoryResponse() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(this.id);
        categoryResponseDto.setName(this.name);
        return categoryResponseDto;
    }
}
