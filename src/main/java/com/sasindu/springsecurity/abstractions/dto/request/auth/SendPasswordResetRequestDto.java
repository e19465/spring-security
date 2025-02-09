package com.sasindu.springsecurity.abstractions.dto.request.auth;

import lombok.Data;

@Data
public class SendPasswordResetRequestDto {
    private String email;
}
