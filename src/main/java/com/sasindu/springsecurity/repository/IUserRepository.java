package com.sasindu.springsecurity.repository;

import com.sasindu.springsecurity.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * User repository - DATABASE LAYER for User entity
 */
public interface IUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Find user by email
     * @param email The email
     * @return The user
     */
    AppUser findByEmail(String email);


    /**
     * Check if user exists by email
     * @param email The email
     * @return True if exists, false otherwise
     */
    boolean existsByEmail(String email);
}
