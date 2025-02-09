package com.sasindu.springsecurity.repository;

import com.sasindu.springsecurity.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Category entity
 */
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Check if a category exists by name
     * @param name String
     * @return boolean
     */
    boolean existsByName(String name);

    /**
     * Find all categories by name containing the search string
     * @param search String
     * @return List<Category>
     */
    List<Category> findAllByNameContaining(String search);
}
