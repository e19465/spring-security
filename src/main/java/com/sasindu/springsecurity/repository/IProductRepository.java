package com.sasindu.springsecurity.repository;

import com.sasindu.springsecurity.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Product repository - DATABASE LAYER for Product entity
 */
public interface IProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find all products by name containing
     * @param name String
     * @return List<Product>
     */
    List<Product> findAllByNameContaining(String name);


    /**
     * Find all products by category name containing
     * @param category String
     * @return List<Product>
     */
    List<Product> findAllByCategoryNameContaining(String category);



    /**
     * Find all products by name and category name containing
     * @param name String
     * @param category String
     * @return List<Product>
     */
    List<Product> findAllByNameContainingAndCategoryNameContaining(String name, String category);
}
