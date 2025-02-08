package com.sasindu.springsecurity.repository;

import com.sasindu.springsecurity.abstractions.enums.AppUserRoles;
import com.sasindu.springsecurity.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Role repository - DATABASE LAYER for Role entity
 */
public interface IRoleRepository extends JpaRepository<Role, Long> {

    /**
     * Check if a role exists by name
     * @param role the role to check
     * @return true if the role exists, false otherwise
     */
    boolean existsByName(AppUserRoles role);
}
