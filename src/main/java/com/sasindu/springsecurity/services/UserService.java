package com.sasindu.springsecurity.services;


import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserRequestDto;
import com.sasindu.springsecurity.abstractions.interfaces.IAuthService;
import com.sasindu.springsecurity.abstractions.interfaces.IUserService;
import com.sasindu.springsecurity.entities.AppUser;
import com.sasindu.springsecurity.exceptions.BadRequestException;
import com.sasindu.springsecurity.exceptions.ForbiddenException;
import com.sasindu.springsecurity.exceptions.NotFoundException;
import com.sasindu.springsecurity.helpers.HelperUtilMethods;
import com.sasindu.springsecurity.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository _userRepository;
    private final IAuthService _authService;

    /**
     * Gets the details of a user account by id
     * @param id - User id
     * @return AppUser
     */
    @Override
    public AppUser getUserAccountDetailsById(Long id) {
        try{
            if (!_authService.isAuthenticatedUserAdmin() && !_authService.checkLoggedInUserWithId(id)) {
                throw new ForbiddenException("Access Denied");
            }
            return _userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Update the details of a user account
     * @param request - UpdateUserRequestDto
     * @param id - User id
     * @return AppUser
     */
    @Override
    public AppUser updateUserAccount(UpdateUserRequestDto request, Long id) {
        try{
            // Check if the user is trying to update their own account
            if(_authService.checkLoggedInUserWithId(id)) {
                throw new ForbiddenException("Access denied");
            }

            // Get the user by id
            AppUser user = _userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            // Update user details with the new values and save, then return the updated user
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            return _userRepository.save(user);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Update the password of a user account
     * @param request - UpdateUserPasswordRequestDto
     * @param id - User id
     */
    @Override
    public void updateUserPassword(UpdateUserPasswordRequestDto request, Long id) {
        try{
            // Check if the user is trying to update their own account
            if(_authService.checkLoggedInUserWithId(id)) {
                throw new ForbiddenException("Access denied");
            }

            // Get the user by id
            AppUser user = _userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            // Check if the old password is correct
            if(!_authService.isPasswordCorrect(user, request.getOldPassword())) {
                throw new ForbiddenException("Incorrect password");
            }

            // Check if the new password is the same as the old password
            if(request.getOldPassword().equals(request.getNewPassword())) {
                throw new ForbiddenException("New password cannot be the same as the old password");
            }


            // Check if the new password and confirm password match
            if(!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new ForbiddenException("New password and confirm password do not match");
            }


            // Check if the new password is strong
            if(!HelperUtilMethods.isPasswordStrong(request.getNewPassword())) {
                throw new BadRequestException("Password should be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character");
            }

            // Update the user password and save
            user.setPassword(request.getNewPassword());
            _userRepository.save(user);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Delete a user account by id
     * @param id - User id
     */
    @Override
    @Transactional
    public void deleteUserAccountById(Long id) {
        try{
            // Check if the user is trying to delete their own account
            if(_authService.checkLoggedInUserWithId(id)) {
                throw new ForbiddenException("Access denied");
            }

            // Get the user by id and delete
            AppUser user = _userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            _userRepository.delete(user);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the list of all users for admin
     *
     * @return List of AppUser
     */
    @Override
    public List<AppUser> getAllUsersForAdmin() {
        try{
           return _userRepository.findAll();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
