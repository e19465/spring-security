package com.sasindu.springsecurity.controllers;


import com.sasindu.springsecurity.abstractions.dto.request.product.AddProductRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.product.UpdateProductRequestDto;
import com.sasindu.springsecurity.abstractions.dto.response.product.ProductResponseDto;
import com.sasindu.springsecurity.abstractions.interfaces.IProductService;
import com.sasindu.springsecurity.entities.Product;
import com.sasindu.springsecurity.helpers.ApiResponse;
import com.sasindu.springsecurity.helpers.ErrorResponse;
import com.sasindu.springsecurity.helpers.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.url.prefix}" + "/product")
public class ProductController {
    private final IProductService _productService;


    /**
     * Create a new product
     * @param request AddProductRequestDto
     * @return ResponseEntity<ApiResponse>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequestDto request){
        try{
            ProductResponseDto response = _productService.addProduct(request).toProductResponse();
            return SuccessResponse.handleSuccess("Product created successfully", response, HttpStatus.CREATED.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Update an existing product
     * @param request UpdateProductRequestDto
     * @return ResponseEntity<ApiResponse>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequestDto request, @PathVariable Long productId){
        try{
            ProductResponseDto response = _productService.updateProduct(request, productId).toProductResponse();
            return SuccessResponse.handleSuccess("Product updated successfully", response, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Delete a product
     * @param productId Long
     * @return ResponseEntity<ApiResponse>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        try{
            _productService.deleteProduct(productId);
            return SuccessResponse.handleSuccess("Product deleted successfully", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }



    /**
     * Get a product by id
     * @param productId Long
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("/get-by-id/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try{
            ProductResponseDto response = _productService.getProductById(productId).toProductResponse();
            return SuccessResponse.handleSuccess("Product retrieved successfully", response, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }



    /**
     * Get all products
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllProducts(@RequestParam(required = false) String name, @RequestParam(required = false) String category){
        try{
            List<ProductResponseDto> response = _productService.getAllProducts(name, category).stream().map(Product::toProductResponse).toList();
            return SuccessResponse.handleSuccess("Products retrieved successfully",response, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }
}


/*
 * ENDPOINTS
 * 1. create - POST - http://localhost:9091/api/v1/product/create
 * 2. update - POST - http://localhost:9091/api/v1/product/update/{productId}
 * 3. delete - DELETE - http://localhost:9091/api/v1/product/delete/{productId}
 * 4. get-by-id - GET - http://localhost:9091/api/v1/product/get-by-id/{productId}
 * 5. get-all - GET - http://localhost:9091/api/v1/product/get-all {params: name, category}
 */