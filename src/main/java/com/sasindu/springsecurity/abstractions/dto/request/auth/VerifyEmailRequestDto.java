package com.sasindu.springsecurity.abstractions.dto.request.auth;


import lombok.Data;

@Data
public class VerifyEmailRequestDto {
    private String email;
    private String otp;
}
