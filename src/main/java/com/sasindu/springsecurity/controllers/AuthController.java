package com.sasindu.springsecurity.controllers;


import com.sasindu.springsecurity.abstractions.dto.request.auth.LoginRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.auth.RegisterUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.response.user.UserResponseDto;
import com.sasindu.springsecurity.abstractions.interfaces.IAuthService;
import com.sasindu.springsecurity.helpers.ApiResponse;
import com.sasindu.springsecurity.helpers.ErrorResponse;
import com.sasindu.springsecurity.helpers.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.url.prefix}" + "/auth")
public class AuthController {
    private final IAuthService _authService;

    @Value("${application.environment}")
    String environment;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterUserRequestDto request){
        try{
            UserResponseDto response = _authService.register(request).toUserResponseDto();
            return SuccessResponse.handleSuccess("Registration successful", response, HttpStatus.CREATED.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto request, HttpServletResponse response){
        try{
            _authService.login(request, response);
            return SuccessResponse.handleSuccess("Login successful", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response){
        try{
            _authService.logout(response);
            return SuccessResponse.handleSuccess("Logout successful", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


    @GetMapping("/refresh-tokens")
    public ResponseEntity<ApiResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        try{
            _authService.refreshToken(request, response);
            return SuccessResponse.handleSuccess("Token refreshed", null, HttpStatus.OK.value(),null);
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
 */