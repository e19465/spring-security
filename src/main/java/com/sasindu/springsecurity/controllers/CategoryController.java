package com.sasindu.springsecurity.controllers;


import com.sasindu.springsecurity.abstractions.dto.request.category.AddCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.category.UpdateCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.dto.response.category.CategoryResponseDto;
import com.sasindu.springsecurity.abstractions.interfaces.ICategoryService;
import com.sasindu.springsecurity.entities.Category;
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
@RequestMapping("${api.url.prefix}" + "/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService _categoryService;


    /**
     * Create a new category - only accessible by admins
     * @param request AddCategoryRequestDto
     * @return ResponseEntity<ApiResponse>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody AddCategoryRequestDto request) {
        try {
            CategoryResponseDto category = _categoryService.createCategory(request).toCategoryResponse();
            return SuccessResponse.handleSuccess("Category created successfully", category, HttpStatus.CREATED.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Update an existing category - only accessible by admins
     * @param request AddCategoryRequestDto
     * @return ResponseEntity<ApiResponse>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody UpdateCategoryRequestDto request, @PathVariable Long categoryId) {
        try {
            CategoryResponseDto category = _categoryService.updateCategory(request, categoryId).toCategoryResponse();
            return SuccessResponse.handleSuccess("Category updated successfully", category, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Get a category by id
     * @param categoryId Long
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("/get-by-id/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            CategoryResponseDto category = _categoryService.getCategoryById(categoryId).toCategoryResponse();
            return SuccessResponse.handleSuccess("Category retrieved successfully", category, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Get all categories
     * @param search String
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllCategories(@RequestParam(required = false) String search) {
        try {

            List<CategoryResponseDto> categories = _categoryService.getAllCategories(search).stream().map(Category::toCategoryResponse).toList();

            return SuccessResponse.handleSuccess("Categories retrieved successfully", categories, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Delete a category - only accessible by admins
     * @param categoryId Long
     * @return ResponseEntity<ApiResponse>
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        try {
            _categoryService.deleteCategory(categoryId);
            return SuccessResponse.handleSuccess("Category deleted successfully", null, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }
}


/*
 * ENDPOINTS
 * 1. create - POST - http://localhost:9091/api/v1/category/create
 * 2. update - PUT - http://localhost:9091/api/v1/category/update/{categoryId}
 * 3. get - GET - http://localhost:9091/api/v1/category/get-by-id/{categoryId}
 * 4. get-all - GET - http://localhost:9091/api/v1/category/get-all
 * 5. delete - DELETE - http://localhost:9091/api/v1/category/delete/{categoryId}
 */