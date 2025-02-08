package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.product.AddProductRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.product.UpdateProductRequestDto;
import com.sasindu.springsecurity.abstractions.interfaces.IProductService;
import com.sasindu.springsecurity.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    @Override
    public Product addProduct(AddProductRequestDto request) {
        return null;
    }

    @Override
    public Product updateProduct(UpdateProductRequestDto request, Long id) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public List<Product> getFilteredProducts(String name, String category, String brand) {
        return List.of();
    }
}
