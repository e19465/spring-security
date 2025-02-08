package com.sasindu.springsecurity.controllers;


import com.sasindu.springsecurity.helpers.ApiResponse;
import com.sasindu.springsecurity.helpers.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.url.prefix}" + "/products")
public class ProductController {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test(){
        return SuccessResponse.handleSuccess("Test successful", null, HttpStatus.OK.value(),null);
    }
}


/*
 * ENDPOINTS
 * 1. Test - GET - http://localhost:9091/api/v1/products/test
 */