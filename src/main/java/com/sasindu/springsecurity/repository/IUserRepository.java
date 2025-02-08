package com.sasindu.springsecurity.repository;

import com.sasindu.springsecurity.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * User repository - DATABASE LAYER for User entity
 */
public interface IUserRepository extends JpaRepository<AppUser, Long> {
}
