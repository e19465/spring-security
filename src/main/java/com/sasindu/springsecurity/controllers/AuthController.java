package com.sasindu.springsecurity.controllers;


import com.sasindu.springsecurity.abstractions.dto.request.auth.*;
import com.sasindu.springsecurity.abstractions.dto.response.user.UserResponseDto;
import com.sasindu.springsecurity.abstractions.interfaces.IAuthService;
import com.sasindu.springsecurity.helpers.ApiResponse;
import com.sasindu.springsecurity.helpers.ErrorResponse;
import com.sasindu.springsecurity.helpers.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.url.prefix}" + "/auth")
public class AuthController {
    private final IAuthService _authService;


    /**
     * Registers a new user
     * @param request RegisterUserRequestDto
     * @return ResponseEntity<ApiResponse>
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterUserRequestDto request){
        try{
            UserResponseDto response = _authService.register(request).toUserResponseDto();
            return SuccessResponse.handleSuccess("Registration successful! A verification email has been sent. Please check your inbox and verify your email to sign in", response, HttpStatus.CREATED.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Logs in a user
     * @param request LoginRequestDto
     * @param response HttpServletResponse
     * @return ResponseEntity<ApiResponse>
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto request, HttpServletResponse response){
        try{
            _authService.login(request, response);
            return SuccessResponse.handleSuccess("Login successful", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Logs out a user
     * @param response HttpServletResponse
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response){
        try{
            _authService.logout(response);
            return SuccessResponse.handleSuccess("Logout successful", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }



    /**
     * Refreshes the tokens
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ResponseEntity<ApiResponse>
     */
    @GetMapping("/refresh-tokens")
    public ResponseEntity<ApiResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        try{
            _authService.refreshToken(request, response);
            return SuccessResponse.handleSuccess("Token refreshed", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Verifies the email
     * @param request VerifyEmailRequestDto
     * @return ResponseEntity<ApiResponse>
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestBody VerifyEmailRequestDto request){
        try{
            _authService.verifyEmail(request);
            return SuccessResponse.handleSuccess("Email verified successfully", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Sends the email verification email
     * @param request SendVerifyEmailRequestDto object
     * @return ResponseEntity<ApiResponse>
     */
    @PostMapping("/send-email-verification-email")
    public ResponseEntity<ApiResponse> sendEmailVerificationEmail(@RequestBody SendVerifyEmailRequestDto request){
        try{
            _authService.sendEmailVerificationOtpEmail(request.getEmail());
            return SuccessResponse.handleSuccess("Email verification email sent successfully", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Sends the password reset email
     * @param request SendPasswordResetRequestDto object
     * @return ResponseEntity<ApiResponse>
     */
    @PostMapping("/send-password-reset-email")
    public ResponseEntity<ApiResponse> sendPasswordResetEmail(@RequestBody SendPasswordResetRequestDto request){
        try{
            _authService.sendPasswordResetOtpEmail(request.getEmail());
            return SuccessResponse.handleSuccess("Password reset email sent successfully", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    /**
     * Resets the password
     * @param request ResetPasswordRequestDto object
     * @return ResponseEntity<ApiResponse>
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequestDto request){
        try{
            _authService.resetPassword(request);
            return SuccessResponse.handleSuccess("Password reset successfully", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }
}

/*
* ENDPOINTS
* 1. Register - POST - http://localhost:9091/api/v1/auth/register
* 2. Login - POST - http://localhost:9091/api/v1/auth/login
* 3. Logout - GET - http://localhost:9091/api/v1/auth/logout
* 4. Refresh Tokens - GET - http://localhost:9091/api/v1/auth/refresh-tokens
* 5. Verify Email - POST - http://localhost:9091/api/v1/auth/verify-email
* 6. Send Email Verification Email - POST - http://localhost:9091/api/v1/auth/send-email-verification-email
* 7. Send Password Reset Email - POST - http://localhost:9091/api/v1/auth/send-password-reset-email
* 8. Reset Password - POST - http://localhost:9091/api/v1/auth/reset-password
 */