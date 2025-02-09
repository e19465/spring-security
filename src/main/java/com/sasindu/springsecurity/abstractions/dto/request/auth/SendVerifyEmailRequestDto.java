package com.sasindu.springsecurity.abstractions.dto.request.auth;

import lombok.Data;

@Data
public class SendVerifyEmailRequestDto {
    private  String email;
}
