package com.sasindu.springsecurity.abstractions.dto.request.auth;

import lombok.Data;

@Data
public class RegisterUserRequestDto {
    private String firstName;
    private String lastName = "";
    private String email;
    private String password;
    private String confirmPassword;
}
