package com.sasindu.springsecurity.controllers;


import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.UpdateUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.response.user.UserResponseDto;
import com.sasindu.springsecurity.abstractions.interfaces.IUserService;
import com.sasindu.springsecurity.entities.AppUser;
import com.sasindu.springsecurity.helpers.ApiResponse;
import com.sasindu.springsecurity.helpers.ErrorResponse;
import com.sasindu.springsecurity.helpers.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.url.prefix}" + "/users")
public class UserController {
    private final IUserService _userService;


    /**
     * Get the details of the authenticated user
     *
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("/get-account-by-id/{userId}")
    public ResponseEntity<ApiResponse> getAccountDetailsById(@PathVariable  Long userId){
        try{
            UserResponseDto response = _userService.getUserAccountDetailsById(userId).toUserResponseDto();
            return SuccessResponse.handleSuccess("User account details", response, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Update the details of the authenticated user
     *
     * @param request UpdateUserRequestDto object
     * @return ResponseEntity<ApiResponse>
     */
    @PutMapping("/update-account/{userId}")
    public ResponseEntity<ApiResponse> updateAccount(@RequestBody UpdateUserRequestDto request, @PathVariable Long userId){
        try{
            UserResponseDto response = _userService.updateUserAccount(request, userId).toUserResponseDto();
            return SuccessResponse.handleSuccess("User account updated", response, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Update the password of the authenticated user
     *
     * @param request UpdateUserPasswordRequestDto object
     * @return ResponseEntity<ApiResponse>
     */
    @PutMapping("/update-password/{userId}")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody UpdateUserPasswordRequestDto request, @PathVariable Long userId){
        try{
            _userService.updateUserPassword(request, userId);
            return SuccessResponse.handleSuccess("User password updated", null, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Delete the authenticated user
     *
     * @return ResponseEntity<ApiResponse>
     */
    @DeleteMapping("/delete-account/{userId}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long userId){
        try{
            _userService.deleteUserAccountById(userId);
            return SuccessResponse.handleSuccess("User account deleted", null, HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Get list of all users for admin
     *
     * @return List of UserResponseDto objects
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsersAdmin(){
        try{
            List<UserResponseDto> response = _userService.getAllUsersForAdmin()
                    .stream()
                    .map(AppUser::toUserResponseDto)
                    .toList();
            return SuccessResponse.handleSuccess("All users", response, HttpStatus.CREATED.value(), null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }
}


/*
 * ENDPOINTS
 * 1. get account details - GET - http://localhost:9091/api/v1/users/get-account-by-id/{userId}
 * 2. update account - PUT - http://localhost:9091/api/v1/users/update-account/{userId}
 * 3. update password - PUT - http://localhost:9091/api/v1/users/update-password/{userId}
 * 4. delete account - DELETE - http://localhost:9091/api/v1/users/delete-account/{userId}
 * 5. get all users - GET - http://localhost:9091/api/v1/users/all
 */