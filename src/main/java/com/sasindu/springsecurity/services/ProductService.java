package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.category.AddCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.product.AddProductRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.product.UpdateProductRequestDto;
import com.sasindu.springsecurity.abstractions.interfaces.ICategoryService;
import com.sasindu.springsecurity.abstractions.interfaces.IProductService;
import com.sasindu.springsecurity.entities.Category;
import com.sasindu.springsecurity.entities.Product;
import com.sasindu.springsecurity.exceptions.NotFoundException;
import com.sasindu.springsecurity.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final IProductRepository _productRepository;
    private final ICategoryService _categoryService;


    /**
     * Add a new product
     * @param request AddProductRequestDto
     * @return Product
     */
    @Override
    public Product addProduct(AddProductRequestDto request) {
        try {
            // Get the category by name
            Category cat = _categoryService.getCategoryByName(request.getCategoryName());

            // If category does not exist, create a new category
            if(cat == null){
                AddCategoryRequestDto requestDto = new AddCategoryRequestDto();
                requestDto.setName(request.getCategoryName());
                cat = _categoryService.createCategory(requestDto);
            }

            // Create a new product and save it and return
            Product product = new Product();
            product.setName(request.getName());
            product.setCategory(cat);
            return _productRepository.save(product);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Update an existing product
     * @param request UpdateProductRequestDto
     * @param id Long
     * @return Product
     */
    @Override
    public Product updateProduct(UpdateProductRequestDto request, Long id) {
        try {

            // Get the product by id, if not found throw an exception
            Product product = _productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            // Get the category by name
            Category category = _categoryService.getCategoryByName(request.getCategoryName());

            // If category does not exist, create a new category
            if(category == null){
                AddCategoryRequestDto requestDto = new AddCategoryRequestDto();
                requestDto.setName(request.getCategoryName());
                category = _categoryService.createCategory(requestDto);
            }

            // Update the product and save it and return
            product.setName(request.getName());
            product.setCategory(category);
            return _productRepository.save(product);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Delete a product
     * @param id Long
     */
    @Override
    public void deleteProduct(Long id) {
        try {
            // Get the product by id, if not found throw an exception
            Product product = _productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            // Delete the product
            _productRepository.delete(product);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Get a product by id
     * @param id Long
     * @return Product
     */
    @Override
    public Product getProductById(Long id) {
        try {
            return _productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found"));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Get all products - if name and category are null, return all products
     * if name is not null and category is null, return all products by name containing
     * if name is null and category is not null, return all products by category name containing
     * if name and category are not null, return all products by name and category name containing
     *
     * @param name String
     * @param category String
     * @return List<Product>
     */
    @Override
    public List<Product> getAllProducts(String name, String category) {
        try {
            if(name == null && category == null) {
                return _productRepository.findAll();
            }
            if(name != null && category == null) {
                return _productRepository.findAllByNameContaining(name);
            }
            if(name == null) {
                return _productRepository.findAllByCategoryNameContaining(category);
            }
            return _productRepository.findAllByNameContainingAndCategoryNameContaining(name, category);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
