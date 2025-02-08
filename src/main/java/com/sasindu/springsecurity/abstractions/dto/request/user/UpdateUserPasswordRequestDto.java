package com.sasindu.springsecurity.abstractions.dto.request.user;

import lombok.Data;

@Data
public class UpdateUserPasswordRequestDto {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
