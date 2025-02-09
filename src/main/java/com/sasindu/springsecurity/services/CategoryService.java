package com.sasindu.springsecurity.services;

import com.sasindu.springsecurity.abstractions.dto.request.category.AddCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.category.UpdateCategoryRequestDto;
import com.sasindu.springsecurity.abstractions.interfaces.ICategoryService;
import com.sasindu.springsecurity.entities.Category;
import com.sasindu.springsecurity.exceptions.ConflictException;
import com.sasindu.springsecurity.exceptions.NotFoundException;
import com.sasindu.springsecurity.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class to handle category related operations
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryRepository _categoryRepository;


    /**
     * Create a new category
     * @param request AddCategoryRequestDto
     * @return Category
     */
    @Override
    public Category createCategory(AddCategoryRequestDto request) {
        try {
            if(_categoryRepository.existsByName(request.getName())) {
                throw new ConflictException("Category already exists");
            }

            Category category = new Category();
            category.setName(request.getName().toLowerCase());
            return _categoryRepository.save(category);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Update an existing category
     * @param request UpdateCategoryRequestDto
     * @param categoryId Long
     * @return Category
     */
    @Override
    public Category updateCategory(UpdateCategoryRequestDto request, Long categoryId) {
        try {
            Category category = _categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            if (_categoryRepository.existsByName(request.getName())) {
                throw new ConflictException("Category already exists");
            }

            category.setName(request.getName().toLowerCase());
            return _categoryRepository.save(category);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Get a category by id
     * @param categoryId Long
     * @return Category
     */
    @Override
    public Category getCategoryById(Long categoryId) {
        try {
            return _categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Get all categories
     * @param search String
     * @return List<Category>
     */
    @Override
    public List<Category> getAllCategories(String search) {
        try {
            if(search == null || search.isEmpty()) {
                return _categoryRepository.findAll();
            }
            return _categoryRepository.findAllByNameContaining(search);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Delete a category
     * @param categoryId Long
     */
    @Override
    public void deleteCategory(Long categoryId) {
        try {
            Category category = _categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            _categoryRepository.delete(category);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Get a category by name
     * @param name String
     * @return Category
     */
    @Override
    public Category getCategoryByName(String name) {
        try {
            return _categoryRepository.findByName(name);
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
