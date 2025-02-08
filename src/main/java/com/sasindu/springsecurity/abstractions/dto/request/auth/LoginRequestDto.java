package com.sasindu.springsecurity.abstractions.dto.request.auth;


import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
