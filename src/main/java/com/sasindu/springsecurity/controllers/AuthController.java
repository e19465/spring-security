package com.sasindu.springsecurity.controllers;


import com.sasindu.springsecurity.abstractions.dto.request.user.AddUserRequestDto;
import com.sasindu.springsecurity.abstractions.dto.request.user.LoginRequestDto;
import com.sasindu.springsecurity.abstractions.dto.response.user.UserResponseDto;
import com.sasindu.springsecurity.abstractions.interfaces.IAuthService;
import com.sasindu.springsecurity.helpers.ApiResponse;
import com.sasindu.springsecurity.helpers.ErrorResponse;
import com.sasindu.springsecurity.helpers.SuccessResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.url.prefix}" + "/auth")
public class AuthController {
    private final IAuthService _authService;

    @Value("${application.environment}")
    String environment;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AddUserRequestDto request){
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
            // Obtain the access and refresh tokens
            Map<String, String> data = _authService.login(request);
            String access = data.get("access");
            String refresh = data.get("refresh");

            // sameSite = strict is set using environment file

            Cookie accessCookie = new Cookie("access", access);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(environment.equals("production"));
            accessCookie.setPath("/");
            accessCookie.setMaxAge(60 * 5); // 5 minutes

            Cookie refreshCookie = new Cookie("refresh", refresh);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(environment.equals("production"));
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7 days

            // Add the cookies to the response
            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);

            return SuccessResponse.handleSuccess("Login successful", null, HttpStatus.OK.value(),null);
        } catch (Exception e) {
            return ErrorResponse.handleError(e);
        }
    }


}

/*
* ENDPOINTS
* 1. Register - POST - http://localhost:9091/api/v1/auth/register
* 2. Login - POST - http://localhost:9091/api/v1/auth/login
* 3. Test - GET - http://localhost:9091/api/v1/auth/test
 */