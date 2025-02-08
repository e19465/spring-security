package com.sasindu.springsecurity.abstractions.interfaces;

import com.sasindu.springsecurity.abstractions.dto.request.product.AddProductRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.product.UpdateProductRequestDto;
import com.sasindu.springsecurity.entities.Product;

import java.util.List;

public interface IProductService {

    /**
     * Add a new product
     *
     * @param request AddProductRequestDto object
     * @return Product object
     */
    Product addProduct(AddProductRequestDto request);


    /**
     * Update a product
     *
     * @param request UpdateProductRequestDto object
     * @param id Long
     * @return Product object
     */
    Product updateProduct(UpdateProductRequestDto request, Long id);


    /**
     * Delete a product
     *
     * @param id Long
     */
    void deleteProduct(Long id);


    /**
     * Get a product by id
     *
     * @param id Long
     * @return Product object
     */
    Product getProductById(Long id);


    /**
     * Get all products
     *
     * @return List of Product objects
     */
    List<Product> getAllProducts();


    /**
     * Get filtered products
     *
     * @param name String
     * @param category String
     * @param brand String
     * @return List of Product objects
     */
    List<Product> getFilteredProducts(String name, String category, String brand);
}
