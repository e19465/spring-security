package com.sasindu.springsecurity.abstractions.interfaces;

import com.sasindu.springsecurity.abstractions.dto.request.auth.LoginRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.RegisterUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.ResetPasswordRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.VerifyEmailRequestDto;
import com.sasindu.springsecurity.entities.AppUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

    /**
     * Register a new user
     *
     * @param request AddUserRequestDto object
     * @return AppUser object
     */
    AppUser register(RegisterUserRequestDto request);


    /**
     * Login a user
     *
     * @param request LoginRequestDto object
     */
    void login(LoginRequestDto request, HttpServletResponse response);


    /**
     * Get the authenticated user
     *
     * @return AppUser object
     */
    AppUser getAuthenticatedUser();


    /**
     * Refresh the access token
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response);


    /**
     * Logout
     */
    void logout(HttpServletResponse response);


    /**
     * Send email verification otp email
     *
     * @param email String
     */
    void sendEmailVerificationOtpEmail(String email);


    /**
     * Send password reset otp email
     *
     * @param email - String
     */
    void sendPasswordResetOtpEmail(String email);


    /**
     * Verify email
     *
     * @param request - VerifyEmailRequestDto object
     */
    void verifyEmail(VerifyEmailRequestDto request);


    /**
     * Reset password
     *
     * @param request - ResetPasswordRequestDto object
     */
    void resetPassword(ResetPasswordRequestDto request);


    /**
     * Check if the logged in user is the same as the user with the given id
     *
     * @param id - Long
     * @return boolean
     */
    boolean checkLoggedInUserWithId(Long id);


    /**
     * Check if the given password is correct
     *
     * @param password - String
     * @param user - AppUser
     * @return boolean
     */
    boolean isPasswordCorrect(AppUser user, String password);


    /**
     * Check whether authenticated user is admin or not
     *
     * @return true: if admin, else return false
     */
    boolean isAuthenticatedUserAdmin();
}
