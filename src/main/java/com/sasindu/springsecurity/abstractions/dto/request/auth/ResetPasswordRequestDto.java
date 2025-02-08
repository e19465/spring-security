package com.sasindu.springsecurity.abstractions.dto.request.auth;

import lombok.Data;

@Data
public class ResetPasswordRequestDto {
    private String email;
    private String otp;
    private String password;
    private String confirmPassword;
}
