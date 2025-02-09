package com.sasindu.springsecurity.abstractions.interfaces;

import com.sasindu.springsecurity.abstractions.dto.request.category.AddCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.category.UpdateCategoryRequestDto;
import com.sasindu.springsecurity.entities.Category;

import java.util.List;

public interface ICategoryService {

    /**
     * Create a new category
     * @param request AddCategoryRequestDto
     * @return Category
     */
    Category createCategory(AddCategoryRequestDto request);


    /**
     * Update an existing category
     * @param request UpdateCategoryRequestDto
     * @param categoryId Long
     * @return Category
     */
    Category updateCategory(UpdateCategoryRequestDto request,Long categoryId);


    /**
     * Get a category by id
     * @param categoryId Long
     * @return Category
     */
    Category getCategoryById(Long categoryId);


    /**
     * Get all categories
     * @param search String
     * @return List<Category>
     */
    List<Category> getAllCategories(String search);


    /**
     * Delete a category
     * @param categoryId Long
     */
    void deleteCategory(Long categoryId);


    /**
     * Get a category by name
     * @param name String
     * @return Category
     */
    Category getCategoryByName(String name);
}
