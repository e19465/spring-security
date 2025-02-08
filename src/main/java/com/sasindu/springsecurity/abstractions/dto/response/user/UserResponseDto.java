package com.sasindu.springsecurity.abstractions.dto.response.user;


import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isEmailVerified;
    private String role;
}
