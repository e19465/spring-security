package com.sasindu.springsecurity.repository;

import com.sasindu.springsecurity.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Product repository - DATABASE LAYER for Product entity
 */
public interface IProductRepository extends JpaRepository<Product, Long> {
}
