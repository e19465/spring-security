package com.sasindu.springsecurity.abstractions.interfaces;

import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserRequestDto;
import com.sasindu.springsecurity.entities.AppUser;

public interface IUserService {

    /**
     * Get a user by id
     *
     * @param id Long object
     * @return User object
     */
    AppUser getUserAccountDetailsById(Long id);


    /**
     * Update a user
     *
     * @param request UpdateUserRequestDto object
     * @param id Long object
     * @return User object
     */
    AppUser updateUserAccount(UpdateUserRequestDto request, Long id);


    /**
     * Update a user password
     *
     * @param request PasswordUpdateRequestDto object
     * @param id Long object
     */
    void updateUserPassword(UpdateUserPasswordRequestDto request, Long id);


    /**
     * Delete a user
     *
     * @param id Long object
     */
    void deleteUserAccountById(Long id);
}
