package com.sasindu.springsecurity.services;

import com.sasindu.springsecurity.abstractions.dto.request.category.AddCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.category.UpdateCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.interfaces.ICategoryService;
import com.sasindu.springsecurity.entities.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class to handle category related operations
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    @Override
    public Category createCategory(AddCategoryRequestDto request) {
        return null;
    }

    @Override
    public Category updateCategory(UpdateCategoryRequestDto request, Long categoryId) {
        return null;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return null;
    }

    @Override
    public List<Category> getAllCategories(String search) {
        return List.of();
    }

    @Override
    public void deleteCategory(Long categoryId) {

    }
}
