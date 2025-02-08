package com.sasindu.springsecurity.abstractions.dto.request.user;


import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
